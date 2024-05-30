package com.pangility.schwab.api.client.accountsandtrading.model.instrument;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Instrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "assetType",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EquityInstrument.class, name = "EQUITY"),
    @JsonSubTypes.Type(value = MutualFundInstrument.class, name = "MUTUAL_FUND"),
    @JsonSubTypes.Type(value = CurrencyInstrument.class, name = "CURRENCY"),
    @JsonSubTypes.Type(value = OptionInstrument.class, name = "OPTION"),
    @JsonSubTypes.Type(value = IndexInstrument.class, name = "INDEX"),
    @JsonSubTypes.Type(value = CashEquivalentInstrument.class, name = "CASH_EQUIVALENT"),
    @JsonSubTypes.Type(value = FixedIncomeInstrument.class, name = "FIXED_INCOME"),
    @JsonSubTypes.Type(value = EquityInstrument.class, name = "COLLECTIVE_INVESTMENT"),
})
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Instrument {
  /**
   * Instrument Asset Type
   */
  protected AssetType assetType;
  /**
   * Instrument Cusip
   */
  protected String cusip;
  /**
   * Instrument Symbol
   */
  protected String symbol;
  /**
   * Instrument Description
   */
  protected String description;
  /**
   * Instrument ID
   */
  protected Long instrumentId;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

}
