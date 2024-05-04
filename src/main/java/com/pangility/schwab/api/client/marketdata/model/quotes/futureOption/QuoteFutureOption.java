package com.pangility.schwab.api.client.marketdata.model.quotes.futureOption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Quote for Future Options
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteFutureOption extends Quote {
  private String askMICId;
  private BigDecimal askPrice;
  private Long askSize;
  private String bidMICId;
  private BigDecimal bidPrice;
  private Long bidSize;
  private BigDecimal highPrice;
  private String lastMICId;
  private BigDecimal lastPrice;
  private Long lastSize;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal markChange;
  private BigDecimal openInterest;
  private BigDecimal openPrice;
  private Long quoteTime;
  private BigDecimal settlementPrice;
  private BigDecimal tick;
  private BigDecimal tickAmount;
}