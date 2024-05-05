package com.pangility.schwab.api.client.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pangility.schwab.api.client.common.deserializers.BigDecimalNanDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

/**
 * A wrapper for the WebClient interface to avoid Spring bean collisions when the client
 * {@literal @}Autowire a WebClient bean. This class wraps the WebClient along with
 * any settings and deserializers that it needs to interact with the Schwab API.
 */
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
                .baseUrl(schwabUri.toString())
                .exchangeStrategies(strategies)
                .build();
    }
}
