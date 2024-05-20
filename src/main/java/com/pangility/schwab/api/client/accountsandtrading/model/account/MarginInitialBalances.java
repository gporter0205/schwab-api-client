package com.pangility.schwab.api.client.accountsandtrading.model.account;

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
 * Initial Balances of a Margin Account
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MarginInitialBalances {
  private BigDecimal accruedInterest;
  private BigDecimal availableFundsNonMarginableTrade;
  private BigDecimal bondValue;
  private BigDecimal buyingPower;
  private BigDecimal cashBalance;
  private BigDecimal cashAvailableForTrading;
  private BigDecimal cashReceipts;
  private BigDecimal dayTradingBuyingPower;
  private BigDecimal dayTradingBuyingPowerCall;
  private BigDecimal dayTradingEquityCall;
  private BigDecimal equity;
  private BigDecimal equityPercentage;
  private BigDecimal liquidationValue;
  private BigDecimal longMarginValue;
  private BigDecimal longOptionMarketValue;
  private BigDecimal longStockValue;
  private BigDecimal maintenanceCall;
  private BigDecimal maintenanceRequirement;
  private BigDecimal margin;
  private BigDecimal marginEquity;
  private BigDecimal moneyMarketFund;
  private BigDecimal mutualFundValue;
  private BigDecimal regTCall;
  private BigDecimal shortMarginValue;
  private BigDecimal shortOptionMarketValue;
  private BigDecimal shortStockValue;
  private BigDecimal totalCash;
  private Boolean isInCall;
  private BigDecimal unsettledCash;
  private BigDecimal pendingDeposits;
  private BigDecimal marginBalance;
  private BigDecimal shortBalance;
  private BigDecimal accountValue;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}