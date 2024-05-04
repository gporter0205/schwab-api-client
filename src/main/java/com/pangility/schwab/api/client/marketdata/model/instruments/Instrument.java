package com.pangility.schwab.api.client.marketdata.model.instruments;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Instrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
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

  /**
   * Instrument
   */
  @Getter
  @Setter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Bond extends Instrument {
    private String bondFactor;
    private String bondMultiplier;
    private BigDecimal bondPrice;
  }

  /**
   * Instrument AssetType
   */
  public enum AssetType {
    /**
     * Bond
     */
    BOND,
    /**
     * Equity
     */
    EQUITY,
    /**
     * ETF
     */
    ETF,
    /**
     * Extended
     */
    EXTENDED,
    /**
     * Forex
     */
    FOREX,
    /**
     * Future
     */
    FUTURE,
    /**
     * Future Option
     */
    FUTURE_OPTION,
    /**
     * Fundamental
     */
    FUNDAMENTAL,
    /**
     * Index
     */
    INDEX,
    /**
     * Indicator
     */
    INDICATOR,
    /**
     * Mutual Fund
     */
    MUTUAL_FUND,
    /**
     * Option
     */
    OPTION,
    /**
     * Unknown
     */
    UNKNOWN
  }
}
