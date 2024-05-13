package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

/**
 * Mutual Fund Instrument
 */
public class MutualFundInstrument extends Instrument {
  public MutualFundInstrument(){
    this.setAssetType(AssetType.MUTUAL_FUND);
  }
}
