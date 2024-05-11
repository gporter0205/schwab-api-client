package com.pangility.schwab.api.client.accountsandtrading.model.accounts;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Option Instrument
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionInstrument extends Instrument {
  private Type type;
  private PutCall putCall;
  private String underlyingSymbol;
  private Long optionMultiplier;
  private List<OptionDeliverable> optionDeliverables = new ArrayList<>();
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public enum PutCall {
    PUT,
    CALL
  }

  public enum Type {
    VANILLA,
    BINARY,
    BARRIER
  }
}
