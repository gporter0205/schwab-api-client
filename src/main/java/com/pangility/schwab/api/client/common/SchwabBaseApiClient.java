package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.common.deserializers.ApiTooManyRequestsException;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.Duration;
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
    public void init(@NonNull SchwabAccount schwabAccount) {
        this.init(Collections.singletonList(schwabAccount), null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NonNull SchwabAccount schwabAccount,
                     SchwabTokenHandler tokenHandler) {
        this.init(Collections.singletonList(schwabAccount), tokenHandler);
    }

    /**
     * Initialize the client controller
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    @SuppressWarnings("unused")
    public void init(@NonNull List<SchwabAccount> schwabAccounts) {
        this.init(schwabAccounts, null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NonNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
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
    @Deprecated
    protected <T> List<T> callGetApiAsList(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           @NonNull ParameterizedTypeReference<List<T>> bodyTypeReference) {
        List<T> ret = null;

        ResponseEntity<List<T>> retMono = this.callApi(schwabUserId, HttpMethod.GET, uriComponentsBuilder)
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
    @Deprecated
    protected <T> Map<String, T> callGetApiAsMap(@NonNull String schwabUserId,
                                                 @NonNull UriComponentsBuilder uriComponentsBuilder,
                                                 @NonNull ParameterizedTypeReference<Map<String, T>> bodyTypeReference) {
        Map<String, T> ret = null;

        ResponseEntity<Map<String, T>> retMono = this.callApi(schwabUserId, HttpMethod.GET, uriComponentsBuilder)
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
    protected <T> Mono<T> callGetApiToMono(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           @NonNull Class<T> clazz) {

        return this.callApiToMono(schwabUserId, HttpMethod.GET, uriComponentsBuilder, null, clazz);
    }

    /**
     * Call a Schwab Api using the get http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callGetApiToMono(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           @NonNull ParameterizedTypeReference<T> bodyTypeReference) {

        return this.callApiToMono(schwabUserId, HttpMethod.GET, uriComponentsBuilder, null, bodyTypeReference);
    }

    /**
     * Call a Schwab Api using the get http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callGetApiToFlux(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           @NonNull Class<T> clazz) {

        return this.callApiToFlux(schwabUserId, HttpMethod.GET, uriComponentsBuilder, null, clazz);
    }

    /**
     * Call a Schwab Api using the get http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callGetApiToFlux(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           @NonNull ParameterizedTypeReference<T> bodyTypeReference) {

        return this.callApiToFlux(schwabUserId, HttpMethod.GET, uriComponentsBuilder, null, bodyTypeReference);
    }

    /**
     * Call a Schwab Api using the get http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    @Deprecated
    protected <T> T callGetApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder,
                               @NonNull Class<T> clazz) {

        return this.callApi(schwabUserId, HttpMethod.GET, uriComponentsBuilder, clazz, null);
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callPostApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder) {
        this.callApiToMono(schwabUserId, HttpMethod.POST, uriComponentsBuilder, null, Object.class);
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callPostApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder,
                               Object body) {
        this.callApiToMono(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body, Object.class);
    }

    /**
     * Call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    @Deprecated
    protected <T> T callPostApi(@NonNull String schwabUserId,
                                @NonNull UriComponentsBuilder uriComponentsBuilder,
                                Object body,
                                @NonNull Class<T> clazz) {
        return this.callApi(schwabUserId, HttpMethod.POST, uriComponentsBuilder, clazz, body);
    }

    /**
     * reactively call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callPostApiToMono(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            @NonNull Class<T> clazz) {
        return this.callApiToMono(schwabUserId, HttpMethod.POST, uriComponentsBuilder, null, clazz);
    }

    /**
     * reactively call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callPostApiToMono(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull Class<T> clazz) {
        return this.callApiToMono(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callPostApiToMono(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToMono(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body, bodyTypeReference);
    }

    /**
     * reactively call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callPostApiToFlux(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull Class<T> clazz) {
        return this.callApiToFlux(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the post http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callPostApiToFlux(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToFlux(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body, bodyTypeReference);
    }

    /**
     * Call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callPutApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder) {
        this.callApiToMono(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, null, Object.class);
    }

    /**
     * Call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callPutApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder,
                               Object body) {
        this.callApiToMono(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, body, Object.class);
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
    @Deprecated
    protected <T> T callPutApi(@NonNull String schwabUserId,
                               @NonNull UriComponentsBuilder uriComponentsBuilder,
                               @NonNull Class<T> clazz,
                               Object body) {
        return this.callApi(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, clazz, body);
    }

    /**
     * reactively call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callPutApiToMono(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull Class<T> clazz) {
        return this.callApiToMono(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callPutApiToMono(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToMono(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, body, bodyTypeReference);
    }

    /**
     * reactively call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callPutApiToFlux(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull Class<T> clazz) {
        return this.callApiToFlux(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the put http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callPutApiToFlux(@NonNull String schwabUserId,
                                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                                            Object body,
                                            @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToFlux(schwabUserId, HttpMethod.PUT, uriComponentsBuilder, body, bodyTypeReference);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     */
    protected void callDeleteApi(@NonNull String schwabUserId,
                                 @NonNull UriComponentsBuilder uriComponentsBuilder) {
        this.callApiToMono(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, null, Object.class);
    }

    /**
     * Call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     */
    protected void callDeleteApi(@NonNull String schwabUserId,
                                 @NonNull UriComponentsBuilder uriComponentsBuilder,
                                 Object body) {
        this.callApiToMono(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, body, Object.class);
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
    @Deprecated
    protected <T> T callDeleteApi(@NonNull String schwabUserId,
                                  @NonNull UriComponentsBuilder uriComponentsBuilder,
                                  @NonNull Class<T> clazz,
                                  Object body) {
        return this.callApi(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, clazz, body);
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
    @Deprecated
    protected <T> T callApi(@NonNull String schwabUserId,
                            @NonNull HttpMethod httpMethod,
                            @NonNull UriComponentsBuilder uriComponentsBuilder,
                            @NonNull Class<T> clazz,
                            Object body) {
        T ret = null;
        WebClient.ResponseSpec responseSpec = this.callApi(schwabUserId, HttpMethod.POST, uriComponentsBuilder, body);
        ResponseEntity<T> retEntity = responseSpec.toEntity(clazz)
                .block();
        if(retEntity != null && retEntity.hasBody()) {
            ret = retEntity.getBody();
        }
        return ret;
    }

    /**
     * reactively call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callDeleteApiToMono(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body,
                                           @NonNull Class<T> clazz) {
        return this.callApiToMono(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Mono<T> callDeleteApiToMono(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body,
                                           @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToMono(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, body, bodyTypeReference);
    }

    /**
     * reactively call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param clazz a Class{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callDeleteApiToFlux(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body,
                                           @NonNull Class<T> clazz) {
        return this.callApiToFlux(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, body, clazz);
    }

    /**
     * reactively call a Schwab Api using the delete http method
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param uriComponentsBuilder the path and query params of the API
     * @param body the body of the request
     * @param bodyTypeReference a {@link ParameterizedTypeReference}{@literal <}T{@literal >} object defining the return type of the method
     * @return T
     * @param <T> the return type of the method
     */
    protected <T> Flux<T> callDeleteApiToFlux(@NonNull String schwabUserId,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body,
                                           @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToFlux(schwabUserId, HttpMethod.DELETE, uriComponentsBuilder, body, bodyTypeReference);
    }

    @Deprecated
    private WebClient.ResponseSpec callApi(@NonNull String schwabUserId,
                                           @NonNull HttpMethod httpMethod,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder) {
        return this.callApi(schwabUserId, httpMethod, uriComponentsBuilder, null);
    }

    @Deprecated
    private WebClient.ResponseSpec callApi(@NonNull String schwabUserId,
                                           @NonNull HttpMethod httpMethod,
                                           @NonNull UriComponentsBuilder uriComponentsBuilder,
                                           Object body) {

        return schwabOauth2Controller.getAccessToken(schwabUserId).map(tokenInfo -> {
            //Validate refresh token
            schwabOauth2Controller.validateRefreshToken(tokenInfo);

            URI uri = uriComponentsBuilder
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .build()
                    .toUri();
            WebClient.RequestBodySpec bodySpec = schwabWebClient.getSchwabWebClient()
                    .method(httpMethod)
                    .uri(uri)
                    .headers(h -> h.setBearerAuth(tokenInfo.getAccessToken()));
            if (body != null) {
                bodySpec.body(BodyInserters.fromValue(body));
            }

            return bodySpec.retrieve()
                    .onStatus(httpStatus -> httpStatus.isSameCodeAs(HttpStatus.UNAUTHORIZED), error -> Mono.error(new ApiUnauthorizedException()));
        }).block();
    }

    private <T> Mono<T> callApiToMono(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull Class<T> clazz) {

        return this.callApiToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, this.classToTypeReference(clazz), false);
    }

    /*private <T> Mono<T> callApiToMono(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull Class<T> clazz,
                                      @NonNull Boolean hasRetried401WithNewAccessToken) {
        return schwabOauth2Controller.getAccessToken(schwabUserId)
                .flatMap(tokenInfo -> {
                    //Validate refresh token
                    schwabOauth2Controller.validateRefreshToken(tokenInfo);

                    URI uri = uriComponentsBuilder
                            .scheme("https")
                            .host(schwabTargetUrl)
                            .build()
                            .toUri();
                    WebClient.RequestBodySpec bodySpec = schwabWebClient.getSchwabWebClient()
                            .method(httpMethod)
                            .uri(uri)
                            .headers(h -> h.setBearerAuth(tokenInfo.getAccessToken()));
                    if (body != null) {
                        bodySpec.body(BodyInserters.fromValue(body));
                    }

                    return bodySpec.exchangeToMono(response -> {
                        Mono<T> mono;
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            mono = response.bodyToMono(clazz);
                        } else if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                            if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                                mono = Mono.error(new ApiUnauthorizedException());
                            } else {
                                mono = response.createException()
                                        .flatMap(Mono::error);
                            }
                        } else {
                            mono = response.createException()
                                    .flatMap(Mono::error);
                        }
                        return mono;
                    });
                })
                .onErrorResume(throwable -> {
                    if(throwable instanceof ApiUnauthorizedException && !hasRetried401WithNewAccessToken) {
                        schwabOauth2Controller.getSchwabAccount(schwabUserId).setAccessToken(null);
                        return this.callApiToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, clazz, true);
                    } else {
                        return Mono.error(throwable);
                    }
                });
    }*/

    private <T> Mono<T> callApiToMono(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, bodyTypeReference, false);
    }

    private <T> Mono<T> callApiToMono(@NonNull String schwabUserId,
                                        @NonNull HttpMethod httpMethod,
                                        @NonNull UriComponentsBuilder uriComponentsBuilder,
                                        Object body,
                                        @NonNull ParameterizedTypeReference<T> bodyTypeReference,
                                        @NonNull Boolean hasRetried401WithNewAccessToken) {

        return schwabOauth2Controller.getAccessToken(schwabUserId)
                .flatMap(tokenInfo -> this.callApiPreProcessToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, tokenInfo)
                        .flatMap(requestBodySpec -> requestBodySpec.exchangeToMono(response -> {
                            Mono<T> mono;
                            if (response.statusCode().is2xxSuccessful()) {
                                mono = response.bodyToMono(bodyTypeReference);
                            } else if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                                if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                                    mono = Mono.error(new ApiUnauthorizedException());
                                } else if (response.statusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                                    mono = Mono.error(new ApiTooManyRequestsException());
                                } else {
                                    mono = response.createException()
                                            .flatMap(Mono::error);
                                }
                            } else {
                                mono = response.createException()
                                        .flatMap(Mono::error);
                            }
                            return mono;

                }))
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(1)).jitter(0.25)
                        .filter(throwable -> throwable instanceof ApiTooManyRequestsException)
                )
                .onErrorResume(throwable -> {
                    if(throwable instanceof ApiUnauthorizedException && !hasRetried401WithNewAccessToken) {
                        // Invalidate the access token and try again
                        schwabOauth2Controller.getSchwabAccount(schwabUserId).setAccessToken(null);
                        return this.callApiToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, bodyTypeReference, true);
                    } else {
                        return Mono.error(throwable);
                    }
                }));
    }

    private <T> Flux<T> callApiToFlux(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull Class<T> clazz) {
        return this.callApiToFlux(schwabUserId, httpMethod, uriComponentsBuilder, body, this.classToTypeReference(clazz));
    }

    private <T> Flux<T> callApiToFlux(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull ParameterizedTypeReference<T> bodyTypeReference) {
        return this.callApiToFlux(schwabUserId, httpMethod, uriComponentsBuilder, body, bodyTypeReference, false);
    }

    private <T> Flux<T> callApiToFlux(@NonNull String schwabUserId,
                                      @NonNull HttpMethod httpMethod,
                                      @NonNull UriComponentsBuilder uriComponentsBuilder,
                                      Object body,
                                      @NonNull ParameterizedTypeReference<T> bodyTypeReference,
                                      @NonNull Boolean hasRetried401WithNewAccessToken) {
        return schwabOauth2Controller.getAccessToken(schwabUserId)
                .flatMapMany(tokenInfo -> this.callApiPreProcessToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, tokenInfo)
                        .flatMapMany(requestBodySpec -> requestBodySpec.exchangeToFlux(response -> {
                            Flux<T> flux;
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                flux = response.bodyToFlux(bodyTypeReference);
                            } else if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                                if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                                    flux = Flux.error(new ApiUnauthorizedException());
                                } else if (response.statusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                                    flux = Flux.error(new ApiTooManyRequestsException());
                                } else {
                                    flux = response.createException()
                                            .flatMapMany(Flux::error);
                                }
                            } else {
                                flux = response.createException()
                                        .flatMapMany(Flux::error);
                            }
                            return flux;
                }))
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(1)).jitter(0.25)
                        .filter(throwable -> throwable instanceof ApiTooManyRequestsException)
                )
                .onErrorResume(throwable -> {
                    if(throwable instanceof ApiUnauthorizedException && !hasRetried401WithNewAccessToken) {
                        // Invalidate the access token and try again
                        schwabOauth2Controller.getSchwabAccount(schwabUserId).setAccessToken(null);
                        return this.callApiToFlux(schwabUserId, httpMethod, uriComponentsBuilder, body, bodyTypeReference, true);
                    } else {
                        return Flux.error(throwable);
                    }
                }));
    }

    private WebClient.RequestBodySpec callApiPreProcess(@NonNull String schwabUserId,
                                                        @NonNull HttpMethod httpMethod,
                                                        @NonNull UriComponentsBuilder uriComponentsBuilder,
                                                        Object body,
                                                        @NonNull SchwabAccount tokenInfo) {
        return this.callApiPreProcessToMono(schwabUserId, httpMethod, uriComponentsBuilder, body, tokenInfo).block();
    }

    private Mono<WebClient.RequestBodySpec> callApiPreProcessToMono(@NonNull String schwabUserId,
                                                                    @NonNull HttpMethod httpMethod,
                                                                    @NonNull UriComponentsBuilder uriComponentsBuilder,
                                                                    Object body,
                                                                    @NonNull SchwabAccount tokenInfo) {
        return schwabOauth2Controller.validateRefreshTokenToMono(tokenInfo)
                .flatMap(schwabAccount -> {
                    URI uri = uriComponentsBuilder
                            .scheme("https")
                            .host(schwabTargetUrl)
                            .build()
                            .toUri();
                    WebClient.RequestBodySpec bodySpec = schwabWebClient.getSchwabWebClient()
                            .method(httpMethod)
                            .uri(uri)
                            .headers(h -> h.setBearerAuth(tokenInfo.getAccessToken()));
                    if (body != null) {
                        bodySpec.body(BodyInserters.fromValue(body));
                    }
                    return Mono.just(bodySpec);
                });
    }

    private <T> ParameterizedTypeReference<T> classToTypeReference(Class<T> clazz) {
        return new ParameterizedTypeReference<T>() {
            @Override
            public @NonNull Type getType() {
                return clazz;
            }
        };

    }
}