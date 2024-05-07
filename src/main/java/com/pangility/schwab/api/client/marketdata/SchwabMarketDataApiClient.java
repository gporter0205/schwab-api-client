package com.pangility.schwab.api.client.marketdata;

import com.pangility.schwab.api.client.common.SchwabWebClient;
import com.pangility.schwab.api.client.marketdata.deserializers.MarketsHashMap;
import com.pangility.schwab.api.client.marketdata.deserializers.QuoteResponseHashMap;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainRequest;
import com.pangility.schwab.api.client.marketdata.model.chains.OptionChainResponse;
import com.pangility.schwab.api.client.marketdata.model.expirationchain.ExpirationChainResponse;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest;
import com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsResponse;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversRequest;
import com.pangility.schwab.api.client.marketdata.model.movers.MoversResponse;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.Candle;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryRequest;
import com.pangility.schwab.api.client.marketdata.model.pricehistory.PriceHistoryResponse;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Main client for interacting with the Schwab Market Data API.
 * Use {@literal @}Autowire to create the component in any class annotated with
 * {@literal @}EnableSchwabMarketDataApi or {@literal @}EnableSchwabApi
 */
@Service
@ConditionalOnResource(resources = {"classpath:schwabapiclient.properties"})
@Slf4j
public class SchwabMarketDataApiClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    @Value("${schwab-api.apiVersion}")
    private String schwabApiVersion;
    @Value("${schwab-api.marketDataPath}")
    private String schwabMarketDataPath;

    @Autowired
    private SchwabOauth2Controller schwabOauth2Controller;
    @Autowired
    private SchwabWebClient schwabWebClient;

    private String defaultUserId = null;

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull SchwabAccount schwabAccount) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), null);
    }

    /**
     * Initialize the client controller
     * @param schwabAccount {@link SchwabAccount}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull SchwabAccount schwabAccount, SchwabTokenHandler tokenHandler) {
        this.init(schwabAccount.getUserId(), Collections.singletonList(schwabAccount), tokenHandler);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    @SuppressWarnings("unused")
    public void init(@NotNull String defaultUserId, @NotNull List<SchwabAccount> schwabAccounts) {
        this.init(defaultUserId, schwabAccounts, null);
    }

    /**
     * Initialize the client controller
     * @param defaultUserId String
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     * @param tokenHandler {@link SchwabTokenHandler}
     */
    public void init(@NotNull String defaultUserId, @NotNull List<SchwabAccount> schwabAccounts, SchwabTokenHandler tokenHandler) {
        this.defaultUserId = defaultUserId;
        schwabOauth2Controller.init(schwabAccounts, tokenHandler);
    }

    /**
     * determine if the controller has been initialized
     * @return Boolean
     */
    @SuppressWarnings("unused")
    public Boolean isInitialized() {
        return this.defaultUserId != null && schwabOauth2Controller.isInitialized();
    }

    /**
     * fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link QuoteResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
    public QuoteResponse fetchQuote(@NotNull String symbol)
            throws SymbolNotFoundException {
        return fetchQuote(symbol, null);
    }

    /**
     * fetch a quote from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @param fields String (quote, fundamental or all)
     * @return {@link QuoteResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
    public QuoteResponse fetchQuote(@NotNull String symbol,
                                    String fields)
            throws SymbolNotFoundException {
        log.info("Fetch Quote for: {}", symbol);
        QuoteResponse quoteResponse = null;

        if(fields == null || fields.isEmpty()) {
            fields = "all";
        }

        if (symbol.length() > 0) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, symbol.toUpperCase(), "quotes")
                    .queryParam("fields", fields);
            QuoteResponseHashMap response = this.callGetAPI(uriBuilder, QuoteResponseHashMap.class);
            if (response != null && response.quoteMap.size() > 0 && response.quoteMap.containsKey(symbol)) {
                quoteResponse = response.quoteMap.get(symbol);
            } else {
                throw new SymbolNotFoundException("'" + symbol + "' not found");
            }
        }
        return quoteResponse;
    }

    /**
     * fetch a list of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
    public List<QuoteResponse> fetchQuotes(@NotNull List<String> symbols)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, null);
    }

    /**
     * fetch a list of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
    public List<QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                           String fields)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, fields, null);
    }

    /**
     * fetch a list of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @param indicative Boolean (include indicative symbol quotes for all ETF symbols in request)
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
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


    /**
     * fetch an option chain from the Schwab API
     * @param chainRequest {@literal @}NotNull {@link OptionChainRequest}
     * @return {@link OptionChainResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
    public OptionChainResponse fetchOptionChain(@NotNull OptionChainRequest chainRequest)
            throws SymbolNotFoundException {
        log.info("Fetch option chain for: {}", chainRequest);

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

    /**
     * fetch an expiration chain from the Schwab API
     * @param symbol {@literal @}NotNull String
     * @return {@link ExpirationChainResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
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

    /**
     * fetch the price history from the Schwab API
     * @param priceHistReq {@literal @}NotNull {@link PriceHistoryRequest}
     * @return {@link PriceHistoryResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
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

    /**
     * fetch the movers from the Schwab API
     * @param moversRequest {@literal @}NotNull {@link MoversRequest}
     * @return {@link MoversResponse}
     * @throws SymbolNotFoundException API did not find the index symbol
     */
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

    /**
     * fetch the market hours for today from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @return {@link List}{@literal <}{@link Hours}{@literal >}
     * @throws MarketNotFoundException API did not find the market
     */
    @SuppressWarnings("unused")
    public List<Hours> fetchMarket(@NotNull Market market)
            throws MarketNotFoundException {
        return fetchMarket(market, null);
    }

    /**
     * fetch the market hours from the Schwab API
     * @param market {@literal @}NotNull {@link Market}
     * @param date LocalDate
     * @return {@link List}{@literal <}{@link Hours}{@literal >}
     * @throws MarketNotFoundException API did not find the market
     */
    public List<Hours> fetchMarket(@NotNull Market market,
                                   LocalDate date)
            throws MarketNotFoundException {
        return this.fetchMarkets(Collections.singletonList(market), date);
    }

    /**
     * fetch the market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @return {@link List}{@literal <}{@link Hours}{@literal >}
     * @throws MarketNotFoundException API did not find the market
     */
    public List<Hours> fetchMarkets(@NotNull List<Market> markets)
            throws MarketNotFoundException {
        return fetchMarkets(markets, null);
    }

    /**
     * fetch the market hours from the Schwab API
     * @param markets {@literal @}NotNull {@link List}{@literal <}{@link Market}{@literal >}
     * @param date LocalDate
     * @return {@link List}{@literal <}{@link Hours}{@literal >}
     * @throws MarketNotFoundException API did not find the market
     */
    public List<Hours> fetchMarkets(@NotNull List<Market> markets,
                                    LocalDate date)
            throws MarketNotFoundException {
        log.info("Fetch Markets for: {}", markets);
        List<Hours> ret = null;

        if (markets.size() > 0) {
            String marketsString = getMarketsCsv(markets);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "markets")
                    .queryParam("markets", marketsString);
            if (date != null) {
                uriBuilder.queryParam("date", date.format(DateTimeFormatter.ISO_DATE));
            }
            MarketsHashMap marketsMap = this.callGetAPI(uriBuilder, MarketsHashMap.class);
            if (marketsMap != null && marketsMap.map != null && marketsMap.map.size() > 0) {
                for(String product : marketsMap.map.keySet()) {
                    HashMap<String, Hours> productMap = marketsMap.map.get(product);
                    if(productMap != null && productMap.size() > 0) {
                        for(String productName : productMap.keySet()) {
                            Hours hours = productMap.get(productName);
                            if(ret == null) {
                                ret = new ArrayList<>();
                            }
                            ret.add(hours);
                        }
                    }
                }
            } else {
                throw new MarketNotFoundException("Market Hours for '" + markets + "' not found");
            }
        } else {
            throw new IllegalArgumentException("At least one market is required to fetch Market Hours.");
        }
        return ret;
    }

    /**
     * fetch instruments from the Schwab API
     * @param instrumentsRequest {@literal @}NotNull {@link InstrumentsRequest}
     * @return {@link InstrumentsResponse}
     * @throws SymbolNotFoundException API did not find the symbol
     */
    public InstrumentsResponse fetchInstruments(@NotNull InstrumentsRequest instrumentsRequest)
            throws SymbolNotFoundException {
        log.info("Fetch Instruments for: {}", instrumentsRequest);
        InstrumentsResponse response;

        if (instrumentsRequest.getSymbol() != null && instrumentsRequest.getProjection() != null) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "instruments")
                    .queryParam("symbol", instrumentsRequest.getSymbol())
                    .queryParam("projection", instrumentsRequest.getProjection().value());
            response = this.callGetAPI(uriBuilder, InstrumentsResponse.class);
            if (response == null || response.getInstruments() == null || response.getInstruments().size() == 0) {
                throw new SymbolNotFoundException("Instruments for '" + instrumentsRequest.getSymbol() + "' not found");
            }
        } else {
            throw new IllegalArgumentException("A request for Instruments must include a symbol and a projection.");
        }
        return response;
    }

    /**
     * fetch instruments by cusip from the Schwab API
     * @param cusip {@literal @}NotNull String
     * @return {@link InstrumentsResponse}
     * @throws SymbolNotFoundException API did not find the cusip
     */
    public InstrumentsResponse fetchInstrumentsByCusip(@NotNull String cusip)
            throws SymbolNotFoundException {
        log.info("Fetch Instruments by cusip for: {}", cusip);
        InstrumentsResponse response;

        if (!cusip.isEmpty()) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "instruments", cusip);
            response = this.callGetAPI(uriBuilder, InstrumentsResponse.class);
            if (response == null || response.getInstruments() == null || response.getInstruments().size() == 0) {
                throw new SymbolNotFoundException("Instrument for cusip '" + cusip + "' not found");
            }
        } else {
            throw new IllegalArgumentException("A request for Instruments by cusip must include a cusip.");
        }
        return response;
    }

    private <T> T callGetAPI(@NotNull UriComponentsBuilder uriComponentsBuilder,
                             @NotNull Class<T> clazz) {

        T ret = null;

        //Validate refresh token
        schwabOauth2Controller.validateRefreshToken(defaultUserId);

        String accessToken = schwabOauth2Controller.getAccessToken(defaultUserId);
        if(accessToken != null) {
            URI uri = uriComponentsBuilder
                    .scheme("https")
                    .host(schwabTargetUrl)
                    .build()
                    .toUri();
            ResponseEntity<T> retMono = schwabWebClient.getSchwabWebClient()
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

    /**
     * Available markets for requesting hours
     */
    public enum Market {
        /**
         * equity
         */
        equity,
        /**
         * option
         */
        option,
        /**
         * bond
         */
        @SuppressWarnings("unused")
        bond,
        /**
         * future
         */
        @SuppressWarnings("unused")
        future,
        /**
         * forex
         */
        @SuppressWarnings("unused")
        forex
    }

    private String getMarketsCsv(@NotNull List<Market> marketList) {
        StringBuilder csv = new StringBuilder();
        for(Market market : marketList) {
            if(csv.length() > 0) {
                csv.append(",");
            }
            csv.append(market);
        }
        return csv.toString();
    }
}
