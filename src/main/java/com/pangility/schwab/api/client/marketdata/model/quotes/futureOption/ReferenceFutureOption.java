package com.pangility.schwab.api.client.marketdata.model.quotes.futureOption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * ReferenceFutureOption
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceFutureOption extends Reference {
  private String contractType;
  private BigDecimal multiplier;
  private Long expirationDate;
  private String expirationStyle;
  private BigDecimal strikePrice;
  private String underlying;
}