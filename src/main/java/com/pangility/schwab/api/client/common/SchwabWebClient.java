package com.pangility.schwab.api.client.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pangility.schwab.api.client.common.deserializers.BigDecimalNanDeserializer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * A wrapper for the WebClient interface to avoid Spring bean collisions when the client
 * {@literal @}Autowire a WebClient bean. This class wraps the WebClient along with
 * any settings and deserializers that it needs to interact with the Schwab API.
 */
@Slf4j
@Component
@Conditional(OnApiEnabledCondition.class)
public class SchwabWebClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;

    /**
     * get a web client initialized with defaults for the Schwab API
     * @return {@link WebClient}
     */
    public WebClient getSchwabWebClient() {
        URI schwabUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(schwabTargetUrl)
                .build()
                .toUri();

        final int size = 16 * 1024 * 1024;
        final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(BigDecimal.class, new BigDecimalNanDeserializer()));

        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> {
                    codecs.defaultCodecs().maxInMemorySize(size);
                    codecs.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                })
                .build();

        return WebClient.builder()
                .filter((request, next) -> {
                    logRequest(request);
                    return next
                            .exchange(interceptBody(request))
                            .doOnNext(this::logResponse)
                            .map(this::interceptBody);
                })
                .baseUrl(schwabUri.toString())
                .exchangeStrategies(strategies)
                .build();
    }

    private ClientRequest interceptBody(ClientRequest request) {
        return ClientRequest.from(request)
                .body((outputMessage, context) -> request.body().insert(new ClientHttpRequestDecorator(outputMessage) {
                    @Override public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                        return super.writeWith(Mono.from(body)
                                .doOnNext(dataBuffer -> logRequestBody(dataBuffer)));
                    }
                }, context))
                .build();
    }

    private ClientResponse interceptBody(ClientResponse response) {
        return response.mutate()
                .body(data -> data.doOnNext(this::logResponseBody))
                .build();
    }

    private void logRequest(ClientRequest request) {
        log.debug("DOWNSTREAM REQUEST: METHOD {}, URI: {}, HEADERS: {}", request.method(), request.url(), request.headers());
    }

    private void logRequestBody(DataBuffer dataBuffer) {
        log.debug("DOWNSTREAM REQUEST: BODY: {}", dataBuffer.toString(StandardCharsets.UTF_8));
    }

    private void logResponse(ClientResponse response) {
        log.debug("DOWNSTREAM RESPONSE: STATUS: {}, HEADERS: {}", response.statusCode().value(), response.headers().asHttpHeaders());
    }

    private void logResponseBody(DataBuffer dataBuffer) {
        log.debug("DOWNSTREAM RESPONSE: BODY: {}", dataBuffer.toString(StandardCharsets.UTF_8));
    }
}
