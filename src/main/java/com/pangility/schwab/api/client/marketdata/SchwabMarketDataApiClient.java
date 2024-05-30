package com.pangility.schwab.api.client.marketdata;

import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainRequest;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainResponse;
import com.pangility.schwab.api.client.marketdata.model.expirationchain.ExpirationChainResponse;
import com.pangility.schwab.api.client.marketdata.model.instruments.Instrument;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsResponse;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversRequest;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversResponse;
import com.pangility.schwab.api.client.marketdata.model.movers.Screener;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryRequest;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryResponse;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main client for interacting with the Schwab Market Data API.
 * Use {@literal @}Autowire to create the component in any class annotated with
 * {@literal @}EnableSchwabMarketDataApi or {@literal @}EnableSchwabApi
 */
@Service
@ConditionalOnResource(resources = {"classpath:schwabapiclient.properties"})
@Slf4j
public class SchwabMarketDataApiClient extends SchwabBaseApiClient {

    @Value("${schwab-api.marketDataPath}")
    private String schwabMarketDataPath;

    private String defaultUserId = null;

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    @Override
    public void init(@NotNull SchwabAccount schwabAccount,
                     SchwabTokenHandler tokenHandler) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), tokenHandler);

    }

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     */
    @SuppressWarnings("unused")
    @Override
    public void init(@NotNull SchwabAccount schwabAccount) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), null);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull String defaultUserId,
                     @NotNull List<SchwabAccount> schwabAccounts) {
        this.init(defaultUserId, schwabAccounts, null);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull String defaultUserId,
                     @NotNull List<SchwabAccount> schwabAccounts,
                     SchwabTokenHandler tokenHandler) {
        this.init(schwabAccounts, tokenHandler);
        this.defaultUserId = defaultUserId;
    }

    /**
     * determine if the controller has been initialized
     * @return Boolean
     */
    @SuppressWarnings("unused")
    public Boolean isInitialized() {
        return this.defaultUserId != null && super.isInitialized();
    }

    /**
     * fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link QuoteResponse}
     */
    @Deprecated
    public QuoteResponse fetchQuote(@NotNull String symbol) {
        return fetchQuote(symbol, null);
    }

    /**
     * fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @param fields String (quote, fundamental or all)
     * @return {@link QuoteResponse}
     */
    @Deprecated
    public QuoteResponse fetchQuote(@NotNull String symbol,
                                    String fields) {
        return this.fetchQuoteToMono(symbol, fields).block();
    }

    /**
     * reactively fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link Mono}{@literal <}{@link QuoteResponse}{@literal >}
     */
    public Mono<QuoteResponse> fetchQuoteToMono(@NotNull String symbol) {
        return this.fetchQuoteToMono(symbol, null);
    }

    /**
     * reactively fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @param fields String (quote, fundamental or all)
     * @return {@link Mono}{@literal <}{@link QuoteResponse}{@literal >}
     */
    public Mono<QuoteResponse> fetchQuoteToMono(@NotNull String symbol,
                                                 String fields) {
        log.info("Fetch Quote [{}]", symbol);
        Mono<QuoteResponse> quoteResponseMono;

        if(fields == null || fields.isEmpty()) {
            fields = "all";
        }

        if (!symbol.isEmpty()) {
            UriComponentsBuilder uriBuilder = this.getUriBuilder()
                    .pathSegment(symbol.toUpperCase(), "quotes")
                    .queryParam("fields", fields);
            quoteResponseMono = this.callGetApiToMono(defaultUserId, uriBuilder, new ParameterizedTypeReference<Map<String, QuoteResponse>>() {})
                    .onErrorResume(throwable -> {
                        if(throwable instanceof WebClientResponseException) {
                            if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                                return Mono.error(new SymbolNotFoundException("'" + symbol + "' not found"));
                            }
                        }
                        return Mono.error(throwable);
                    })
                    .flatMap(quoteResponseMap -> {
                        if(!quoteResponseMap.isEmpty() && quoteResponseMap.containsKey(symbol)) {
                            return Mono.just(quoteResponseMap.get(symbol));
                        } else {
                            return Mono.error(new SymbolNotFoundException("'" + symbol + "' not found"));
                        }
                    });
        } else {
            throw new IllegalArgumentException("A Quote must include a symbol.");
        }
        return quoteResponseMono;
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @return {@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}
     */
    @Deprecated
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols) {
        return fetchQuotes(symbols, null);
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @return {@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}
     */
    @Deprecated
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                           String fields) {
        return fetchQuotes(symbols, fields, null);
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @param indicative Boolean (include indicative symbol quotes for all ETF symbols in request)
     * @return {@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}
     */
    @Deprecated
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                                  String fields,
                                                  Boolean indicative) {
        return this.fetchQuotesToMono(symbols, fields, indicative).block();
    }

    /**
     * reactively fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}{@literal >}
     */
    public Mono<Map<String, QuoteResponse>> fetchQuotesToMono(@NotNull List<String> symbols) {
        return this.fetchQuotesToMono(symbols, null);
    }

    /**
     * reactively fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}{@literal >}
     */
    public Mono<Map<String, QuoteResponse>> fetchQuotesToMono(@NotNull List<String> symbols,
                                                              String fields) {
        return this.fetchQuotesToMono(symbols, fields, null);
    }

    /**
     * reactively fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @param indicative Boolean (include indicative symbol quotes for all ETF symbols in request)
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link QuoteResponse}{@literal >}{@literal >}
     */
    public Mono<Map<String, QuoteResponse>> fetchQuotesToMono(@NotNull List<String> symbols,
                                                              String fields,
                                                              Boolean indicative) {
        log.info("Fetch Quotes -> [{}]", symbols);

        if(fields == null || fields.isEmpty()) {
            fields = "all";
        }
        if (symbols.isEmpty()) {
            throw new IllegalArgumentException("Quotes must include one or more symbols.");
        }

        String symbolsParam = String.join(",", symbols).toUpperCase();
        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("quotes")
                .queryParam("symbols", symbolsParam)
                .queryParam("fields", fields);
        if(indicative != null) {
            uriBuilder.queryParam("indicative", indicative);
        }
        return this.callGetApiToMono(defaultUserId, uriBuilder, new ParameterizedTypeReference<Map<String, QuoteResponse>>() {})
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SymbolNotFoundException("'" + symbols + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(quoteResponseMap -> {
                    if(!quoteResponseMap.isEmpty() && quoteResponseMap.size() == symbols.size()) {
                        return Mono.just(quoteResponseMap);
                    } else {
                        return Mono.error(new SymbolNotFoundException("One or more '" + symbols + "' symbols not found"));
                    }
                });
    }

    /**
     * fetch an option chain from the Schwab API
     * @param chainRequest {@literal @}NotNull {@link OptionChainRequest}
     * @return {@link OptionChainResponse}
     */
    @Deprecated
    public OptionChainResponse fetchOptionChain(@NotNull OptionChainRequest chainRequest) {
        return this.fetchOptionChainToMono(chainRequest).block();
    }

    /**
     * fetch an option chain from the Schwab API
     * @param chainRequest {@literal @}NotNull {@link OptionChainRequest}
     * @return {@link Mono}{@literal <}{@link OptionChainResponse}{@literal >}
     */
    public Mono<OptionChainResponse> fetchOptionChainToMono(@NotNull OptionChainRequest chainRequest) {
        log.info("Fetch Option Chain -> {}", chainRequest);

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
        return this.callGetApiToMono(defaultUserId, uriBuilder, OptionChainResponse.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SymbolNotFoundException("'" + chainRequest.getSymbol() + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(response -> {
                    if(response.getSymbol() != null && !response.getSymbol().isEmpty()) {
                        return Mono.just(response);
                    } else {
                        return Mono.error(new SymbolNotFoundException("'" + chainRequest.getSymbol() + "' not found"));
                    }
                });
    }

    /**
     * fetch an expiration chain from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link ExpirationChainResponse}
     */
    @Deprecated
    public ExpirationChainResponse fetchExpirationChain(@NotNull String symbol) {
        return this.fetchExpirationChainToMono(symbol).block();
    }

    /**
     * reactively fetch an expiration chain from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link Mono}{@literal <}{@link ExpirationChainResponse}{@literal >}
     */
    public Mono<ExpirationChainResponse> fetchExpirationChainToMono(@NotNull String symbol) {
        log.info("Fetch Expiration Chain -> [{}]", symbol);

        if (symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be blank.");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("expirationchain")
                .queryParam("symbol", symbol.toUpperCase());
        return this.callGetApiToMono(defaultUserId, uriBuilder, ExpirationChainResponse.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SymbolNotFoundException("'" + symbol + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(response -> {
                    if(response.getExpirationList() != null && !response.getExpirationList().isEmpty() && response.getExpirationList().get(0).getOptionRoots() != null && response.getExpirationList().get(0).getOptionRoots().equalsIgnoreCase(symbol)) {
                        return Mono.just(response);
                    } else {
                        return Mono.error(new SymbolNotFoundException("'" + symbol + "' not found"));
                    }
                });
    }

    /**
     * fetch the price history from the Schwab API
     * @param priceHistReq {@literal @}NotNull {@link PriceHistoryRequest}
     * @return {@link PriceHistoryResponse}
     */
    @Deprecated
    public PriceHistoryResponse fetchPriceHistory(@NotNull PriceHistoryRequest priceHistReq) {
        return this.fetchPriceHistoryToMono(priceHistReq).block();
    }

    /**
     * fetch the price history from the Schwab API
     * @param priceHistReq {@literal @}NotNull {@link PriceHistoryRequest}
     * @return {@link Mono}{@literal <}{@link PriceHistoryResponse}{@literal >}
     */
    public Mono<PriceHistoryResponse> fetchPriceHistoryToMono(@NotNull PriceHistoryRequest priceHistReq) {
        log.info("Fetch Price History -> {}", priceHistReq);

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
            long epochStartDay = priceHistReq.getStartDate().toEpochDay() * 86400 * 1000;
            uriBuilder.queryParam("startDate", String.valueOf(epochStartDay));
        }
        if (priceHistReq.getEndDate() != null) {
            long epochEndDay = priceHistReq.getEndDate().plusDays(1).toEpochDay() * 86400 * 1000;
            uriBuilder.queryParam("endDate", String.valueOf(epochEndDay));
        }
        if (priceHistReq.getNeedExtendedHoursData() != null) {
            uriBuilder.queryParam("needExtendedHoursData",
                    String.valueOf(priceHistReq.getNeedExtendedHoursData()));
        }
        if (priceHistReq.getNeedPreviousClose() != null) {
            uriBuilder.queryParam("needPreviousClose",
                    String.valueOf(priceHistReq.getNeedPreviousClose()));
        }
        return this.callGetApiToMono(defaultUserId, uriBuilder, PriceHistoryResponse.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SymbolNotFoundException("'" + priceHistReq.getSymbol() + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .doOnSuccess(priceHistoryResponse -> {
                    if(priceHistoryResponse.getPreviousCloseDate() != null && priceHistoryResponse.getPreviousCloseDateISO8601() == null) {
                        LocalDate ld = Instant.ofEpochMilli(priceHistoryResponse.getPreviousCloseDate()).atZone(ZoneId.systemDefault()).toLocalDate();
                        priceHistoryResponse.setPreviousCloseDateISO8601(ld);
                        if(priceHistoryResponse.getCandles() != null) {
                            priceHistoryResponse.getCandles().forEach(candle -> {
                                if(candle.getDatetimeISO8601() == null) {
                                    LocalDateTime ldt = Instant.ofEpochMilli(candle.getDatetime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                                    candle.setDatetimeISO8601(ldt);
                                }
                            });
                        }
                    }
                })
                .flatMap(response -> {
                    if(!response.getEmpty() && response.getSymbol() != null && !response.getSymbol().isEmpty() && response.getSymbol().equalsIgnoreCase(priceHistReq.getSymbol())) {
                        return Mono.just(response);
                    } else {
                        return Mono.error(new SymbolNotFoundException("'" + priceHistReq.getSymbol() + "' not found"));
                    }
                });
    }

    /**
     * fetch the movers from the Schwab API
     * @param moversRequest {@literal @}NotNull {@link MoversRequest}
     * @return {@link MoversResponse}
     */
    @Deprecated
    public MoversResponse fetchMovers(@NotNull MoversRequest moversRequest) {
        MoversResponse moversResponse = new MoversResponse();
        moversResponse.setScreeners(this.fetchMoversToFlux(moversRequest).toStream().toList());
        return moversResponse;
    }

    /**
     * reactively fetch the movers from the Schwab API
     * @param moversRequest {@literal @}NotNull {@link MoversRequest}
     * @return {@link Flux}{@literal <}{@link Screener}{@literal >}
     */
    public Flux<Screener> fetchMoversToFlux(@NotNull MoversRequest moversRequest) {
        log.info("Fetch Movers -> {}", moversRequest);

        if (moversRequest.getIndexSymbol() == null) {
            throw new IllegalArgumentException("Index Symbol cannot be blank.");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
            .pathSegment("movers", moversRequest.getIndexSymbol().toString());
        return this.callGetApiToMono(defaultUserId, uriBuilder, MoversResponse.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new IndexNotFoundException("Movers for '" + moversRequest.getIndexSymbol().toString() + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flux()
                .flatMap(moversResponse -> Flux.fromIterable(moversResponse.getScreeners()));
    }

    /**
     * fetch a map of market hours for today from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @return {@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}
     */
    @Deprecated
    @SuppressWarnings("unused")
    public Map<String, Map<String, Hours>> fetchMarket(@NotNull Market market) {
        return fetchMarket(market, null);
    }

    /**
     * fetch a map of market hours from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @param date LocalDate
     * @return {@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}
     */
    @Deprecated
    public Map<String, Map<String, Hours>> fetchMarket(@NotNull Market market,
                                                       LocalDate date) {
        return this.fetchMarkets(Collections.singletonList(market), date);
    }

    /**
     * fetch a map of market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @return {@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}
     */
    @Deprecated
    public Map<String, Map<String, Hours>> fetchMarkets(@NotNull List<Market> markets) {
        return fetchMarkets(markets, null);
    }

    /**
     * fetch a map of market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @param date LocalDate
     * @return {@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}
     */
    @Deprecated
    public Map<String, Map<String, Hours>> fetchMarkets(@NotNull List<Market> markets,
                                                        LocalDate date) {
        return this.fetchMarketsToMono(markets, date).block();
    }

    /**
     * reactively fetch a map of market hours for today from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}{@literal >}
     */
    @SuppressWarnings("unused")
    public Mono<Map<String, Map<String, Hours>>> fetchMarketToMono(@NotNull Market market) {
        return fetchMarketToMono(market, null);
    }

    /**
     * reactively fetch a map of market hours from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @param date LocalDate
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}{@literal >}
     */
    public Mono<Map<String, Map<String, Hours>>> fetchMarketToMono(@NotNull Market market,
                                                       LocalDate date) {
        return this.fetchMarketsToMono(Collections.singletonList(market), date);
    }

    /**
     * fetch a map of market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}{@literal >}
     */
    public Mono<Map<String, Map<String, Hours>>> fetchMarketsToMono(@NotNull List<Market> markets) {
        return fetchMarketsToMono(markets, null);
    }

    /**
     * reactively fetch a map of market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @param date LocalDate
     * @return {@link Mono}{@literal <}{@link Map}{@literal <}String, {@link Map}{@literal <}String, {@link Hours}{@literal >}{@literal >}{@literal >}
     */
    public Mono<Map<String, Map<String, Hours>>> fetchMarketsToMono(@NotNull List<Market> markets,
                                    LocalDate date) {
        log.info("Fetch Market Hours -> {}", markets);

        if (markets.isEmpty()) {
            throw new IllegalArgumentException("At least one market is required to fetch Market Hours.");
        }

        String marketsString = String.join(",", markets.stream().map(Market::value).toArray(String[]::new));
        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("markets")
                .queryParam("markets", marketsString);
        if (date != null) {
            uriBuilder.queryParam("date", date.format(DateTimeFormatter.ISO_DATE));
        }
        return this.callGetApiToMono(defaultUserId, uriBuilder, new ParameterizedTypeReference<Map<String, Map<String, Hours>>>() {})
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new MarketNotFoundException("Market Hours for '" + markets + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(response -> {
                    if(!response.isEmpty()) {
                        return Mono.just(response);
                    } else {
                        return Mono.error(new MarketNotFoundException("Market Hours for '" + markets + "' not found"));
                    }
                });
    }

    /**
     * fetch instruments from the Schwab API
     * @param instrumentsRequest {@literal @}NotNull {@link InstrumentsRequest}
     * @return {@link InstrumentsResponse}
     */
    @Deprecated
    public InstrumentsResponse fetchInstruments(@NotNull InstrumentsRequest instrumentsRequest) {
        InstrumentsResponse instrumentsResponse = new InstrumentsResponse();
        instrumentsResponse.setInstruments(fetchInstrumentsToFlux(instrumentsRequest).toStream().toList());
        return instrumentsResponse;
    }

    /**
     * reactively fetch instruments from the Schwab API
     * @param instrumentsRequest {@literal @}NotNull {@link InstrumentsRequest}
     * @return {@link Flux}{@literal <}{@link Instrument}{@literal >}
     */
    public Flux<Instrument> fetchInstrumentsToFlux(@NotNull InstrumentsRequest instrumentsRequest) {
        log.info("Fetch Instruments -> {}", instrumentsRequest);

        if (instrumentsRequest.getSymbol() == null || instrumentsRequest.getSymbol().isEmpty() || instrumentsRequest.getProjection() == null) {
            throw new IllegalArgumentException("A request for Instruments must include a symbol and a projection.");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("instruments")
                .queryParam("symbol", instrumentsRequest.getSymbol())
                .queryParam("projection", instrumentsRequest.getProjection().value());
        return this.callGetApiToMono(defaultUserId, uriBuilder, InstrumentsResponse.class)
                .flatMap(instrumentsResponse -> {
                    if(instrumentsResponse.getInstruments() == null) {
                        return Mono.error(new SymbolNotFoundException("Instruments for '" + instrumentsRequest.getSymbol() + "' not found"));
                    } else {
                        return Mono.just(instrumentsResponse);
                    }
                })
                .flux()
                .flatMap(instrumentsResponse -> Flux.fromIterable(instrumentsResponse.getInstruments()));
    }

    /**
     * fetch instruments by cusip from the Schwab API
     * @param cusip {@literal @}NotNull String
     * @return {@link InstrumentsResponse}
     */
    @Deprecated
    public InstrumentsResponse fetchInstrumentsByCusip(@NotNull String cusip) {
        Instrument instrument = this.fetchInstrumentByCusipToMono(cusip).block();
        InstrumentsResponse instrumentsResponse = new InstrumentsResponse();
        if(instrument != null) {
            instrumentsResponse.setInstruments(List.of(instrument));
        }
        return instrumentsResponse;
    }

    /**
     * reactively fetch instruments by cusip from the Schwab API
     * @param cusip {@literal @}NotNull String
     * @return {@link Mono}{@literal <}{@link Instrument}{@literal >}
     */
    public Mono<Instrument> fetchInstrumentByCusipToMono(@NotNull String cusip) {
        log.info("Fetch Instrument by cusip [{}]", cusip);

        if (cusip.isEmpty()) {
            throw new IllegalArgumentException("Cusip is required");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
            .pathSegment("instruments", cusip);
        return this.callGetApiToMono(defaultUserId, uriBuilder, InstrumentsResponse.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SymbolNotFoundException("Instrument for cusip '" + cusip + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(instrumentResponse -> {
                    if(instrumentResponse.getInstruments() == null || instrumentResponse.getInstruments().isEmpty()) {
                        return Mono.error(new SymbolNotFoundException("Instrument for cusip '" + cusip + "' not found"));
                    } else {
                        return Mono.just(instrumentResponse.getInstruments().get(0));
                    }
                });
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .pathSegment(schwabMarketDataPath, schwabApiVersion);
    }
    
    /**
     * Available markets for requesting hours
     */
    public enum Market {
        /**
         * equity
         */
        EQUITY("equity"),
        /**
         * option
         */
        OPTION("option"),
        /**
         * bond
         */
        @SuppressWarnings("unused")
        BOND("bond"),
        /**
         * future
         */
        @SuppressWarnings("unused")
        FUTURE("future"),
        /**
         * forex
         */
        @SuppressWarnings("unused")
        FOREX("forex");

        private final String value;
        private final static Map<String, Market> CONSTANTS = new HashMap<>();

        static {
            for (Market c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Market(String value) {
            this.value = value;
        }

        /**
         * get the value to be used by the market API
         * @return String
         */
        public String value() {
            return this.value;
        }

        /**
         * convert a string into an enum
         * @param value String
         * @return Market enum
         */
        public static Market fromValue(String value) {
            Market constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
