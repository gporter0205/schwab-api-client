package com.pangility.schwab.api.client.accountsandtrading.model.account;

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
 * Position
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
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
  private BigDecimal longOpenProfitLoss;
  private BigDecimal taxLotAverageLongPrice;
  private BigDecimal maintenanceRequirement;
  private BigDecimal previousSessionLongQuantity;
  private BigDecimal averageLongPrice;
  private BigDecimal currentDayCost;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
