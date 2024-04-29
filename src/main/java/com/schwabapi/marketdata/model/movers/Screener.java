package com.schwabapi.marketdata.model.movers;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Screener {
    private BigDecimal netChange;
    private BigDecimal netPercentChange;
    private String description;
    private String direction;
    private BigDecimal lastPrice;
    private String symbol;
    private Long totalVolume;
    private Long volume;
    private BigDecimal marketShare;
    private Long trades;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}