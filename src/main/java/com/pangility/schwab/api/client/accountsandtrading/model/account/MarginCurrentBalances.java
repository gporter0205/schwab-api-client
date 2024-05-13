package com.pangility.schwab.api.client.accountsandtrading.model.account;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Current Balances of a Margin Account
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MarginCurrentBalances implements Serializable {
  private BigDecimal accruedInterest;
  private BigDecimal cashBalance;
  private BigDecimal cashReceipts;
  private BigDecimal longOptionMarketValue;
  private BigDecimal liquidationValue;
  private BigDecimal longMarketValue;
  private BigDecimal moneyMarketFund;
  private BigDecimal savings;
  private BigDecimal shortMarketValue;
  private BigDecimal pendingDeposits;
  private BigDecimal availableFunds;
  private BigDecimal availableFundsNonMarginableTrade;
  private BigDecimal buyingPower;
  private BigDecimal buyingPowerNonMarginableTrade;
  private BigDecimal dayTradingBuyingPower;
  private BigDecimal dayTradingBuyingPowerCall;
  private BigDecimal equity;
  private BigDecimal equityPercentage;
  private BigDecimal longMarginValue;
  private BigDecimal maintenanceCall;
  private BigDecimal maintenanceRequirement;
  private BigDecimal marginBalance;
  private BigDecimal regTCall;
  private BigDecimal shortBalance;
  private BigDecimal shortMarginValue;
  private BigDecimal shortOptionMarketValue;
  private BigDecimal sma;
  private BigDecimal mutualFundValue;
  private BigDecimal bondValue;
  private Boolean isInCall;
  private BigDecimal stockBuyingPower;
  private BigDecimal optionBuyingPower;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
