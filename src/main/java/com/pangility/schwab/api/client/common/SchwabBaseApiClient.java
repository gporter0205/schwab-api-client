package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class SchwabBaseApiClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    @Value("${schwab-api.apiVersion}")
    protected String schwabApiVersion;

    @Autowired
    private SchwabOauth2Controller schwabOauth2Controller;
    @Autowired
    private SchwabWebClient schwabWebClient;

    protected String defaultUserId = null;

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull SchwabAccount schwabAccount) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull SchwabAccount schwabAccount, SchwabTokenHandler tokenHandler) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), tokenHandler);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull String defaultUserId, @NotNull List<SchwabAccount> schwabAccounts) {
        this.init(defaultUserId, schwabAccounts, null);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull String defaultUserId, @NotNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
        this.defaultUserId = defaultUserId;
        schwabOauth2Controller.init(schwabAccounts, tokenHandler);
    }

    /**
     * determine if the controller has been initialized
     * @return Boolean
     */
    @SuppressWarnings("unused")
    public Boolean isInitialized() {
        return this.defaultUserId != null && schwabOauth2Controller.isInitialized();
    }

    protected <T> List<T> callGetApiAsList(@NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull ParameterizedTypeReference<List<T>> bodyTypeReference) {
        List<T> ret = null;

        ResponseEntity<List<T>> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().get())
                .toEntity(bodyTypeReference)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    protected <T> Map<String, T> callGetAPIAsMap(@NotNull UriComponentsBuilder uriComponentsBuilder,
                                    @NotNull ParameterizedTypeReference<Map<String, T>> bodyTypeReference) {
        Map<String, T> ret = null;

        ResponseEntity<Map<String, T>> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().get())
                .toEntity(bodyTypeReference)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    protected <T> T callGetAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz) {

        T ret = null;

        ResponseEntity<T> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().get())
                .toEntity(clazz)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    protected <T> T callPostAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz) {

        T ret = null;

        ResponseEntity<T> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().post())
                .toEntity(clazz)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    protected <T> T callPutAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz) {

        T ret = null;

        ResponseEntity<T> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().put())
                .toEntity(clazz)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    protected <T> T callDeleteAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz) {

        T ret = null;

        ResponseEntity<T> retMono = this.callAPI(uriComponentsBuilder, schwabWebClient.getSchwabWebClient().delete())
                .toEntity(clazz)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    private WebClient.ResponseSpec callAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                                           @NotNull WebClient.RequestHeadersUriSpec<?> webClientMethod) {
        WebClient.ResponseSpec ret = null;

        //Validate refresh token
        schwabOauth2Controller.validateRefreshToken(defaultUserId);

        String accessToken = schwabOauth2Controller.getAccessToken(defaultUserId);
        if(accessToken != null) {
            URI uri = uriComponentsBuilder
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .build()
                    .toUri();
            ret = webClientMethod
                    .uri(uri)
                    .headers(h -> h.setBearerAuth(accessToken))
                    .retrieve();
        }
        return ret;
    }
}