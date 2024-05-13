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

  public enum Status {
    VALID, INVALID, PENDING, UNKNOWN
  }

  public enum Type {
    TRADE,
    RECEIVE_AND_DELIVER,
    DIVIDEND_OR_INTEREST,
    ACH_RECEIPT,
    ACH_DISBURSEMENT,
    CASH_RECEIPT,
    CASH_DISBURSEMENT,
    ELECTRONIC_FUND,
    WIRE_OUT,
    WIRE_IN,
    JOURNAL,
    MEMORANDUM,
    MARGIN_CALL,
    MONEY_MARKET,
    SMA_ADJUSTMENT
  }

  public enum ActivityType {
    ACTIVITY_CORRECTION, EXECUTION, ORDER_ACTION, TRANSFER, UNKNOWN
  }
}