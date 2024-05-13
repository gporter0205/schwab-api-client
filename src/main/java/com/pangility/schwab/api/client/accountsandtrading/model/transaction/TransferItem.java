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

  public enum PositionEffect {
    OPENING, CLOSING, AUTOMATIC, UNKNOWN
  }

  public enum FeeType {
    COMMISSION, SEC_FEE, STR_FEE, R_FEE, CDSC_FEE, OPT_REG_FEE, ADDITIONAL_FEE, MISCELLANEOUS_FEE, FUTURES_EXCHANGE_FEE, LOW_PROCEEDS_COMMISSION, BASE_CHARGE, GENERAL_CHARGE, GST_FEE, TAF_FEE, INDEX_OPTION_FEE, UNKNOWN
  }
}