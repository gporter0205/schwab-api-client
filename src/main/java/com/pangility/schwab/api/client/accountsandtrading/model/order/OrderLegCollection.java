package com.pangility.schwab.api.client.accountsandtrading.model.order;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.accountsandtrading.model.account.Instrument;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

  public enum Instruction {
    BUY,
    SELL,
    BUY_TO_COVER,
    SELL_SHORT,
    BUY_TO_OPEN,
    BUY_TO_CLOSE,
    SELL_TO_OPEN,
    SELL_TO_CLOSE,
    EXCHANGE
  }

  public enum OrderLegType {
    CASH_EQUIVALENT,
    CURRENCY,
    EQUITY,
    FIXED_INCOME,
    INDEX,
    MUTUAL_FUND,
    OPTION,
  }

  public enum PositionEffect {
    OPENING,
    CLOSING,
    AUTOMATIC
  }

  public enum QuantityType {
    ALL_SHARES,
    DOLLARS,
    SHARES
  }
}
