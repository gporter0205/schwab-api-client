package com.schwabapi.unittest;

import com.schwabapi.config.EnableSchwabApi;
import com.schwabapi.controllers.SchwabApiClient;
import com.schwabapi.controllers.SchwabOauth2Controller;
import com.schwabapi.controllers.SymbolNotFoundException;
import com.schwabapi.model.SchwabAccount;
import com.schwabapi.model.movers.MoversRequest;
import com.schwabapi.model.movers.MoversResponse;
import com.schwabapi.model.optionchain.OptionChainRequest;
import com.schwabapi.model.optionchain.OptionChainResponse;
import com.schwabapi.model.optionexpirationchain.ExpirationChainResponse;
import com.schwabapi.model.pricehistory.PriceHistoryRequest;
import com.schwabapi.model.pricehistory.PriceHistoryResponse;
import com.schwabapi.model.quote.QuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SchwabApiClient.class, SchwabOauth2Controller.class})
@EnableSchwabApi
@ActiveProfiles("unittest")
public class SchwabApiTest {

    private final String schwabUserId = "testUser";
    private final String refreshToken = "uZ1-eZEWKKywO_r1iONVolv7BUu_m8yOoNWWM_42FAYukTSB9plG2Z6C1fnhflZB6qlCzDM1Lp_kLZUS4y4KTKI2zt4KlIpO";
    private final LocalDateTime refreshExpiration = LocalDateTime.parse("2024-04-30T16:13:45.3450989", DateTimeFormatter.ISO_DATE_TIME);
    private final SchwabAccount schwabAccount = new SchwabAccount();

    @Autowired
    private SchwabApiClient schwabApiClient;

    @BeforeEach
    public void setUpEachTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(refreshExpiration);
        schwabApiClient.init(schwabAccount);
    }

    @Test
    public void quoteTest() throws SymbolNotFoundException {
        QuoteResponse optionResponse = schwabApiClient.fetchQuote("TQQQ  240920C00051000");

        QuoteResponse quoteResponse = schwabApiClient.fetchQuote("TQQQ");
        assertThat(quoteResponse).isNotNull();
        assertThat(quoteResponse.getSymbol()).isNotNull();
        assertThat(quoteResponse.getSymbol()).isEqualToIgnoringCase("TQQQ");
    }

    @Test
    public void quotesTest() throws SymbolNotFoundException {
        List<QuoteResponse> quoteResponses = schwabApiClient.fetchQuotes(Arrays.asList("TQQQ","UPRO"));
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
        OptionChainResponse optionChainResponse = schwabApiClient.fetchOptionChain(optionChainRequest);
        assertThat(optionChainResponse).isNotNull();
    }

    @Test
    public void expirationChainTest() throws SymbolNotFoundException {
        ExpirationChainResponse expirationChainResponse = schwabApiClient.fetchExpirationChain("AAPL");
        assertThat(expirationChainResponse).isNotNull();
        assertThat(expirationChainResponse.getExpirationList()).isNotNull();
        assertThat(expirationChainResponse.getExpirationList().size()).isGreaterThan(0);
    }

    @Test
    public void priceHistoryTest() throws SymbolNotFoundException {
        PriceHistoryRequest priceHistoryRequest = PriceHistoryRequest.Builder.priceHistReq().withSymbol("AAPL").withNeedPreviousClose(true).build();
        PriceHistoryResponse priceHistoryResponse = schwabApiClient.fetchPriceHistory(priceHistoryRequest);
        assertThat(priceHistoryResponse).isNotNull();
    }

    @Test
    public void moversTest() throws SymbolNotFoundException {
        MoversRequest moversRequest = MoversRequest.Builder.moversRequest().withIndexSymbol(MoversRequest.IndexSymbol.$DJI).build();
        MoversResponse moversResponse = schwabApiClient.fetchMovers(moversRequest);
        assertThat(moversResponse).isNotNull();
    }

    @TestConfiguration
    public static class TestConfig {
    }
}
