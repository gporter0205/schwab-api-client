package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.Instrument;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionDeliverable {
    private String rootSymbol;
    private Integer strikePercent;
    private Long deliverableNumber;
    private BigDecimal deliverableUnits;
    private Instrument.AssetType assetType;
    private TransactionInstrument deliverable;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
