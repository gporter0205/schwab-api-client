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


/**
 * Projected Balances of a Margin Account
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MarginProjectedBalances {
  private BigDecimal accruedInterest;
  private BigDecimal availableFunds;
  private BigDecimal availableFundsNonMarginableTrade;
  private BigDecimal buyingPower;
  private BigDecimal bondValue;
  private BigDecimal buyingPowerNonMarginableTrade;
  private BigDecimal cashBalance;
  private BigDecimal cashReceipts;
  private BigDecimal shortMarketValue;
  private BigDecimal dayTradingBuyingPower;
  private BigDecimal dayTradingBuyingPowerCall;
  private BigDecimal equity;
  private BigDecimal equityPercentage;
  private Boolean isInCall;
  private BigDecimal liquidationValue;
  private BigDecimal longOptionMarketValue;
  private BigDecimal longMarketValue;
  private BigDecimal longMarginValue;
  private BigDecimal maintenanceCall;
  private BigDecimal maintenanceRequirement;
  private BigDecimal marginBalance;
  private BigDecimal moneyMarketFund;
  private BigDecimal mutualFundValue;
  private BigDecimal pendingDeposits;
  private BigDecimal optionBuyingPower;
  private BigDecimal regTCall;
  private BigDecimal savings;
  private BigDecimal shortBalance;
  private BigDecimal shortMarginValue;
  private BigDecimal shortOptionMarketValue;
  private BigDecimal sma;
  private BigDecimal stockBuyingPower;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
