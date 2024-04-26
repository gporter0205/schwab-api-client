package com.schwabapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schwabapi.controllers.SchwabApiClient;
import com.schwabapi.controllers.SchwabOauth2Controller;
import com.schwabapi.deserializers.BigDecimalNanDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Configuration
@ComponentScan(basePackageClasses = {SchwabApiClient.class, SchwabOauth2Controller.class})
@PropertySource(value = "classpath:schwabapiclient.properties")
public class SchwabApiClientConfig {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;

    @Bean
    public WebClient schwabWebClient() {
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