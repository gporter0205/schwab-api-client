package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Cash Equivalent Instrument
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashEquivalentInstrument extends Instrument {
  private Type type;

  public enum Type {
    SAVINGS,
    MONEY_MARKET_FUND
  }
}