package com.pangility.schwab.api.client.marketdata.model.quotes.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * ReferenceEquity
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceEquity extends Reference {
  private String fsiDesc;
  private Integer htbQuantity;
  private BigDecimal htbRate;
  private Boolean isHardToBorrow;
  private Boolean isShortable;
  private String otcMarketTier;
}