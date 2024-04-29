package com.schwabapi.unittest;

import com.schwabapi.marketdata.EnableSchwabMarketDataApi;
import com.schwabapi.marketdata.SchwabMarketDataApiClient;
import com.schwabapi.marketdata.SymbolNotFoundException;
import com.schwabapi.marketdata.model.instruments.InstrumentsRequest;
import com.schwabapi.marketdata.model.instruments.InstrumentsResponse;
import com.schwabapi.marketdata.model.markets.Hours;
import com.schwabapi.marketdata.model.movers.MoversRequest;
import com.schwabapi.marketdata.model.movers.MoversResponse;
import com.schwabapi.marketdata.model.optionchain.OptionChainRequest;
import com.schwabapi.marketdata.model.optionchain.OptionChainResponse;
import com.schwabapi.marketdata.model.optionexpirationchain.ExpirationChainResponse;
import com.schwabapi.marketdata.model.pricehistory.PriceHistoryRequest;
import com.schwabapi.marketdata.model.pricehistory.PriceHistoryResponse;
import com.schwabapi.marketdata.model.quote.QuoteResponse;
import com.schwabapi.oauth2.SchwabAccount;
import com.schwabapi.oauth2.SchwabOauth2Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SchwabMarketDataApiClient.class, SchwabOauth2Controller.class})
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

    @BeforeEach
    public void setUpEachTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(LocalDateTime.parse(refreshExpiration, DateTimeFormatter.ISO_DATE_TIME));
        schwabMarketDataApiClient.init(schwabAccount);
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
    public void marketsTest() throws SymbolNotFoundException {
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

    @TestConfiguration
    public static class TestConfig {
    }
}
