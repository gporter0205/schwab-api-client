package com.pangility.schwab.api.client.accountsandtrading.model.accounts;

/**
 * Index Instrument
 */
public class IndexInstrument extends Instrument {

  public IndexInstrument(){
    this.setAssetType(AssetType.INDEX);
  }
}
