package com.pangility.schwab.api.client.marketdata.model.quotes.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * ExtendedMarket
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExtendedMarket {
  private BigDecimal askPrice;
  private Long askSize;
  private BigDecimal bidPrice;
  private Long bidSize;
  private BigDecimal lastPrice;
  private Long lastSize;
  private BigDecimal mark;
  private Long quoteTime;
  private Long totalVolume;
  private Long tradeTime;
}