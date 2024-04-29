package com.schwabapi.marketdata.model.instruments;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Instrument
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "assetType",
        visible = true,
        defaultImpl = Instrument.class
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Instrument.Bond.class, name = "BOND")
})
public class Instrument {
  private String cusip;
  private String symbol;
  private String description;
  private String exchange;
  private AssetType assetType;
  private Fundamental fundamental;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Bond extends Instrument {
    private String bondFactor;
    private String bondMultiplier;
    private BigDecimal bondPrice;
  }
  public enum AssetType {
    EQUITY,
    ETF,
    FOREX,
    FUTURE,
    FUTURE_OPTION,
    INDEX,
    INDICATOR,
    MUTUAL_FUND,
    OPTION,
    UNKNOWN,
    BOND
  }
}
