package com.pangility.schwab.api.client.marketdata.model.quote.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pangility.schwab.api.client.marketdata.model.quote.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuoteEquity extends Quote {
  @JsonProperty("52WeekHigh")
  private BigDecimal _52WeekHigh;
  @JsonProperty("52WeekLow")
  private BigDecimal _52WeekLow;
  private String askMICId;
  private BigDecimal askPrice;
  private Long askSize;
  private Long askTime;
  private String bidMICId;
  private BigDecimal bidPrice;
  private Long bidSize;
  private Long bidTime;
  private BigDecimal highPrice;
  private String lastMICId;
  private BigDecimal lastPrice;
  private Long lastSize;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal markChange;
  private BigDecimal markPercentChange;
  private BigDecimal openPrice;
  private BigDecimal postMarketChange;
  private BigDecimal postMarketPercentChange;
  private Long quoteTime;
  private BigDecimal volatility;
}