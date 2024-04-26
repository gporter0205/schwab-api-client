package com.schwabapi.model.quote;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.schwabapi.model.AssetMainType;
import com.schwabapi.model.quote.equity.*;
import com.schwabapi.model.quote.forex.QuoteForex;
import com.schwabapi.model.quote.forex.ReferenceForex;
import com.schwabapi.model.quote.future.QuoteFuture;
import com.schwabapi.model.quote.future.ReferenceFuture;
import com.schwabapi.model.quote.futureOption.QuoteFutureOption;
import com.schwabapi.model.quote.futureOption.ReferenceFutureOption;
import com.schwabapi.model.quote.index.QuoteIndex;
import com.schwabapi.model.quote.index.ReferenceIndex;
import com.schwabapi.model.quote.mutualfund.MutualFundAssetSubType;
import com.schwabapi.model.quote.mutualfund.QuoteMutualFund;
import com.schwabapi.model.quote.mutualfund.ReferenceMutualFund;
import com.schwabapi.model.quote.option.QuoteOption;
import com.schwabapi.model.quote.option.ReferenceOption;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

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

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class OptionResponse extends QuoteResponse {
    private QuoteOption quote;
    private ReferenceOption reference;
  }

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class ForexResponse extends QuoteResponse {
    private QuoteForex quote;
    private ReferenceForex reference;
  }

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class FutureResponse extends QuoteResponse {
    private QuoteFuture quote;
    private ReferenceFuture reference;
  }

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class FutureOptionResponse extends QuoteResponse {
    private QuoteFutureOption quote;
    private ReferenceFutureOption reference;
  }

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class IndexResponse extends QuoteResponse {
    private QuoteIndex quote;
    private ReferenceIndex reference;
  }

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