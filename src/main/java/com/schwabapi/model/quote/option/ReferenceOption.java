package com.schwabapi.model.quote.option;

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
public class ReferenceOption extends Reference {
  private String contractType;
  private Integer daysToExpiration;
  private String deliverables;
  private String exerciseType;
  private Integer expirationDay;
  private Integer expirationMonth;
  private String expirationType;
  private Integer expirationYear;
  private Boolean isPennyPilot;
  private Integer lastTradingDay;
  private BigDecimal multiplier;
  private String settlementType;
  private BigDecimal strikePrice;
  private String underlying;
}