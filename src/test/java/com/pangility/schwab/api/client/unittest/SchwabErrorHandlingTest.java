package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsResponse;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableSchwabMarketDataApi
@ActiveProfiles("unittest")
public class SchwabErrorHandlingTest {

    @Value("${schwab-api.oauth2.userId}")
    private String schwabUserId;
    @Value("${schwab-api.oauth2.refreshToken}")
    private String refreshToken;
    @Value("${schwab-api.oauth2.refreshExpiration}")
    private String refreshExpiration;
    private final SchwabAccount schwabAccount = new SchwabAccount();

    @Autowired
    private SchwabMarketDataApiClient schwabMarketDataApiClient;
    @Autowired
    private TestTokenHandler testTokenHandler;

    @BeforeEach
    public void setUpEachTest() {
    }

    @Test
    public void UnauthorizedAccessTokenTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(LocalDateTime.parse(refreshExpiration, DateTimeFormatter.ISO_DATE_TIME));
        schwabAccount.setAccessToken("12345678".repeat(8));
        schwabAccount.setAccessExpiration(LocalDateTime.now().plusMinutes(10));
        schwabMarketDataApiClient.init(schwabAccount, testTokenHandler);

        assertThat(schwabAccount.getAccessToken()).isEqualTo("12345678".repeat(8));

        InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("TSLA")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToMono(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectNextMatches(response -> response.getInstruments() != null &&
                        response.getInstruments().size() == 1 &&
                        response.getInstruments().get(0).getSymbol().equalsIgnoreCase("TSLA"))
                .verifyComplete();
        assertThat(schwabAccount.getAccessToken()).isNotEqualTo("12345678".repeat(8));
        assertThat(schwabAccount.getAccessExpiration()).isAfter(LocalDateTime.now().plusMinutes(29).plusSeconds(58));
    }

    public static class TestTokenHandler implements SchwabTokenHandler {

        @Override
        public void onAccessTokenChange(@NotNull SchwabAccount schwabAccount) {
            System.out.println("Test Access Token Change");
        }

        @Override
        public void onRefreshTokenChange(@NotNull SchwabAccount schwabAccount) {
            System.out.println("Test Refresh Token Change");
        }
    }

    @SpringBootConfiguration
    public static class TestConfig {
        @Bean
        public TestTokenHandler testTokenHandler() {
            return new TestTokenHandler();
        }
    }
}
