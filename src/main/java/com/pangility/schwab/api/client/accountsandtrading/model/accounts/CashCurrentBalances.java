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
 * Current Balances of a Cash Account
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashCurrentBalances {
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
  private BigDecimal cashAvailableForTrading;
  private BigDecimal cashAvailableForWithdrawal;
  private BigDecimal cashCall;
  private BigDecimal longNonMarginableMarketValue;
  private BigDecimal totalCash;
  private BigDecimal shortOptionMarketValue;
  private BigDecimal mutualFundValue;
  private BigDecimal bondValue;
  private BigDecimal cashDebitCallValue;
  private BigDecimal unsettledCash;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
