package com.pangility.schwab.api.client.marketdata;

import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
            Map<String, QuoteResponse> response = this.callGetAPIAsMap(uriBuilder, new ParameterizedTypeReference<>() {});
            if (response != null && response.size() > 0 && response.containsKey(symbol)) {
                quoteResponse = response.get(symbol);
            } else {
                throw new SymbolNotFoundException("'" + symbol + "' not found");
            }
        }
        return quoteResponse;
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, null);
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                           String fields)
            throws SymbolNotFoundException {
        return fetchQuotes(symbols, fields, null);
    }

    /**
     * fetch a map of quotes from the Schwab API
     * @param symbols {@literal @}NotNull List{@literal <}String{@literal >}
     * @param fields String (quote, fundamental or all)
     * @param indicative Boolean (include indicative symbol quotes for all ETF symbols in request)
     * @return {@link List}{@literal <}{@link QuoteResponse}{@literal >}
     * @throws SymbolNotFoundException API did not find the one or more symbols
     */
    public Map<String, QuoteResponse> fetchQuotes(@NotNull List<String> symbols,
                                                  String fields,
                                                  Boolean indicative)
            throws SymbolNotFoundException {
        log.info("Fetch Quotes for: {}", symbols);

        if(fields == null || fields.isEmpty()) {
            fields = "all";
        }
        Map<String, QuoteResponse> response = null;
        if (symbols.size() > 0) {
            String symbolsParam = String.join(",", symbols).toUpperCase();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "quotes")
                    .queryParam("symbols", symbolsParam)
                    .queryParam("fields", fields);
            if(indicative != null) {
                uriBuilder.queryParam("indicative", indicative);
            }
            response = this.callGetAPIAsMap(uriBuilder, new ParameterizedTypeReference<>() {});
            if(response == null || response.size() == 0) {
                throw new SymbolNotFoundException("'" + symbols + "' not found");
            }
        }
        return response;
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
    public Map<String, Map<String, Hours>> fetchMarket(@NotNull Market market)
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
    public Map<String, Map<String, Hours>> fetchMarket(@NotNull Market market,
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
    public Map<String, Map<String, Hours>> fetchMarkets(@NotNull List<Market> markets)
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
    public Map<String, Map<String, Hours>> fetchMarkets(@NotNull List<Market> markets,
                                    LocalDate date)
            throws MarketNotFoundException {
        log.info("Fetch Markets for: {}", markets);
        Map<String, Map<String, Hours>> marketsMap;

        if (markets.size() > 0) {
            String marketsString = String.join(",", markets.stream().map(Enum::name).toArray(String[]::new));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .pathSegment(schwabMarketDataPath, schwabApiVersion, "markets")
                    .queryParam("markets", marketsString);
            if (date != null) {
                uriBuilder.queryParam("date", date.format(DateTimeFormatter.ISO_DATE));
            }
            marketsMap = this.callGetAPIAsMap(uriBuilder, new ParameterizedTypeReference<>() {});
            if (marketsMap == null || marketsMap.size() == 0) {
                throw new MarketNotFoundException("Market Hours for '" + markets + "' not found");
            }
        } else {
            throw new IllegalArgumentException("At least one market is required to fetch Market Hours.");
        }
        return marketsMap;
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

    /**
     * Available markets for requesting hours
     */
    /* TODO standardize these and add a value to get the info to send to the API */
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
}
