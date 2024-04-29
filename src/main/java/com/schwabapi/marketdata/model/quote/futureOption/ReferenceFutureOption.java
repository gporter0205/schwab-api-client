package com.schwabapi.marketdata.model.quote.futureOption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.schwabapi.marketdata.model.quote.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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