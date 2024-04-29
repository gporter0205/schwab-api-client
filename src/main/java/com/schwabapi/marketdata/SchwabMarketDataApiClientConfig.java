package com.schwabapi.marketdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schwabapi.common.deserializers.BigDecimalNanDeserializer;
import com.schwabapi.oauth2.SchwabOauth2Controller;
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
@ComponentScan(basePackageClasses = {SchwabMarketDataApiClient.class, SchwabOauth2Controller.class})
@PropertySource(value = "classpath:schwabapiclient.properties")
public class SchwabMarketDataApiClientConfig {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;

    @Bean
    public WebClient schwabMarketDataWebClient() {
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