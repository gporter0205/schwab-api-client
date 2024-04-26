package com.schwabapi.model.quote.option;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schwabapi.model.quote.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteOption extends Quote {
  @JsonProperty("52WkHigh")
  private BigDecimal _52WkHigh;
  @JsonProperty("52WkLow")
  private BigDecimal askPrice;
  private Long askSize;
  private BigDecimal bidPrice;
  private Long bidSize;
  private BigDecimal closePrice;
  private BigDecimal delta;
  private BigDecimal gamma;
  private BigDecimal highPrice;
  private BigDecimal indAskPrice;
  private BigDecimal indBidPrice;
  private Long indQuoteTime;
  private BigDecimal impliedYield;
  private BigDecimal lastPrice;
  private Long lastSize;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal markChange;
  private BigDecimal markPercentageChange;
  private BigDecimal moneyIntrinsicValue;
  private BigDecimal netChange;
  private BigDecimal netPercentageChange;
  private BigDecimal openInterest;
  private BigDecimal openPrice;
  private Long quoteTime;
  private BigDecimal rho;
  private String securityStatus;
  private BigDecimal theoreticalOptionValue;
  private BigDecimal theta;
  private BigDecimal timeValue;
  private Long totalVolume;
  private Long tradeTime;
  private BigDecimal underlyingPrice;
  private BigDecimal vega;
  private BigDecimal volatility;
}