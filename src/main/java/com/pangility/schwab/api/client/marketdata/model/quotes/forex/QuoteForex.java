package com.pangility.schwab.api.client.marketdata.model.quotes.forex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pangility.schwab.api.client.marketdata.model.quotes.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * QuoteForex
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteForex extends Quote {
  @JsonProperty("52WkHigh")
  private BigDecimal _52WkHigh;
  @JsonProperty("52WkLow")
  private BigDecimal _52WkLow;
  private BigDecimal askPrice;
  private BigDecimal askSize;
  private BigDecimal bidPrice;
  private BigDecimal bidSize;
  private BigDecimal highPrice;
  private BigDecimal lastPrice;
  private BigDecimal lastSize;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal openPrice;
  private Long quoteTime;
  private BigDecimal tick;
  private BigDecimal tickAmount;
}
