package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

/**
 * Index Instrument
 */
public class IndexInstrument extends Instrument {

  public IndexInstrument(){
    this.setAssetType(AssetType.INDEX);
  }
}
