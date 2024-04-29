package com.schwabapi.marketdata.model.markets;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.common.deserializers.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Market Hours.
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName("hours")
public class Hours {
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private MarketType marketType;
    private String exchange;
    private String product;
    private String productName;
    private String category;
    private Boolean isOpen;
    private SessionHours sessionHours;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();

    public enum MarketType {
      BOND,
      EQUITY,
      ETF,
      FOREX,
      FUTURE,
      FUTURE_OPTION,
      FUNDAMENTAL,
      INDEX,
      INDICATOR,
      MUTUAL_FUND,
      OPTION,
      UNKNOWN
    }
}
