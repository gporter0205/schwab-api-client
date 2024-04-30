package com.pangility.schwab.api.client.oauth2;

import com.pangility.schwab.api.client.common.InvalidRefreshTokenException;
import com.pangility.schwab.api.client.common.OnApiEnabledCondition;
import com.pangility.schwab.api.client.common.SchwabWebClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private SchwabWebClient schwabWebClient;

    private final HashMap<String, SchwabAccount> accountMapByUserId = new HashMap<>();

    private final HashMap<UUID, UuidMapInfo> uuids = new HashMap<>();

    private SchwabTokenHandler tokenHandler = null;

    public void init(@NotNull List<SchwabAccount> schwabAccounts) {
        this.init(schwabAccounts, null);
    }

    public void init(@NotNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
        for(SchwabAccount schwabAccount : schwabAccounts) {
            accountMapByUserId.put(schwabAccount.getUserId(), schwabAccount);
        }
        this.tokenHandler = tokenHandler;
    }

    public Boolean isInitialized() {
        return this.accountMapByUserId.size() > 0;
    }

    public void validateRefreshToken(@NotNull String schwabUserId) throws InvalidRefreshTokenException {
        SchwabAccount schwabAccount = accountMapByUserId.get(schwabUserId);
        if(schwabAccount.getRefreshToken() == null) {
            throw new InvalidRefreshTokenException("Missing Refresh Token", schwabAccount);
        } else if(LocalDateTime.now().plusMinutes(1).isAfter(schwabAccount.getRefreshExpiration())) {
            throw new InvalidRefreshTokenException("Expired Refresh Token", schwabAccount);
        }
    }

    public SchwabAccount getSchwabAccount(@NotNull String schwabUserId) {
        return accountMapByUserId.get(schwabUserId);
    }

    public String getAccessToken(@NotNull String schwabUserId) {
        String accessToken = null;
        SchwabAccount schwabAccount = accountMapByUserId.get(schwabUserId);
        if(schwabAccount != null) {
            accessToken = schwabAccount.getAccessToken();
            LocalDateTime accessExpiration = schwabAccount.getAccessExpiration();
            if(accessToken == null || LocalDateTime.now().plusMinutes(1).isAfter(accessExpiration)) {
                accessToken = this.refreshAccessToken(schwabAccount);
            }
        }
        return accessToken;
    }

    public String refreshAccessToken(@NotNull SchwabAccount schwabAccount) {
        String accessToken = null;
        try {
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

            ResponseEntity<AuthorizationTokenInfo> response = schwabWebClient.getSchwabWebClient().post()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, tokenAuthorizationHeader)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData(bodyValues))
                    .retrieve()
                    .toEntity(AuthorizationTokenInfo.class)
                    .block();

            if (response != null) {
                if (response.getStatusCode() == HttpStatus.OK) {
                    AuthorizationTokenInfo responseTokenInfo = response.getBody();
                    if(responseTokenInfo != null) {
                        accessToken = responseTokenInfo.getAccess_token();
                        schwabAccount.setAccessToken(responseTokenInfo.getAccess_token());
                        schwabAccount.setAccessExpiration(LocalDateTime.now().plusSeconds(responseTokenInfo.getExpires_in()));
                        accountMapByUserId.put(schwabAccount.getUserId(), schwabAccount);
                        if(tokenHandler != null) {
                            tokenHandler.onAccessTokenChange(schwabAccount);
                        }
                    }
                }
            }
        } catch(WebClientResponseException wcre) {
            String errorBody = "Unable to retrieve token: " + wcre.getResponseBodyAsString()
                    .replaceAll("\n", "")
                    .replaceAll(" ", "")
                    .replaceAll("\\{", "{ ")
                    .replaceAll("}", " }")
                    .replaceAll(":", " : ")
                    .replaceAll(",", ", ");
            log.error(errorBody);
            throw new ResponseStatusException(
                    wcre.getStatusCode(), errorBody, wcre);
        } catch(Exception e) {
            log.error(this.exceptionToString(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString(e), e);
        }
        return accessToken;
    }

    @GetMapping(value = {"/login/oauth2/code/schwab", "/oauth2/schwab/code"})
    public RedirectView processCode(@RequestParam String code,
                            @RequestParam String state) {
        String tokenAuthorizationHeader = "Basic " + new String(Base64.getMimeEncoder().encode((schwabClientId + ":" + schwabClientSecret).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        RedirectView redirectView = null;
        try {
            UUID paramUuid = UUID.fromString(state);
            if (uuids.containsKey(paramUuid)) {
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

                ResponseEntity<AuthorizationTokenInfo> response = schwabWebClient.getSchwabWebClient().post()
                        .uri(uri)
                        .header(HttpHeaders.AUTHORIZATION, tokenAuthorizationHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(bodyValues))
                        .retrieve()
                        .toEntity(AuthorizationTokenInfo.class)
                        .block();

                if (response != null) {
                    if (response.getStatusCode() == HttpStatus.OK) {
                        UuidMapInfo uuidMapInfo = uuids.get(paramUuid);
                        String schwabUserId = uuidMapInfo.getUserId();
                        String callback = uuidMapInfo.getCallback();
                        SchwabAccount schwabAccount;
                        if(!accountMapByUserId.containsKey(schwabUserId)) {
                            schwabAccount = new SchwabAccount();
                            schwabAccount.setUserId(schwabUserId);
                        } else {
                            schwabAccount = accountMapByUserId.get(schwabUserId);
                        }
                        if(schwabAccount != null) {
                            AuthorizationTokenInfo responseTokenInfo = response.getBody();
                            if(responseTokenInfo != null) {
                                schwabAccount.setRefreshToken(responseTokenInfo.getRefresh_token());
                                schwabAccount.setRefreshExpiration(LocalDateTime.now().plusDays(7));
                                schwabAccount.setAccessToken(responseTokenInfo.getAccess_token());
                                schwabAccount.setAccessExpiration(LocalDateTime.now().plusSeconds(responseTokenInfo.getExpires_in()));
                                accountMapByUserId.put(schwabUserId, schwabAccount);
                                if(tokenHandler != null) {
                                    tokenHandler.onRefreshTokenChange(schwabAccount);
                                }
                            }
                        }
                        redirectView = new RedirectView(callback);
                    } else {
                        throw new ResponseStatusException(
                                response.getStatusCode(), Objects.requireNonNull(response.getBody()).toString());
                    }
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get response from token endpoint");
                }
            }
        } catch(WebClientResponseException wcre) {
            String errorBody = "Unable to retrieve token: " + wcre.getResponseBodyAsString()
                    .replaceAll("\n", "")
                    .replaceAll(" ", "")
                    .replaceAll("\\{", "{ ")
                    .replaceAll("}", " }")
                    .replaceAll(":", " : ")
                    .replaceAll(",", ", ");
            log.error(errorBody);
            throw new ResponseStatusException(
                    wcre.getStatusCode(), errorBody, wcre);
        } catch(ResponseStatusException rse) {
            throw rse;
        } catch(Exception e) {
            log.error(this.exceptionToString(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString(e), e);
        }
        return redirectView;
    }

    @GetMapping("/oauth2/schwab/authorization")
    public RedirectView authorize(RedirectAttributes attributes,
                                  @RequestParam String schwabUserId,
                                  @RequestParam String callback) {

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
        } catch(Exception e) {
            log.error(this.exceptionToString(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, this.exceptionToString(e), e);
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