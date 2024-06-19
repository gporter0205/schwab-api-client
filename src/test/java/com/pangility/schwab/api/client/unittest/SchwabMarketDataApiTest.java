package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import com.pangility.schwab.api.client.marketdata.SymbolNotFoundException;
import com.pangility.schwab.api.client.marketdata.model.AssetMainType;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainRequest;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainResponse;
import com.pangility.schwab.api.client.marketdata.model.expirationchain.ExpirationChainResponse;
import com.pangility.schwab.api.client.marketdata.model.instruments.Instrument;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversRequest;
import com.pangility.schwab.api.client.marketdata.model.movers.Screener;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryRequest;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryResponse;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;
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
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@EnableSchwabMarketDataApi
@ActiveProfiles("unittest")
public class SchwabMarketDataApiTest {

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
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(LocalDateTime.parse(refreshExpiration, DateTimeFormatter.ISO_DATE_TIME));
        schwabMarketDataApiClient.init(schwabAccount, testTokenHandler);
    }

    @Test
    public void equityQuoteTest() {
        Mono<QuoteResponse> quoteResponse = schwabMarketDataApiClient.fetchQuoteToMono("TQQQ");
        StepVerifier
                .create(quoteResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase("TQQQ") &&
                        response.getAssetMainType().equals(AssetMainType.EQUITY))
                .verifyComplete();
    }

    @Test
    public void optionQuoteTest() {
        // get next Friday
        LocalDate nextFriday = LocalDate.now().plusDays(3);
        while(!nextFriday.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextFriday = nextFriday.plusDays(1);
        }
        String symbol = "TQQQ  " + nextFriday.format(DateTimeFormatter.ofPattern("yyMMdd")) + "C00051000";

        Mono<QuoteResponse> optionResponse = schwabMarketDataApiClient.fetchQuoteToMono(symbol);
        StepVerifier
                .create(optionResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase(symbol) &&
                        response.getAssetMainType().equals(AssetMainType.OPTION))
                .verifyComplete();
    }

    @Test
    public void equityQuoteNotFoundTest() {
        Mono<QuoteResponse> quoteResponse = schwabMarketDataApiClient.fetchQuoteToMono("XXXXXX");
        StepVerifier
                .create(quoteResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void optionQuoteNotFoundTest() {
        // get next Friday
        LocalDate nextFriday = LocalDate.now().plusDays(3);
        while(!nextFriday.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextFriday = nextFriday.plusDays(1);
        }
        // Not enough spaces padding this symbol to simulate bad symbol.
        String symbol = "SSO  " + nextFriday.format(DateTimeFormatter.ofPattern("yyMMdd")) + "C00071000";

        Mono<QuoteResponse> quoteResponse = schwabMarketDataApiClient.fetchQuoteToMono(symbol);
        StepVerifier
                .create(quoteResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void quotesTest() {
        Mono<Map<String, QuoteResponse>> quoteResponses = schwabMarketDataApiClient.fetchQuotesToMono(Arrays.asList("TQQQ","UPRO"));
        StepVerifier
                .create(quoteResponses)
                .expectNextMatches(responses -> responses.size() == 2 &&
                        responses.get("TQQQ").getSymbol() != null &&
                        responses.get("TQQQ").getSymbol().equalsIgnoreCase("TQQQ") &&
                        responses.get("UPRO").getSymbol() != null &&
                        responses.get("UPRO").getSymbol().equalsIgnoreCase("UPRO"))
                .verifyComplete();
    }

    /*@Test
    public void quotesNotFoundTest() {
        Mono<Map<String, QuoteResponse>> quoteResponses = schwabMarketDataApiClient.fetchQuotesToMono(Arrays.asList("TQQQ","UPRO","XXXXXX"));
        StepVerifier
                .create(quoteResponses)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }*/

    @Test
    public void chainsTest() {
        OptionChainRequest optionChainRequest = OptionChainRequest.builder().withSymbol("TQQQ").build();
        Mono<OptionChainResponse> optionChainResponseMono = schwabMarketDataApiClient.fetchOptionChainToMono(optionChainRequest);
        StepVerifier
                .create(optionChainResponseMono)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase("TQQQ") &&
                        response.getCallExpDateMap() != null &&
                        !response.getCallExpDateMap().isEmpty())
                .verifyComplete();
    }

    // Service returns a 400 - Bad Request instead of a 404 - Not Found
    /*@Test
    public void chainsNotFoundTest() {
        OptionChainRequest optionChainRequest = OptionChainRequest.builder().withSymbol("XXXXXX").build();
        Mono<OptionChainResponse> optionChainResponse = schwabMarketDataApiClient.fetchOptionChainToMono(optionChainRequest);
        StepVerifier
                .create(optionChainResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }*/

    @Test
    public void expirationChainTest() {
        Mono<ExpirationChainResponse> expirationChainResponse = schwabMarketDataApiClient.fetchExpirationChainToMono("AAPL");
        StepVerifier
                .create(expirationChainResponse)
                .expectNextMatches(response -> response.getExpirationList() != null &&
                        !response.getExpirationList().isEmpty() &&
                        response.getExpirationList().get(0).getOptionRoots() != null &&
                        response.getExpirationList().get(0).getOptionRoots().equalsIgnoreCase("AAPL"))
                .verifyComplete();
    }

    @Test
    public void expirationChainNotFoundTest() {
        Mono<ExpirationChainResponse> expirationChainResponse = schwabMarketDataApiClient.fetchExpirationChainToMono("XXXXXX");
        StepVerifier
                .create(expirationChainResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void priceHistoryTest() {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.builder().withSymbol("AAPL").withNeedPreviousClose(true).build();
        Mono<PriceHistoryResponse> priceHistoryResponse = schwabMarketDataApiClient.fetchPriceHistoryToMono(priceHistoryRequest);
        StepVerifier
                .create(priceHistoryResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        !response.getSymbol().isEmpty() &&
                        response.getSymbol().equalsIgnoreCase("AAPL") &&
                        response.getCandles() != null &&
                        !response.getCandles().isEmpty())
                .verifyComplete();
    }

    @Test
    public void priceHistoryNotFoundTest() {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.builder().withSymbol("XXXXXX").withNeedPreviousClose(true).build();
        Mono<PriceHistoryResponse> priceHistoryResponse = schwabMarketDataApiClient.fetchPriceHistoryToMono(priceHistoryRequest);
        StepVerifier
                .create(priceHistoryResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void moversTest() {
        MoversRequest moversRequest = MoversRequest.builder().withIndexSymbol(MoversRequest.IndexSymbol.$DJI).build();
        Flux<Screener> moversResponse = schwabMarketDataApiClient.fetchMoversToFlux(moversRequest);
        StepVerifier
                .create(moversResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> true)
                .expectRecordedMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    public void singleMarketTest() {
        Mono<Map<String, Map<String, Hours>>> hoursResponse = schwabMarketDataApiClient.fetchMarketsToMono(Collections.singletonList(SchwabMarketDataApiClient.Market.EQUITY));
        StepVerifier
                .create(hoursResponse)
                .expectNextMatches(response -> !response.isEmpty() &&
                        response.containsKey(SchwabMarketDataApiClient.Market.EQUITY.value()))
                .verifyComplete();
    }

    @Test
    public void multiMarketTest() {
        Mono<Map<String, Map<String, Hours>>> hoursResponse = schwabMarketDataApiClient.fetchMarketsToMono(Arrays.asList(SchwabMarketDataApiClient.Market.EQUITY, SchwabMarketDataApiClient.Market.OPTION));
        StepVerifier
                .create(hoursResponse)
                .expectNextMatches(response -> !response.isEmpty() &&
                        response.containsKey(SchwabMarketDataApiClient.Market.EQUITY.value()) &&
                        response.containsKey(SchwabMarketDataApiClient.Market.OPTION.value()))
                .verifyComplete();
    }

    @Test
    public void instrumentsTest() {
        InstrumentsRequest instrumentsRequest = InstrumentsRequest.builder()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getSymbol() != null &&
                        !element.getSymbol().isEmpty() &&
                        element.getSymbol().equals("AAPL"))
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void instrumentsNotFoundTest() {
        InstrumentsRequest instrumentsRequest = InstrumentsRequest.builder()
                .withSymbol("XXXXXX")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void instrumentsWithFundamentalsTest() {
        InstrumentsRequest fundamentalRequest = InstrumentsRequest.builder()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.FUNDAMENTAL)
                .build();
        Flux<Instrument> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsToFlux(fundamentalRequest);
        StepVerifier
                .create(instrumentsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getSymbol() != null &&
                        !element.getSymbol().isEmpty() &&
                        element.getSymbol().equals("AAPL"))
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void instrumentByCusipTest() {
        Mono<Instrument> instrumentResponse = schwabMarketDataApiClient.fetchInstrumentByCusipToMono("037833100");
        StepVerifier
                .create(instrumentResponse)
                .expectNextMatches(response -> response.getSymbol().equalsIgnoreCase("AAPL") &&
                        response.getFundamental() == null)
                .verifyComplete();
    }

    @Test
    public void instrumentByCusipNotFoundTest() {
        Mono<Instrument> instrumentResponse = schwabMarketDataApiClient.fetchInstrumentByCusipToMono("999999999");
        StepVerifier
                .create(instrumentResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
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
