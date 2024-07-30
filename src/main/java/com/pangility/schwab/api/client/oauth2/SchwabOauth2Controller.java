package com.pangility.schwab.api.client.oauth2;

import com.pangility.schwab.api.client.common.ApiUnauthorizedException;
import com.pangility.schwab.api.client.common.OnApiEnabledCondition;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Controller used by the client controllers
 * for delegating the oauth2 authentication tasks for the Schwab API.
 * See the <a href="https://developer.schwab.com/user-guides/get-started/authenticate-with-oauth">Authenticate with OAuth</a>
 * Getting Started Guide found on the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@RestController
@Conditional(OnApiEnabledCondition.class)
@Slf4j
public class SchwabOauth2Controller {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    @Value("${schwab-api.marketDataPath}")
    private String schwabMarketDataPath;
    @Value("${schwab-api.apiVersion}")
    private String schwabApiVersion;
    @Value("${schwab-api.oauth2.authorization-uri}")
    private String schwabAuthorizationUri;
    @Value("${schwab-api.oauth2.authorization-grant-type}")
    private String schwabAuthorizationGrantType;
    @Value("${schwab-api.oauth2.refresh-grant-type}")
    private String schwabRefreshGrantType;
    @Value("${schwab-api.oauth2.authorization-scope}")
    private String schwabAuthorizationScope;
    @Value("${schwab-api.oauth2.token-uri}")
    private String schwabTokenUri;
    @Value("${schwab-api.oauth2.redirect-uri}")
    private String schwabRedirectUri;
    @Value("${schwab-api.oauth2.clientId}")
    private String schwabClientId;
    @Value("${schwab-api.oauth2.clientSecret}")
    private String schwabClientSecret;

    private final HashMap<String, SchwabAccount> accountMapByUserId = new HashMap<>();

    private final HashMap<UUID, UuidMapInfo> uuids = new HashMap<>();

    private SchwabTokenHandler tokenHandler = null;

    /**
     * initialize the controller with the Schwab account information
     * @param schwabAccounts {@link List}{@literal <}{@link SchwabAccount}{@literal >}
     */
    public void init(@NonNull List<SchwabAccount> schwabAccounts) {
        this.init(schwabAccounts, null);
    }

    /**
     * initialize the controller with the Schwab account information
     * @param schwabAccounts {@link List}{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NonNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
        schwabAccounts.forEach(schwabAccount -> accountMapByUserId.put(schwabAccount.getUserId(), schwabAccount));
        this.tokenHandler = tokenHandler;
    }

    /**
     * has the controller been initialized
     * @return Boolean
     */
    public Boolean isInitialized() {
        return !this.accountMapByUserId.isEmpty();
    }

    /**
     * validate the refresh token (i.e. is it present and not expired)
     * @param schwabAccount {@link SchwabAccount}
     * @throws RefreshTokenException throws if the refresh token is not present or expired
     */
    @Deprecated
    public void validateRefreshToken(SchwabAccount schwabAccount)
            throws RefreshTokenException {
        if(schwabAccount == null) {
            throw new RefreshTokenException("Unable to retrieve Refresh Token", schwabAccount);
        } else if(schwabAccount.getRefreshToken() == null) {
            throw new RefreshTokenException("Missing Refresh Token", schwabAccount);
        } else if(LocalDateTime.now().plusMinutes(60).isAfter(schwabAccount.getRefreshExpiration())) {
            throw new RefreshTokenException("Expired Refresh Token", schwabAccount);
        }
    }

    /**
     * validate the refresh token (i.e. is it present and not expired)
     * @param schwabAccount {@link SchwabAccount}
     * @return {@link Mono}{@literal <}{@link SchwabAccount}{@literal >}
     */
    public Mono<SchwabAccount> validateRefreshTokenToMono(SchwabAccount schwabAccount) {
        if(schwabAccount == null) {
            return Mono.error(new RefreshTokenException("Unable to retrieve Refresh Token", schwabAccount));
        } else if(schwabAccount.getRefreshToken() == null) {
            return Mono.error(new RefreshTokenException("Missing Refresh Token", schwabAccount));
        } else if(LocalDateTime.now().plusMinutes(60).isAfter(schwabAccount.getRefreshExpiration())) {
            return Mono.error(new RefreshTokenException("Expired Refresh Token", schwabAccount));
        } else {
            return Mono.just(schwabAccount);
        }
    }

    /**
     * gets the Schwab account info for the user id
     * @param schwabUserId {@literal @}NotNull String
     * @return {@link SchwabAccount}
     */
    public SchwabAccount getSchwabAccount(@NonNull String schwabUserId) {
        return accountMapByUserId.get(schwabUserId);
    }

