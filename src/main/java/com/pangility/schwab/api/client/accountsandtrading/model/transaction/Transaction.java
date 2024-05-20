package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Transactions for a specific account.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Transaction {
  private Long activityId;
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime time;
  private UserDetails user;
  private String description;
  private String accountNumber;
  private Type type;
  private Status status;
  private String subAccount;
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime tradeDate;
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime settlementDate;
  private Long positionId;
  private Long orderId;
  private BigDecimal netAmount;
  private ActivityType activityType;
  private List<TransferItem> transferItems;

  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  /**
   * Transaction Status
   */
  public enum Status {
    /**
     * Valid
     */
    VALID,
    /**
     * Invalid
     */
    INVALID,
    /**
     * Pending
     */
    PENDING,
    /**
     * Unknown
     */
    UNKNOWN
  }

  /**
   * Transaction Type
   */
  public enum Type {
    /**
     * Trade
     */
    TRADE,
    /**
     * Receive and Deliver
     */
    RECEIVE_AND_DELIVER,
    /**
     * Dividend or Interest
     */
    DIVIDEND_OR_INTEREST,
    /**
     * ACH Receipt
     */
    ACH_RECEIPT,
    /**
     * ACH Disbursement
     */
    ACH_DISBURSEMENT,
    /**
     * Cash Receipt
     */
    CASH_RECEIPT,
    /**
     * Cash Disbursement
     */
    CASH_DISBURSEMENT,
    /**
     * Electronic Fund
     */
    ELECTRONIC_FUND,
    /**
     * Wire Out
     */
    WIRE_OUT,
    /**
     * Wire In
     */
    WIRE_IN,
    /**
     * Journal
     */
    JOURNAL,
    /**
     * Memorandum
     */
    MEMORANDUM,
    /**
     * Margin Call
     */
    MARGIN_CALL,
    /**
     * Money Market
     */
    MONEY_MARKET,
    /**
     * SMA Adjustment
     */
    SMA_ADJUSTMENT
  }

  /**
   * Transaction Activity Type
   */
  public enum ActivityType {
    /**
     * Activity Correction
     */
    ACTIVITY_CORRECTION,
    /**
     * Execution
     */
    EXECUTION,
    /**
     * Order Action
     */
    ORDER_ACTION,
    /**
     * Transfer
     */
    TRANSFER,
    /**
     * Unknown
     */
    UNKNOWN
  }
}