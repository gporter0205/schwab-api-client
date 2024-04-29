package com.schwabapi.marketdata.model.quote.futureOption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.schwabapi.marketdata.model.quote.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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