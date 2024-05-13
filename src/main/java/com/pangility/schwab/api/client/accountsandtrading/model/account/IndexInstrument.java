package com.pangility.schwab.api.client.accountsandtrading.model.account;

/**
 * Index Instrument
 */
public class IndexInstrument extends Instrument {

  public IndexInstrument(){
    this.setAssetType(AssetType.INDEX);
  }
}
