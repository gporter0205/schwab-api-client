package com.schwabapi.model.quote.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegularMarket {
  private BigDecimal regularMarketLastPrice;
  private Long regularMarketLastSize;
  private BigDecimal regularMarketNetChange;
  private BigDecimal regularMarketPercentChange;
  private Long regularMarketTradeTime;
}