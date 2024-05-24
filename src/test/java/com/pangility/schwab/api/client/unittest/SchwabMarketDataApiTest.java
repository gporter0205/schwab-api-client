package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import com.pangility.schwab.api.client.marketdata.SymbolNotFoundException;
import com.pangility.schwab.api.client.marketdata.model.AssetMainType;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainRequest;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainResponse;
import com.pangility.schwab.api.client.marketdata.model.expirationchain.ExpirationChainResponse;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsResponse;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversRequest;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversResponse;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryRequest;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryResponse;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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
        Mono<QuoteResponse> quoteResponse = schwabMarketDataApiClient.fetchQuoteAsMono("TQQQ");
        StepVerifier
                .create(quoteResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase("TQQQ") &&
                        response.getAssetMainType().equals(AssetMainType.EQUITY))
                .expectComplete()
                .verify();
    }

    @Test
    public void optionQuoteTest() {
        // get next Friday
        LocalDate nextFriday = LocalDate.now().plusDays(3);
        while(!nextFriday.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextFriday = nextFriday.plusDays(1);
        }
        String symbol = "TQQQ  " + nextFriday.format(DateTimeFormatter.ofPattern("yyMMdd")) + "C00051000";

        Mono<QuoteResponse> optionResponse = schwabMarketDataApiClient.fetchQuoteAsMono(symbol);
        StepVerifier
                .create(optionResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase(symbol) &&
                        response.getAssetMainType().equals(AssetMainType.OPTION))
                .expectComplete()
                .verify();
    }

    @Test
    public void quoteNotFoundTest() {
        Mono<QuoteResponse> quoteResponse = schwabMarketDataApiClient.fetchQuoteAsMono("XXXXXX");
        StepVerifier
                .create(quoteResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void quotesTest() {
        Mono<Map<String, QuoteResponse>> quoteResponses = schwabMarketDataApiClient.fetchQuotesAsMono(Arrays.asList("TQQQ","UPRO"));
        StepVerifier
                .create(quoteResponses)
                .expectNextMatches(responses -> responses.size() == 2 &&
                        responses.get("TQQQ").getSymbol() != null &&
                        responses.get("TQQQ").getSymbol().equalsIgnoreCase("TQQQ") &&
                        responses.get("UPRO").getSymbol() != null &&
                        responses.get("UPRO").getSymbol().equalsIgnoreCase("UPRO"))
                .expectComplete()
                .verify();
    }

    /*@Test
    public void quotesNotFoundTest() {
        Mono<Map<String, QuoteResponse>> quoteResponses = schwabMarketDataApiClient.fetchQuotesAsMono(Arrays.asList("TQQQ","UPRO","XXXXXX"));
        StepVerifier
                .create(quoteResponses)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }*/

    @Test
    public void chainsTest() {
        OptionChainRequest optionChainRequest = OptionChainRequest.Builder.optionChainRequest().withSymbol("TQQQ").build();
        Mono<OptionChainResponse> optionChainResponse = schwabMarketDataApiClient.fetchOptionChainAsMono(optionChainRequest);
        StepVerifier
                .create(optionChainResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        response.getSymbol().equalsIgnoreCase("TQQQ") &&
                        response.getCallExpDateMap() != null &&
                        !response.getCallExpDateMap().isEmpty())
                .expectComplete()
                .verify();
    }

    // Service returns a 400 - Bad Request instead of a 404 - Not Found
    /*@Test
    public void chainsNotFoundTest() {
        OptionChainRequest optionChainRequest = OptionChainRequest.Builder.optionChainRequest().withSymbol("XXXXXX").build();
        Mono<OptionChainResponse> optionChainResponse = schwabMarketDataApiClient.fetchOptionChainAsMono(optionChainRequest);
        StepVerifier
                .create(optionChainResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }*/

    @Test
    public void expirationChainTest() {
        Mono<ExpirationChainResponse> expirationChainResponse = schwabMarketDataApiClient.fetchExpirationChainAsMono("AAPL");
        StepVerifier
                .create(expirationChainResponse)
                .expectNextMatches(response -> response.getExpirationList() != null &&
                        !response.getExpirationList().isEmpty() &&
                        response.getExpirationList().get(0).getOptionRoots() != null &&
                        response.getExpirationList().get(0).getOptionRoots().equalsIgnoreCase("AAPL"))
                .expectComplete()
                .verify();
    }

    @Test
    public void expirationChainNotFoundTest() {
        Mono<ExpirationChainResponse> expirationChainResponse = schwabMarketDataApiClient.fetchExpirationChainAsMono("XXXXXX");
        StepVerifier
                .create(expirationChainResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void priceHistoryTest() {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.Builder.priceHistReq().withSymbol("AAPL").withNeedPreviousClose(true).build();
        Mono<PriceHistoryResponse> priceHistoryResponse = schwabMarketDataApiClient.fetchPriceHistoryAsMono(priceHistoryRequest);
        StepVerifier
                .create(priceHistoryResponse)
                .expectNextMatches(response -> response.getSymbol() != null &&
                        !response.getSymbol().isEmpty() &&
                        response.getSymbol().equalsIgnoreCase("AAPL") &&
                        response.getCandles() != null &&
                        !response.getCandles().isEmpty())
                .expectComplete()
                .verify();
    }

    @Test
    public void priceHistoryNotFoundTest() {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.Builder.priceHistReq().withSymbol("XXXXXX").withNeedPreviousClose(true).build();
        Mono<PriceHistoryResponse> priceHistoryResponse = schwabMarketDataApiClient.fetchPriceHistoryAsMono(priceHistoryRequest);
        StepVerifier
                .create(priceHistoryResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void moversTest() {
        MoversRequest moversRequest = MoversRequest.Builder.moversRequest().withIndexSymbol(MoversRequest.IndexSymbol.$DJI).build();
        Mono<MoversResponse> moversResponse = schwabMarketDataApiClient.fetchMoversAsMono(moversRequest);
        StepVerifier
                .create(moversResponse)
                .expectNextMatches(response -> response.getScreeners() != null &&
                        !response.getScreeners().isEmpty())
                .expectComplete()
                .verify();
    }

    @Test
    public void singleMarketTest() {
        Mono<Map<String, Map<String, Hours>>> hoursResponse = schwabMarketDataApiClient.fetchMarketsAsMono(Collections.singletonList(SchwabMarketDataApiClient.Market.EQUITY));
        StepVerifier
                .create(hoursResponse)
                .expectNextMatches(response -> !response.isEmpty() &&
                        response.containsKey(SchwabMarketDataApiClient.Market.EQUITY.value()))
                .expectComplete()
                .verify();

        /*LocalDate testDate = LocalDate.of(2024, 4, 25);
        hours = schwabMarketDataApiClient.fetchMarkets(Collections.singletonList(SchwabMarketDataApiClient.Market.EQUITY), testDate);
        assertThat(hours).isNotNull();
        assertThat(hours.size()).isEqualTo(1);
        assertThat(hours.get(0).getDate()).isEqualTo(testDate);
        assertThat(hours.get(0).getIsOpen()).isTrue();*/
    }

    @Test
    public void multiMarketTest() {
        Mono<Map<String, Map<String, Hours>>> hoursResponse = schwabMarketDataApiClient.fetchMarketsAsMono(Arrays.asList(SchwabMarketDataApiClient.Market.EQUITY, SchwabMarketDataApiClient.Market.OPTION));
        StepVerifier
                .create(hoursResponse)
                .expectNextMatches(response -> !response.isEmpty() &&
                        response.containsKey(SchwabMarketDataApiClient.Market.EQUITY.value()) &&
                        response.containsKey(SchwabMarketDataApiClient.Market.OPTION.value()))
                .expectComplete()
                .verify();
    }

    @Test
    public void instrumentsTest() {
        InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsAsMono(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectNextMatches(response -> response.getInstruments() != null &&
                                                response.getInstruments().size() == 1 &&
                                                response.getInstruments().get(0).getSymbol().equalsIgnoreCase("AAPL") &&
                                                response.getInstruments().get(0).getFundamental() == null)
                .expectComplete()
                .verify();
    }

    @Test
    public void instrumentsNotFoundTest() {
        InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("XXXXXX")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsAsMono(instrumentsRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
    }

    @Test
    public void instrumentsWithFundamentalsTest() {
        InstrumentsRequest fundamentalRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.FUNDAMENTAL)
                .build();
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsAsMono(fundamentalRequest);
        StepVerifier
                .create(instrumentsResponse)
                .expectNextMatches(response -> response.getInstruments() != null &&
                        response.getInstruments().size() == 1 &&
                        response.getInstruments().get(0).getSymbol().equalsIgnoreCase("AAPL") &&
                        response.getInstruments().get(0).getFundamental() != null)
                .expectComplete()
                .verify();
    }

    @Test
    public void instrumentsByCusipTest() {
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsByCusipAsMono("037833100");
        StepVerifier
                .create(instrumentsResponse)
                .expectNextMatches(response -> response.getInstruments() != null &&
                        response.getInstruments().size() == 1 &&
                        response.getInstruments().get(0).getSymbol().equalsIgnoreCase("AAPL") &&
                        response.getInstruments().get(0).getFundamental() == null)
                .expectComplete()
                .verify();
    }

    @Test
    public void instrumentsByCusipNotFoundTest() {
        Mono<InstrumentsResponse> instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsByCusipAsMono("999999999");
        StepVerifier
                .create(instrumentsResponse)
                .expectError(SymbolNotFoundException.class)
                .verify();
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
