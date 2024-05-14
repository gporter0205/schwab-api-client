package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.*;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.AssetType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "assetType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CashEquivalentTransactionInstrument.class, name = "CASH_EQUIVALENT"),
        @JsonSubTypes.Type(value = CollectiveInvestmentTransactionInstrument.class, name = "COLLECTIVE_INVESTMENT"),
        @JsonSubTypes.Type(value = CurrencyTransactionInstrument.class, name = "CURRENCY"),
        @JsonSubTypes.Type(value = EquityTransactionInstrument.class, name = "EQUITY"),
        @JsonSubTypes.Type(value = FixedIncomeTransactionInstrument.class, name = "FIXED_INCOME"),
        @JsonSubTypes.Type(value = ForexTransactionInstrument.class, name = "FOREX"),
        @JsonSubTypes.Type(value = FutureTransactionInstrument.class, name = "FUTURE"),
        @JsonSubTypes.Type(value = IndexTransactionInstrument.class, name = "INDEX"),
        @JsonSubTypes.Type(value = MutualFundTransactionInstrument.class, name = "MUTUAL_FUND"),
        @JsonSubTypes.Type(value = OptionTransactionInstrument.class, name = "OPTION"),
        @JsonSubTypes.Type(value = ProductTransactionInstrument.class, name = "PRODUCT"),
})
public class TransactionInstrument {
  private AssetType assetType;
  private String cusip;
  private String symbol;
  private String description;
  private Long instrumentId;
  private BigDecimal netChange;
  private String status;
  private BigDecimal closingPrice;

  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}