package com.pangility.schwab.api.client.accountsandtrading.model.order;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.Instrument;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * OrderLegCollection
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderLegCollection {
  private OrderLegType orderLegType;
  private Long legId;
  private Instruction instruction;
  private PositionEffect positionEffect;
  private BigDecimal quantity;
  private QuantityType quantityType;
  private Instrument instrument;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  /**
   * Instruction
   */
  public enum Instruction {
    /**
     * Buy
     */
    BUY,
    /**
     * Sell
     */
    SELL,
    /**
     * Buy to Cover
     */
    BUY_TO_COVER,
    /**
     * Sell Short
     */
    SELL_SHORT,
    /**
     * Buy to Open
     */
    BUY_TO_OPEN,
    /**
     * Buy to Close
     */
    BUY_TO_CLOSE,
    /**
     * Sell to Open
     */
    SELL_TO_OPEN,
    /**
     * Sell to Close
     */
    SELL_TO_CLOSE,
    /**
     * Exchange
     */
    EXCHANGE
  }

  /**
   * Order Leg Type
   */
  public enum OrderLegType {
    /**
     * Cash Equivalent
     */
    CASH_EQUIVALENT,
    /**
     * Currency
     */
    CURRENCY,
    /**
     * Equity
     */
    EQUITY,
    /**
     * Fixed Income
     */
    FIXED_INCOME,
    /**
     * Index
     */
    INDEX,
    /**
     * Mutual Fund
     */
    MUTUAL_FUND,
    /**
     * Option
     */
    OPTION,
  }

  /**
   * Position Effect
   */
  public enum PositionEffect {
    /**
     * Opening
     */
    OPENING,
    /**
     * Closing
     */
    CLOSING,
    /**
     * Automatic
     */
    AUTOMATIC
  }

  /**
   * Quantity Type
   */
  public enum QuantityType {
    /**
     * All Shares
     */
    ALL_SHARES,
    /**
     * Dollars
     */
    DOLLARS,
    /**
     * Shares
     */
    SHARES
  }
}
