package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Equity Instrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EquityInstrument extends Instrument {

  /**
   * constructor
   */
  public EquityInstrument(){
    this.setAssetType(AssetType.EQUITY);
  }

  private EquityInstrumentType type;
}
