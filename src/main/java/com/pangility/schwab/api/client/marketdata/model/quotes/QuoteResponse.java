package com.pangility.schwab.api.client.marketdata.model.quotes;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.pangility.schwab.api.client.marketdata.model.AssetMainType;
import com.pangility.schwab.api.client.marketdata.model.quotes.equity.EquityAssetSubType;
import com.pangility.schwab.api.client.marketdata.model.quotes.equity.ExtendedMarket;
import com.pangility.schwab.api.client.marketdata.model.quotes.equity.QuoteEquity;
import com.pangility.schwab.api.client.marketdata.model.quotes.equity.ReferenceEquity;
import com.pangility.schwab.api.client.marketdata.model.quotes.equity.RegularMarket;
import com.pangility.schwab.api.client.marketdata.model.quotes.forex.QuoteForex;
import com.pangility.schwab.api.client.marketdata.model.quotes.forex.ReferenceForex;
import com.pangility.schwab.api.client.marketdata.model.quotes.future.QuoteFuture;
import com.pangility.schwab.api.client.marketdata.model.quotes.future.ReferenceFuture;
import com.pangility.schwab.api.client.marketdata.model.quotes.futureOption.QuoteFutureOption;
import com.pangility.schwab.api.client.marketdata.model.quotes.futureOption.ReferenceFutureOption;
import com.pangility.schwab.api.client.marketdata.model.quotes.index.QuoteIndex;
import com.pangility.schwab.api.client.marketdata.model.quotes.index.ReferenceIndex;
import com.pangility.schwab.api.client.marketdata.model.quotes.mutualfund.MutualFundAssetSubType;
import com.pangility.schwab.api.client.marketdata.model.quotes.mutualfund.QuoteMutualFund;
import com.pangility.schwab.api.client.marketdata.model.quotes.mutualfund.ReferenceMutualFund;
import com.pangility.schwab.api.client.marketdata.model.quotes.option.QuoteOption;
import com.pangility.schwab.api.client.marketdata.model.quotes.option.ReferenceOption;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Object used to receive the response from the Schwab API <em>quotes</em> endpoint
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
    use = Id.NAME,
    property = "assetMainType",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = QuoteResponse.EquityResponse.class, name = "EQUITY"),
    @JsonSubTypes.Type(value = QuoteResponse.OptionResponse.class, name = "OPTION"),
    @JsonSubTypes.Type(value = QuoteResponse.ForexResponse.class, name = "FOREX"),
    @JsonSubTypes.Type(value = QuoteResponse.FutureResponse.class, name = "FUTURE"),
    @JsonSubTypes.Type(value = QuoteResponse.FutureOptionResponse.class, name = "FUTURE_OPTION"),
    @JsonSubTypes.Type(value = QuoteResponse.IndexResponse.class, name = "INDEX"),
    @JsonSubTypes.Type(value = QuoteResponse.MutualFundResponse.class, name = "MUTUAL_FUND")
})
public class QuoteResponse {
  private AssetMainType assetMainType;
  private Boolean realtime;
  private Long ssid;
  private String symbol;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  /**
   * Object used to receive the response for <em>equity</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class EquityResponse extends QuoteResponse {
    private EquityAssetSubType assetSubType;
    private String quoteType;
    private QuoteEquity quote;
    private ReferenceEquity reference;
    private ExtendedMarket extended;
    private Fundamental fundamental;
    private RegularMarket regular;
  }

  /**
   * Object used to receive the response for <em>option</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class OptionResponse extends QuoteResponse {
    private QuoteOption quote;
    private ReferenceOption reference;
  }

  /**
   * Object used to receive the response for <em>forex</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class ForexResponse extends QuoteResponse {
    private QuoteForex quote;
    private ReferenceForex reference;
  }

  /**
   * Object used to receive the response for <em>future</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class FutureResponse extends QuoteResponse {
    private QuoteFuture quote;
    private ReferenceFuture reference;
  }

  /**
   * Object used to receive the response for <em>future option</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class FutureOptionResponse extends QuoteResponse {
    private QuoteFutureOption quote;
    private ReferenceFutureOption reference;
  }

  /**
   * Object used to receive the response for <em>index</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class IndexResponse extends QuoteResponse {
    private QuoteIndex quote;
    private ReferenceIndex reference;
  }

  /**
   * Object used to receive the response for <em>mutual fund</em> quotes from the Schwab API <em>quotes</em> endpoint
   * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class MutualFundResponse extends QuoteResponse {
    private MutualFundAssetSubType assetSubType;
    private Fundamental fundamental;
    private QuoteMutualFund quote;
    private ReferenceMutualFund reference;
  }
}