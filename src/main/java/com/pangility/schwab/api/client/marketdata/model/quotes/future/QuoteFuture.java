package com.pangility.schwab.api.client.marketdata.model.quotes.future;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Quote for Futures
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteFuture extends Quote {
  private String askMICId;
  private BigDecimal askPrice;
  private Long askSize;
  private Long askTime;
  private String bidMICId;
  private BigDecimal bidPrice;
  private Long bidSize;
  private Long bidTime;
  private BigDecimal futurePercentChange;
  private BigDecimal highPrice;
  private String lastMICId;
  private BigDecimal lastPrice;
  private Long lastSize;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal openInterest;
  private BigDecimal openPrice;
  private Long quoteTime;
  private Boolean quotedInSession;
  private Long settleTime;
  private BigDecimal tick;
  private BigDecimal tickAmount;
}
