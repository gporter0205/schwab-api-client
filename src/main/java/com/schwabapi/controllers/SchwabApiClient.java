package com.schwabapi.controllers;

import com.schwabapi.deserializers.QuoteResponseHashMap;
import com.schwabapi.model.SchwabAccount;
import com.schwabapi.model.movers.MoversRequest;
import com.schwabapi.model.movers.MoversResponse;
import com.schwabapi.model.optionchain.OptionChainRequest;
import com.schwabapi.model.optionchain.OptionChainResponse;
import com.schwabapi.model.optionexpirationchain.ExpirationChainResponse;
import com.schwabapi.model.pricehistory.Candle;
import com.schwabapi.model.pricehistory.PriceHistoryRequest;
import com.schwabapi.model.pricehistory.PriceHistoryResponse;
import com.schwabapi.model.quote.QuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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

    public MoversResponse fetchMovers(@NotNull MoversRequest moversRequest) throws SymbolNotFoundException {
        log.info("Fetch Movers for: {}", moversRequest.getIndexSymbol());
        MoversResponse response = null;

        if (moversRequest.getIndexSymbol() != null) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "movers", moversRequest.getIndexSymbol().toString());
            response = this.callGetAPI(uriBuilder, MoversResponse.class);
            if (response == null) {
                throw new SymbolNotFoundException("Movers for '" + moversRequest.getIndexSymbol().toString() + "' not found");
            }
        }
        return response;
    }

    public PriceHistoryResponse fetchPriceHistory(PriceHistoryRequest priceHistReq)
            throws SymbolNotFoundException {
        log.info("Fetch Price History: {}", priceHistReq);

        if (priceHistReq.getSymbol() == null || priceHistReq.getSymbol().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be blank.");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabMarketDataPath, schwabApiVersion, "pricehistory")
                .queryParam("symbol", priceHistReq.getSymbol().toUpperCase());
        if (priceHistReq.getPeriodType() != null) {
            uriBuilder.queryParam("periodType", priceHistReq.getPeriodType().name());
        }
        if (priceHistReq.getPeriod() != null) {
            uriBuilder.queryParam("period", String.valueOf(priceHistReq.getPeriod()));
        }
        if (priceHistReq.getFrequencyType() != null) {
            uriBuilder.queryParam("frequencyType", priceHistReq.getFrequencyType().name());
        }
        if (priceHistReq.getFrequency() != null) {
            uriBuilder.queryParam("frequency", String.valueOf(priceHistReq.getFrequency()));
        }
        if (priceHistReq.getStartDate() != null) {
            uriBuilder.queryParam("startDate", String.valueOf(priceHistReq.getStartDate()));
        }
        if (priceHistReq.getEndDate() != null) {
            uriBuilder.queryParam("endDate", String.valueOf(priceHistReq.getEndDate()));
        }
        if (priceHistReq.getNeedExtendedHoursData() != null) {
            uriBuilder.queryParam("needExtendedHoursData",
                    String.valueOf(priceHistReq.getNeedExtendedHoursData()));
        }
        if (priceHistReq.getNeedPreviousClose() != null) {
            uriBuilder.queryParam("needPreviousClose",
                    String.valueOf(priceHistReq.getNeedPreviousClose()));
        }
        PriceHistoryResponse response = this.callGetAPI(uriBuilder, PriceHistoryResponse.class);
        if (response != null) {
            if(response.getPreviousCloseDate() != null && response.getPreviousCloseDateISO8601() == null) {
                LocalDate ld = Instant.ofEpochMilli(response.getPreviousCloseDate()).atZone(ZoneId.systemDefault()).toLocalDate();
                response.setPreviousCloseDateISO8601(ld);
            }
            if(response.getCandles() != null) {
                for(Candle candle : response.getCandles()) {
                    if(candle.getDatetimeISO8601() == null) {
                        LocalDateTime ldt = Instant.ofEpochMilli(candle.getDatetime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        candle.setDatetimeISO8601(ldt);
                    }
                }
            }
        } else {
            throw new SymbolNotFoundException("'" + priceHistReq.getSymbol() + "' not found");
        }
        return response;
    }

    public ExpirationChainResponse fetchExpirationChain(@NotNull String symbol)
            throws SymbolNotFoundException {
        log.info("Fetch Expiration Chain for: {}", symbol);
        ExpirationChainResponse response = null;

        if (symbol.length() > 0) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "expirationchain")
                    .queryParam("symbol", symbol.toUpperCase());
            response = this.callGetAPI(uriBuilder, ExpirationChainResponse.class);
            if (response == null ||
                    response.getExpirationList() == null ||
                    response.getExpirationList().size() == 0 ||
                    (response.getExpirationList().get(0).getOptionRoots() != null &&
                    !response.getExpirationList().get(0).getOptionRoots().equalsIgnoreCase(symbol))) {
                throw new SymbolNotFoundException("'" + symbol + "' not found");
            }
        }
        return response;
    }

    public OptionChainResponse fetchOptionChain(@NotNull OptionChainRequest chainRequest)
            throws SymbolNotFoundException {
        log.info("Fetch option chain for: {}", chainRequest.toString());

        OptionChainResponse optionChainResponse;
        if (chainRequest.getSymbol() == null || chainRequest.getSymbol().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be blank.");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabMarketDataPath, schwabApiVersion, "chains")
                .queryParam("symbol", chainRequest.getSymbol().toUpperCase());
        if(chainRequest.getContractType() != null) {
            uriBuilder.queryParam("contractType", chainRequest.getContractType().toString());
        }
        if(chainRequest.getStrategy() != null) {
            uriBuilder.queryParam("strategy", chainRequest.getStrategy().toString());
        }
        if(chainRequest.getRange() != null) {
            uriBuilder.queryParam("range", chainRequest.getRange().toString());
        }
        if(chainRequest.getOptionType() != null) {
            uriBuilder.queryParam("optionType", chainRequest.getOptionType().toString());
        }
        if(chainRequest.getStrikeCount() != null && chainRequest.getStrikeCount() > 0) {
            uriBuilder.queryParam("strikeCount", chainRequest.getStrikeCount().toString());
        }
        if(chainRequest.getIncludeQuotes() != null) {
            uriBuilder.queryParam("includeQuotes", chainRequest.getIncludeQuotes().toString());
        }
        if(chainRequest.getInterval() != null) {
            uriBuilder.queryParam("interval", chainRequest.getInterval().toString());
        }
        if(chainRequest.getStrike() != null) {
            uriBuilder.queryParam("strike", chainRequest.getStrike().toString());
        }
        if(chainRequest.getFromDate() != null) {
            uriBuilder.queryParam("fromDate", chainRequest.getFromDate().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        if(chainRequest.getToDate() != null) {
            uriBuilder.queryParam("toDate", chainRequest.getToDate().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        if(chainRequest.getVolatility() != null) {
            uriBuilder.queryParam("volatility", chainRequest.getVolatility().toString());
        }
        if(chainRequest.getUnderlyingPrice() != null) {
            uriBuilder.queryParam("underlyingPrice", chainRequest.getUnderlyingPrice().toString());
        }
        if(chainRequest.getInterestRate() != null) {
            uriBuilder.queryParam("interestRate", chainRequest.getInterestRate().toString());
        }
        if(chainRequest.getDaysToExpiration() != null) {
            uriBuilder.queryParam("daysToExpiration", chainRequest.getDaysToExpiration().toString());
        }
        if(chainRequest.getMonth() != null) {
            uriBuilder.queryParam("month", chainRequest.getMonth().toString().substring(0, 3).toUpperCase());
        }
        optionChainResponse = this.callGetAPI(uriBuilder, OptionChainResponse.class);
        if (optionChainResponse == null || optionChainResponse.getSymbol() == null || optionChainResponse.getSymbol().isEmpty()) {
            throw new SymbolNotFoundException("'" + chainRequest.getSymbol() + "' not found");
        }

        return optionChainResponse;
    }

    public QuoteResponse fetchQuote(@NotNull String symbol)
            throws SymbolNotFoundException {
        return fetchQuote(symbol, null);
    }

    public QuoteResponse fetchQuote(@NotNull String symbol,
                            String fields)
            throws SymbolNotFoundException {
        log.info("Fetch Quote for: {}", symbol);
        QuoteResponse quoteResponse = null;

        if (symbol.length() > 0) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, symbol.toUpperCase(), "quotes");
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
        log.info("Fetch Quotes for: {}", symbols);

        if(fields == null || fields.isEmpty()) {
            fields = "all";
        }
        List<QuoteResponse> quotes = null;
        if (symbols.size() > 0) {
            String symbolsParam = String.join(",", symbols).toUpperCase();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "quotes")
                    .queryParam("symbols", symbolsParam)
                    .queryParam("fields", fields);
            if(indicative != null) {
                uriBuilder.queryParam("indicative", indicative);
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
