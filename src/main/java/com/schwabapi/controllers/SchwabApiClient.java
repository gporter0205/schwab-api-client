package com.schwabapi.controllers;

import com.schwabapi.deserializers.QuoteResponseHashMap;
import com.schwabapi.model.SchwabAccount;
import com.schwabapi.model.quote.QuoteResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchwabApiClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    @Value("${schwab-api.marketDataPath}")
    private String schwabMarketDataPath;
    @Value("${schwab-api.apiVersion}")
    private String schwabApiVersion;
    @Value("${schwab-api.traderPath}")
    private String schwabTraderPath;

    @Autowired
    private SchwabOauth2Controller schwabOauth2Controller;
    @Autowired
    @Qualifier("schwabWebClient")
    private WebClient schwabWebClient;

    private String defaultUserId = null;

    public void init(SchwabAccount schwabAccount) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount));
    }
    public void init(String defaultUserId, List<SchwabAccount> schwabAccounts) {
        this.defaultUserId = defaultUserId;
        schwabOauth2Controller.init(schwabAccounts);
    }

    public QuoteResponse fetchQuote(@NotNull String symbol)
            throws SymbolNotFoundException {
        return fetchQuote(symbol, null);
    }

    public QuoteResponse fetchQuote(@NotNull String symbol,
                            String fields)
            throws SymbolNotFoundException {
        QuoteResponse quoteResponse = null;

        if (symbol.length() > 0) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, symbol, "quotes");
            if(fields != null) {
                uriBuilder.queryParam("fields", fields);
            }
            QuoteResponseHashMap response = this.callGetAPI(uriBuilder, QuoteResponseHashMap.class);
            if (response != null && response.quoteMap.size() > 0 && response.quoteMap.containsKey(symbol)) {
                quoteResponse = response.quoteMap.get(symbol);
            } else {
                throw new SymbolNotFoundException("'" + symbol + "' not found");
            }
        }
        return quoteResponse;
    }

    public List<QuoteResponse> fetchQuotes(@NotNull List<String> symbols)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, null);
    }

    public List<QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                           String fields)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, fields, null);
    }

    public List<QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                           String fields,
                                           Boolean indicative)
            throws SymbolNotFoundException {
        List<QuoteResponse> quotes = null;
        if (symbols.size() > 0) {
            String symbolsParam = String.join(",", symbols);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "quotes")
                    .queryParam("symbols", symbolsParam);
            if(fields != null && !fields.isEmpty()) {
                uriBuilder.queryParam("fields", "quote");
            }
            if(indicative != null) {
                uriBuilder.queryParam("indicative", false);
            }
            QuoteResponseHashMap response = this.callGetAPI(uriBuilder, QuoteResponseHashMap.class);
            if(response != null && response.quoteMap.size() > 0) {
                for(String symbol: symbols) {
                    QuoteResponse quoteResponse = response.quoteMap.get(symbol);
                    if(quoteResponse != null) {
                        if(quotes == null) {
                            quotes = new ArrayList<>();
                        }
                        quotes.add(quoteResponse);
                    }
                }
            } else {
                throw new SymbolNotFoundException("'" + symbols + "' not found");
            }
        }
        return quotes;
    }

    private <T> T callGetAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                             @NotNull Class<T> clazz) {

        T ret = null;
        String accessToken = schwabOauth2Controller.getAccessToken(defaultUserId);
        if(accessToken != null) {
            URI uri = uriComponentsBuilder
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .build()
                    .toUri();
            ResponseEntity<T> retMono = schwabWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(uri.getPath())
                            .query(uri.getQuery())
                            .build()
                    )
                    .headers(h -> h.setBearerAuth(accessToken))
                    .retrieve()
                    .toEntity(clazz)
                    .block();
            if (retMono != null && retMono.hasBody()) {
                ret = retMono.getBody();
            }
        }
        return ret;
    }
}
