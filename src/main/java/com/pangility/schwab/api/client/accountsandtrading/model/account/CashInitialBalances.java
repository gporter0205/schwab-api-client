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
 * Initial Balances of a Cash Account
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashInitialBalances {
  private BigDecimal accruedInterest;
  private BigDecimal cashAvailableForTrading;
  private BigDecimal cashAvailableForWithdrawal;
  private BigDecimal cashBalance;
  private BigDecimal bondValue;
  private BigDecimal cashReceipts;
  private BigDecimal liquidationValue;
  private BigDecimal longOptionMarketValue;
  private BigDecimal longStockValue;
  private BigDecimal moneyMarketFund;
  private BigDecimal mutualFundValue;
  private BigDecimal shortOptionMarketValue;
  private BigDecimal shortStockValue;
  private Boolean isInCall;
  private BigDecimal unsettledCash;
  private BigDecimal cashDebitCallValue;
  private BigDecimal pendingDeposits;
  private BigDecimal accountValue;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