    /**
     * gets the Schwab access token for the user id and retrieves a
     * new one if it's expired.
     * @param schwabUserId {@literal @}NotNull String
     * @return {@link Mono}{@literal <}{@link SchwabAccount}{@literal >}
     */
    public Mono<SchwabAccount> getAccessToken(@NonNull String schwabUserId) {
        Mono<SchwabAccount> schwabAccountMono = Mono.empty();
        SchwabAccount schwabAccount = accountMapByUserId.get(schwabUserId);
        if(schwabAccount != null) {
            return this.validateRefreshTokenToMono(schwabAccount)
                    .flatMap(validatedSchwabAccount -> {
                        String accessToken = schwabAccount.getAccessToken();
                        LocalDateTime accessExpiration = schwabAccount.getAccessExpiration();
                        if(accessToken == null || (accessExpiration != null && LocalDateTime.now().plusMinutes(5).isAfter(accessExpiration))) {
                            return this.refreshAccessToken(schwabAccount);
                        } else {
                            return Mono.just(schwabAccount);
                        }
                    });
        }
        return schwabAccountMono;
    }

    /**
     * refresh the Schwab access token for the user id
     * @param schwabUserId {@literal @}NotNull String
     * @return {@link Mono}{@literal <}{@link SchwabAccount}{@literal >}
     */
    public Mono<SchwabAccount> refreshAccessToken(@NonNull String schwabUserId) {
        Mono<SchwabAccount> response = Mono.empty();
        SchwabAccount schwabAccount = accountMapByUserId.get(schwabUserId);
        if(schwabAccount != null) {
            response = this.refreshAccessToken(schwabAccount);
        }
        return response;
    }

