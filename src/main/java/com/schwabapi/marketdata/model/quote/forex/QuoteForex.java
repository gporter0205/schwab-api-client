package com.schwabapi.marketdata.model.quote.forex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schwabapi.marketdata.model.quote.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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
