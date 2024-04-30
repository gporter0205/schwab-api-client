package com.pangility.schwab.api.client.marketdata.model.quote.forex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quote.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceForex extends Reference {
  private Boolean isTradable;
  private String marketMaker;
  private String product;
  private String tradingHours;
}