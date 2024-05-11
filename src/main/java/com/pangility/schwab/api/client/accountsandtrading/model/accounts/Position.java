package com.pangility.schwab.api.client.accountsandtrading.model.accounts;

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
public class Position {
  private BigDecimal shortQuantity;
  private BigDecimal averagePrice;
  private BigDecimal currentDayProfitLoss;
  private BigDecimal currentDayProfitLossPercentage;
  private BigDecimal longQuantity;
  private BigDecimal settledLongQuantity;
  private BigDecimal settledShortQuantity;
  private BigDecimal agedQuantity;
  private Instrument instrument;
  private BigDecimal marketValue;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
