package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

import lombok.Getter;
import lombok.Setter;

/**
 * Equity Instrument
 */
@Getter
@Setter
public class EquityInstrument extends Instrument {

  public EquityInstrument(){
    this.setAssetType(AssetType.EQUITY);
  }

  private String type;
}