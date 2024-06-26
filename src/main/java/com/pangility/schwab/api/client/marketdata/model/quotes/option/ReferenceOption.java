package com.pangility.schwab.api.client.marketdata.model.quotes.option;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * ReferenceOption
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
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
  private Long lastTradingDay;
  private BigDecimal multiplier;
  private String settlementType;
  private BigDecimal strikePrice;
  private String underlying;
}