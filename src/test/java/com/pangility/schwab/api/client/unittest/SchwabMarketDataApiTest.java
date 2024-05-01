package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;
import com.pangility.schwab.api.client.marketdata.MarketNotFoundException;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import com.pangility.schwab.api.client.marketdata.SymbolNotFoundException;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsResponse;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversRequest;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversResponse;
import com.pangility.schwab.api.client.marketdata.model.optionchain.OptionChainRequest;
import com.pangility.schwab.api.client.marketdata.model.optionchain.OptionChainResponse;
import com.pangility.schwab.api.client.marketdata.model.optionexpirationchain.ExpirationChainResponse;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryRequest;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryResponse;
import com.pangility.schwab.api.client.marketdata.model.quote.QuoteResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void quoteTest() throws SymbolNotFoundException {
        QuoteResponse optionResponse = schwabMarketDataApiClient.fetchQuote("TQQQ  240920C00051000");

        QuoteResponse quoteResponse = schwabMarketDataApiClient.fetchQuote("TQQQ");
        assertThat(quoteResponse).isNotNull();
        assertThat(quoteResponse.getSymbol()).isNotNull();
        assertThat(quoteResponse.getSymbol()).isEqualToIgnoringCase("TQQQ");
    }

    @Test
    public void quotesTest() throws SymbolNotFoundException {
        List<QuoteResponse> quoteResponses = schwabMarketDataApiClient.fetchQuotes(Arrays.asList("TQQQ","UPRO"));
        assertThat(quoteResponses).isNotNull();
        assertThat(quoteResponses.size()).isEqualTo(2);
        assertThat(quoteResponses.get(0).getSymbol()).isNotNull();
        assertThat(quoteResponses.get(0).getSymbol()).isEqualToIgnoringCase("TQQQ");
        assertThat(quoteResponses.get(1).getSymbol()).isNotNull();
        assertThat(quoteResponses.get(1).getSymbol()).isEqualToIgnoringCase("UPRO");
    }
    @Test
    public void chainsTest() throws SymbolNotFoundException {
        OptionChainRequest optionChainRequest = OptionChainRequest.Builder.optionChainRequest().withSymbol("TQQQ").build();
        OptionChainResponse optionChainResponse = schwabMarketDataApiClient.fetchOptionChain(optionChainRequest);
        assertThat(optionChainResponse).isNotNull();
    }

    @Test
    public void expirationChainTest() throws SymbolNotFoundException {
        ExpirationChainResponse expirationChainResponse = schwabMarketDataApiClient.fetchExpirationChain("AAPL");
        assertThat(expirationChainResponse).isNotNull();
        assertThat(expirationChainResponse.getExpirationList()).isNotNull();
        assertThat(expirationChainResponse.getExpirationList().size()).isGreaterThan(0);
    }

    @Test
    public void priceHistoryTest() throws SymbolNotFoundException {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.Builder.priceHistReq().withSymbol("AAPL").withNeedPreviousClose(true).build();
        PriceHistoryResponse priceHistoryResponse = schwabMarketDataApiClient.fetchPriceHistory(priceHistoryRequest);
        assertThat(priceHistoryResponse).isNotNull();
    }

    @Test
    public void moversTest() throws SymbolNotFoundException {
        MoversRequest moversRequest = MoversRequest.Builder.moversRequest().withIndexSymbol(MoversRequest.IndexSymbol.$DJI).build();
        MoversResponse moversResponse = schwabMarketDataApiClient.fetchMovers(moversRequest);
        assertThat(moversResponse).isNotNull();
    }

    @Test
    public void marketsTest() throws MarketNotFoundException {
        List<Hours> hours = schwabMarketDataApiClient.fetchMarkets(Collections.singletonList(SchwabMarketDataApiClient.Market.equity));
        assertThat(hours).isNotNull();

        hours = schwabMarketDataApiClient.fetchMarkets(Arrays.asList(SchwabMarketDataApiClient.Market.equity, SchwabMarketDataApiClient.Market.option));
        assertThat(hours).isNotNull();

        LocalDate testDate = LocalDate.of(2024, 4, 25);
        hours = schwabMarketDataApiClient.fetchMarkets(Collections.singletonList(SchwabMarketDataApiClient.Market.equity), testDate);
        assertThat(hours).isNotNull();
        assertThat(hours.size()).isEqualTo(1);
        assertThat(hours.get(0).getDate()).isEqualTo(testDate);
        assertThat(hours.get(0).getIsOpen()).isTrue();
    }

    @Test
    public void instrumentsTest() throws SymbolNotFoundException {
        InstrumentsRequest instrumentsRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.SYMBOL_SEARCH)
                .build();
        InstrumentsResponse instrumentsResponse = schwabMarketDataApiClient.fetchInstruments(instrumentsRequest);
        assertThat(instrumentsResponse).isNotNull();

        InstrumentsRequest fundamentalRequest = InstrumentsRequest.Builder.instrumentsRequest()
                .withSymbol("AAPL")
                .withProjection(InstrumentsRequest.Projection.FUNDAMENTAL)
                .build();
        instrumentsResponse = schwabMarketDataApiClient.fetchInstruments(fundamentalRequest);
        assertThat(instrumentsResponse).isNotNull();
    }

    @Test
    public void instrumentsByCusipTest() throws SymbolNotFoundException {
        InstrumentsResponse instrumentsResponse = schwabMarketDataApiClient.fetchInstrumentsByCusip("037833100");
        assertThat(instrumentsResponse).isNotNull();
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
