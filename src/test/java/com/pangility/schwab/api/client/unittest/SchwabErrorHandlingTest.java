package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import com.pangility.schwab.api.client.marketdata.model.instruments.Instrument;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.oauth2.RefreshTokenException;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
    public void expiredRefreshTokenTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(LocalDateTime.now().plusMinutes(10));
        schwabAccount.setAccessToken("12345678".repeat(8));
        schwabAccount.setAccessExpiration(LocalDateTime.now().plusMinutes(10));
        schwabMarketDataApiClient.init(schwabAccount, testTokenHandler);

        try {
            InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                    .withSymbol("TSLA")
                    .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                    .build();
            Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(instrumentsRequest);
            StepVerifier
                    .create(instrumentsResponse)
                    .expectError(RefreshTokenException.class)
                    .verify();
        } catch(RefreshTokenException rte) {
            assertThat(rte).isNotNull();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void invalidRefreshTokenTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken("12345678".repeat(12));
        schwabAccount.setRefreshExpiration(LocalDateTime.now().plusDays(1));
        schwabAccount.setAccessToken("12345678".repeat(8));
        schwabAccount.setAccessExpiration(LocalDateTime.now().plusMinutes(10));
        schwabMarketDataApiClient.init(schwabAccount, testTokenHandler);

        InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("TSLA")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST))
                .verify();
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
        Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        !response.getSymbol().isEmpty() &&
                        response.getSymbol().equalsIgnoreCase("TSLA"))
                .verifyComplete();
        assertThat(schwabAccount.getAccessToken()).isNotEqualTo("12345678".repeat(8));
        assertThat(schwabAccount.getAccessExpiration()).isAfter(LocalDateTime.now().plusMinutes(29).plusSeconds(58));
    }

    public static class TestTokenHandler implements SchwabTokenHandler {

        @Override
        public void onAccessTokenChange(@NonNull SchwabAccount schwabAccount) {
            System.out.println("Test Access Token Change");
        }

        @Override
        public void onRefreshTokenChange(@NonNull SchwabAccount schwabAccount) {
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
