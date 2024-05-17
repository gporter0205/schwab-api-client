package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for the client Api classes to extend.  Contains the helper
 * methods for calling the Schwab API.
 */
@Slf4j
public class SchwabBaseApiClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    /**
     * The version of the Schwab API
     */
    @Value("${schwab-api.apiVersion}")
    protected String schwabApiVersion;

    @Autowired
    private SchwabOauth2Controller schwabOauth2Controller;
    @Autowired
    private SchwabWebClient schwabWebClient;

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull SchwabAccount schwabAccount) {
        this.init(Collections.singletonList(schwabAccount), null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull SchwabAccount schwabAccount,
                     SchwabTokenHandler tokenHandler) {
        this.init(Collections.singletonList(schwabAccount), tokenHandler);
    }

    /**
     * Initialize the client controller
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull List<SchwabAccount> schwabAccounts) {
        this.init(schwabAccounts, null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
        schwabOauth2Controller.init(schwabAccounts, tokenHandler);
    }

    /**
     * determine if the controller has been initialized
     * @return Boolean
     */
    public Boolean isInitialized() {
        return schwabOauth2Controller.isInitialized();
    }

    /**
     * Call a Schwab Api using the get http method and return the results as a List collection
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param bodyTypeReference a {@link ParameterizedTypeReference} defining the return type of the method
     * @return {@link List}{@literal <}T{@literal >}
     * @param <T> the return type of the method
     */
    protected <T> List<T> callGetApiAsList(@NotNull String schwabUserId,
                                           @NotNull UriComponentsBuilder uriComponentsBuilder,
                                           @NotNull ParameterizedTypeReference<List<T>> bodyTypeReference) {
        List<T> ret = null;

        ResponseEntity<List<T>> retMono = this.callAPI(schwabUserId, HttpMethod.GET, uriComponentsBuilder)
                .toEntity(bodyTypeReference)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    /**
     * Call a Schwab Api using the get http method and return the results in a map object
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param bodyTypeReference a {@link ParameterizedTypeReference} defining the return type of the method
     * @return {@link Map}{@literal <}String, T{@literal >}
     * @param <T> the return type of the method
     */
    protected <T> Map<String, T> callGetAPIAsMap(@NotNull String schwabUserId,
                                                 @NotNull UriComponentsBuilder uriComponentsBuilder,
                                                 @NotNull ParameterizedTypeReference<Map<String, T>> bodyTypeReference) {
        Map<String, T> ret = null;

        ResponseEntity<Map<String, T>> retMono = this.callAPI(schwabUserId, HttpMethod.GET, uriComponentsBuilder)
                .toEntity(bodyTypeReference)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    /**
     * Call a Schwab Api using the get http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> T callGetAPI(@NotNull String schwabUserId,
                               @NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz) {

        T ret = null;

        ResponseEntity<T> retMono = this.callAPI(schwabUserId, HttpMethod.GET, uriComponentsBuilder)
                .toEntity(clazz)
                .block();
        if (retMono != null && retMono.hasBody()) {
            ret = retMono.getBody();
        }
        return ret;
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callPostAPI(@NotNull String schwabUserId,
                               @NotNull UriComponentsBuilder uriComponentsBuilder,
                               Object body) {
        this.callAPI(schwabUserId, HttpMethod.POST, uriComponentsBuilder, Object.class, body);
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callPostAPI(@NotNull String schwabUserId,
                               @NotNull UriComponentsBuilder uriComponentsBuilder) {
        this.callAPI(schwabUserId, HttpMethod.POST, uriComponentsBuilder, Object.class, null);
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @param body the body of the request
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> T callPostAPI(@NotNull String schwabUserId,
                                @NotNull UriComponentsBuilder uriComponentsBuilder,
                                @NotNull Class<T> clazz,
                                Object body) {
        return this.callAPI(schwabUserId, HttpMethod.POST, uriComponentsBuilder, clazz, body);
    }

    /**
     * Call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callPutAPI(@NotNull String schwabUserId,
                              @NotNull UriComponentsBuilder uriComponentsBuilder) {
        this.callAPI(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, Object.class, null);
    }

    /**
     * Call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callPutAPI(@NotNull String schwabUserId,
                              @NotNull UriComponentsBuilder uriComponentsBuilder,
                              Object body) {
        this.callAPI(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, Object.class, body);
    }

    /**
     * Call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @param body the body of the request
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> T callPutAPI(@NotNull String schwabUserId,
                               @NotNull UriComponentsBuilder uriComponentsBuilder,
                               @NotNull Class<T> clazz,
                               Object body) {
        return this.callAPI(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, clazz, body);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callDeleteAPI(@NotNull String schwabUserId,
                                 @NotNull UriComponentsBuilder uriComponentsBuilder) {
        this.callAPI(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, Object.class, null);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callDeleteAPI(@NotNull String schwabUserId,
                                 @NotNull UriComponentsBuilder uriComponentsBuilder,
                                 Object body) {
        this.callAPI(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, Object.class, body);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @param body the body of the request
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> T callDeleteAPI(@NotNull String schwabUserId,
                                  @NotNull UriComponentsBuilder uriComponentsBuilder,
                                  @NotNull Class<T> clazz,
                                  Object body) {
        return this.callAPI(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, clazz, body);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param httpMethod the http method to use when calling the API
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @param body the body of the request
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> T callAPI(@NotNull String schwabUserId,
                            @NotNull HttpMethod httpMethod,
                            @NotNull UriComponentsBuilder uriComponentsBuilder,
                            @NotNull Class<T> clazz,
                            Object body) {
        T ret = null;
        WebClient.ResponseSpec responseSpec = this.callAPI(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body);
        ResponseEntity<T> retEntity = responseSpec.toEntity(clazz).block();
        if(retEntity != null && retEntity.hasBody()) {
            ret = retEntity.getBody();
        }
        return ret;
    }

    private WebClient.ResponseSpec callAPI(@NotNull String schwabUserId,
                                           @NotNull HttpMethod httpMethod,
                                           @NotNull UriComponentsBuilder uriComponentsBuilder) {
        return this.callAPI(schwabUserId, httpMethod, uriComponentsBuilder, null);
    }

    private WebClient.ResponseSpec callAPI(@NotNull String schwabUserId,
                                           @NotNull HttpMethod httpMethod,
                                           @NotNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body) {
        WebClient.ResponseSpec ret = null;

        //Validate refresh token
        schwabOauth2Controller.validateRefreshToken(schwabUserId);

        String accessToken = schwabOauth2Controller.getAccessToken(schwabUserId);
        if(accessToken != null) {
            URI uri = uriComponentsBuilder
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .build()
                    .toUri();
            WebClient.RequestBodySpec bodySpec = schwabWebClient.getSchwabWebClient()
                    .method(httpMethod)
                    .uri(uri)
                    .headers(h -> h.setBearerAuth(accessToken));
            if(body != null) {
                bodySpec.body(BodyInserters.fromValue(body));
            }
            ret = bodySpec.retrieve();

        }
        return ret;
    }
}