package com.pangility.schwab.api.client.accountsandtrading.model.account;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(
    use = Id.NAME,
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
public abstract class Instrument {
  protected AssetType assetType;
  protected String cusip;
  protected String symbol;
  protected String description;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public enum AssetType {
    CASH_EQUIVALENT,
    CURRENCY,
    EQUITY,
    FIXED_INCOME,
    INDEX,
    MUTUAL_FUND,
    OPTION,
    COLLECTIVE_INVESTMENT,
  }
}