    /**
     * refresh the Schwab access token for the account
     * @param schwabAccount {@literal @}NotNull {@link SchwabAccount}
     * @return {@link Mono}{@literal <}{@link SchwabAccount}{@literal >}
     */
    public Mono<SchwabAccount> refreshAccessToken(@NonNull SchwabAccount schwabAccount) {
        String tokenAuthorizationHeader = "Basic " + new String(Base64.getMimeEncoder().encode((schwabClientId + ":" + schwabClientSecret).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(schwabTargetUrl)
                .pathSegment(schwabApiVersion, schwabTokenUri)
                .build()
                .toUri();

        LinkedMultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add("grant_type", schwabRefreshGrantType);
        bodyValues.add("refresh_token", schwabAccount.getRefreshToken());

        return WebClient.create().post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, tokenAuthorizationHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(bodyValues))
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(AuthorizationTokenInfo.class);
                    } else if(clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()){
                        if (clientResponse.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                            return Mono.error(new ApiUnauthorizedException());
                        } else {
                            return Mono.error(new ResponseStatusException(clientResponse.statusCode()));
                        }
                    }
                    return Mono.empty();
                })
                .onErrorResume(throwable -> {
                    if(throwable instanceof ApiUnauthorizedException) {
                        schwabAccount.setAccessToken(null);
                    }
                    return Mono.error(throwable);
                })
                .retryWhen(Retry.backoff(2, Duration.ZERO).filter(throwable -> throwable instanceof ApiUnauthorizedException))
                .flatMap(tokenInfo -> {
                    schwabAccount.setAccessToken(tokenInfo.getAccess_token());
                    schwabAccount.setAccessExpiration(LocalDateTime.now().plusSeconds(tokenInfo.getExpires_in()));
                    if(accountMapByUserId.containsKey(schwabAccount.getUserId())) {
                        accountMapByUserId.replace(schwabAccount.getUserId(), schwabAccount);
                    } else {
                        accountMapByUserId.put(schwabAccount.getUserId(), schwabAccount);
                    }
                    if(tokenHandler != null) {
                        tokenHandler.onAccessTokenChange(schwabAccount);
                    }
                    return Mono.just(schwabAccount);
                })
                .onErrorResume(e -> {
                    if(e instanceof WebClientResponseException wcre) {
                        String errorBody = "Unable to retrieve token: " + wcre.getResponseBodyAsString()
                                .replaceAll("\n", "")
                                .replaceAll(" ", "")
                                .replaceAll("\\{", "{ ")
                                .replaceAll("}", " }")
                                .replaceAll(":", " : ")
                                .replaceAll(",", ", ");
                        log.error(errorBody);
                        return Mono.error(new ResponseStatusException(
                                wcre.getStatusCode(), errorBody, wcre));
                    } else if(e instanceof ResponseStatusException rse) {
                        return Mono.error(rse);
                    } else {
                        log.error(this.exceptionToString((Exception) e));
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString((Exception) e), e));
                    }
                });
    }

    /**
     * end point for retrieving the refresh and access tokens
     * @param code {@literal @}RequestParam String
     * @param state {@literal @}RequestParam String
     * @return {@link Mono}{@literal <}{@link RedirectView}{@literal >}
     */
    @GetMapping(value = {"/oauth2/schwab/code"})
    public Mono<RedirectView> processCode(@RequestParam String code,
                            @RequestParam String state) {
        String tokenAuthorizationHeader = "Basic " + new String(Base64.getMimeEncoder().encode((schwabClientId + ":" + schwabClientSecret).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        UUID paramUuid = UUID.fromString(state);
        if (uuids.containsKey(paramUuid)) {
            UuidMapInfo uuidMapInfo = uuids.get(paramUuid);
            String schwabUserId = uuidMapInfo.getUserId();
            String callback = uuidMapInfo.getCallback();

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .pathSegment(schwabApiVersion, schwabTokenUri)
                    .build()
                    .toUri();

            LinkedMultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
            bodyValues.add("grant_type", schwabAuthorizationGrantType);
            bodyValues.add("access_type", "offline");
            bodyValues.add("code", code);
            bodyValues.add("client_id", schwabClientId);
            bodyValues.add("redirect_uri", schwabRedirectUri);

            return WebClient.create().post()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, tokenAuthorizationHeader)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData(bodyValues))
                    .exchangeToMono(clientResponse -> {
                        if(clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(AuthorizationTokenInfo.class);
                        } else if(clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()){
                            return Mono.error(new ResponseStatusException(clientResponse.statusCode()));
                        }
                        return Mono.empty();
                    })
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                    .flatMap(tokenInfo -> {
                        SchwabAccount schwabAccount;
                        if(!accountMapByUserId.containsKey(schwabUserId)) {
                            schwabAccount = new SchwabAccount();
                            schwabAccount.setUserId(schwabUserId);
                        } else {
                            schwabAccount = accountMapByUserId.get(schwabUserId);
                        }
                        if(schwabAccount != null) {
                            schwabAccount.setRefreshToken(tokenInfo.getRefresh_token());
                            schwabAccount.setRefreshExpiration(LocalDateTime.now().plusDays(7));
                            schwabAccount.setAccessToken(tokenInfo.getAccess_token());
                            schwabAccount.setAccessExpiration(LocalDateTime.now().plusSeconds(tokenInfo.getExpires_in()));
                            if(accountMapByUserId.containsKey(schwabUserId)) {
                                accountMapByUserId.replace(schwabUserId, schwabAccount);
                            } else {
                                accountMapByUserId.put(schwabUserId, schwabAccount);
                            }
                            if(tokenHandler != null) {
                                tokenHandler.onRefreshTokenChange(schwabAccount);
                            }
                            return Mono.just(new RedirectView(callback));
                        } else {
                            return Mono.error(new RefreshTokenException("Unable to retrieve refresh token", null));
                        }
                    })
                    .onErrorResume(e -> {
                        if(e instanceof WebClientResponseException wcre) {
                            String errorBody = "Unable to retrieve token: " + wcre.getResponseBodyAsString()
                                    .replaceAll("\n", "")
                                    .replaceAll(" ", "")
                                    .replaceAll("\\{", "{ ")
                                    .replaceAll("}", " }")
                                    .replaceAll(":", " : ")
                                    .replaceAll(",", ", ");
                            log.error(errorBody);
                            return Mono.error(new ResponseStatusException(wcre.getStatusCode(), errorBody, wcre));
                        } else {
                            log.error(this.exceptionToString((Exception) e));
                            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString((Exception) e), e));
                        }
                    });
            } else {
                return Mono.empty();
            }
    }

    /**
     * end point for calling the Schwab authentication end point.
     * @param attributes {@link RedirectAttributes}
     * @param schwabUserId {@literal @}RequestParam String
     * @param callback {@literal @}RequestParam String
     * @return {@link RedirectView}
     */
    @GetMapping("/oauth2/schwab/authorization")
    public RedirectView authorize(RedirectAttributes attributes,
                                  @RequestParam String schwabUserId,
                                  @RequestParam String callback) {

        if(this.isInitialized()) {
            try {
                UUID uuid = UUID.randomUUID();
                UuidMapInfo uuidMapInfo = new UuidMapInfo();
                uuidMapInfo.setUserId(schwabUserId);
                uuidMapInfo.setCallback(callback);
                uuids.put(uuid, uuidMapInfo);
                URI uri = UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host(schwabTargetUrl)
                        .pathSegment(schwabApiVersion, schwabAuthorizationUri)
                        .build()
                        .toUri();
                attributes.addAttribute("response_type", "code");
                attributes.addAttribute("redirect_uri", schwabRedirectUri);
                attributes.addAttribute("client_id", schwabClientId);
                attributes.addAttribute("scope", schwabAuthorizationScope);
                attributes.addAttribute("state", uuid.toString());
                return new RedirectView(uri.toString());
            } catch (Exception e) {
                log.error(this.exceptionToString(e));
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString(e), e);
            }
        } else {
            String errorMsg = "Initialization Error: Unable to get Refresh Token before service initialization";
            log.error(errorMsg);
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED, errorMsg);
        }
    }

    @Getter
    @Setter
    private static class UuidMapInfo {
        private String userId;
        private String callback;
    }

    @Getter
    @ToString
    private static class AuthorizationTokenInfo {
        private Long expires_in;
        private String token_type;
        private String scope;
        private String refresh_token;
        private String access_token;
        private String id_token;
    }

    private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}