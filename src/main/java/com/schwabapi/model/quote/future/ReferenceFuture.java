package com.schwabapi.model.quote.future;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.schwabapi.model.quote.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceFuture extends Reference {
  private String futureActiveSymbol;
  private Long futureExpirationDate;
  private Boolean futureIsActive;
  private BigDecimal futureMultiplier;
  private String futurePriceFormat;
  private BigDecimal futureSettlementPrice;
  private String futureTradingHours;
  private String product;
}