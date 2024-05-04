package com.pangility.schwab.api.client.marketdata.model.chains;

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
 * Underlying
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Underlying {
  private BigDecimal ask;
  private Long askSize;
  private BigDecimal bid;
  private Long bidSize;
  private BigDecimal change;
  private BigDecimal close;
  private Boolean delayed;
  private String description;
  private String exchangeName;
  private BigDecimal fiftyTwoWeekHigh;
  private BigDecimal fiftyTwoWeekLow;
  private BigDecimal highPrice;
  private BigDecimal last;
  private BigDecimal lowPrice;
  private BigDecimal mark;
  private BigDecimal markChange;
  private BigDecimal markPercentChange;
  private BigDecimal openPrice;
  private BigDecimal percentChange;
  private Long quoteTime;
  private String symbol;
  private Long totalVolume;
  private Long tradeTime;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
