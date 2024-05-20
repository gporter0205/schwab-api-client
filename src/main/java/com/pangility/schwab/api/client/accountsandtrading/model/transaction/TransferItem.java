package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * TransferItem
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransferItem {
  private TransactionInstrument instrument;
  private BigDecimal amount;
  private BigDecimal cost;
  private BigDecimal price;
  private FeeType feeType;
  private PositionEffect positionEffect;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

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
    AUTOMATIC,
    /**
     * Unknown
     */
    UNKNOWN
  }

  /**
   * Fee Type
   */
  public enum FeeType {
    /**
     * Commission
     */
    COMMISSION,
    /**
     * SEC Fee
     */
    SEC_FEE,
    /**
     * Str Fee
     */
    STR_FEE,
    /**
     * R Fee
     */
    R_FEE,
    /**
     * CDSC Fee
     */
    CDSC_FEE,
    /**
     * Opt Reg Fee
     */
    OPT_REG_FEE,
    /**
     * Additional Fee
     */
    ADDITIONAL_FEE,
    /**
     * Miscellaneous Fee
     */
    MISCELLANEOUS_FEE,
    /**
     * Futures Exchange Fee
     */
    FUTURES_EXCHANGE_FEE,
    /**
     * Low Proceeds Commission
     */
    LOW_PROCEEDS_COMMISSION,
    /**
     * Base Charge
     */
    BASE_CHARGE,
    /**
     * General Charge
     */
    GENERAL_CHARGE,
    /**
     * GST Fee
     */
    GST_FEE,
    /**
     * TAF Fee
     */
    TAF_FEE,
    /**
     * Index Option Fee
     */
    INDEX_OPTION_FEE,
    /**
     * Unknown
     */
    UNKNOWN
  }
}