package com.pangility.schwab.api.client.marketdata.model.quotes.option;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pangility.schwab.api.client.marketdata.model.quotes.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Option Quote
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteOption extends Quote {
  @JsonProperty("52WeekHigh")
  private BigDecimal _52WeekHigh;
  @JsonProperty("52WeekLow")
  private BigDecimal _52WeekLow;
  private BigDecimal askPrice;
  private Long askSize;
  private BigDecimal bidPrice;
  private Long bidSize;
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
  private BigDecimal markPercentChange;
  private BigDecimal moneyIntrinsicValue;
  private BigDecimal netPercentageChange;
  private BigDecimal openInterest;
  private BigDecimal openPrice;
  private Long quoteTime;
  private BigDecimal rho;
  private BigDecimal theoreticalOptionValue;
  private BigDecimal theta;
  private BigDecimal timeValue;
  private BigDecimal underlyingPrice;
  private BigDecimal vega;
  private BigDecimal volatility;
}