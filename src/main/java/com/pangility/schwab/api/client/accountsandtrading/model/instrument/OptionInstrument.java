package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * Option Instrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionInstrument extends Instrument {
  private OptionInstrumentType type;
  private PutCall putCall;
  private String underlyingSymbol;
  private Long optionMultiplier;
  private List<OptionDeliverable> optionDeliverables = new ArrayList<>();
}
