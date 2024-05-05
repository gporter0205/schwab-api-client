package com.pangility.schwab.api.client.marketdata.model.markets;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Market Hours.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
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

    /**
     * List of market types used to request market hours.
     */
    public enum MarketType {
        /**
         * Bond market type
         */
        BOND,
        /**
         * Equity market type
         */
        EQUITY,
        /**
         * Forex market type
         */
        FOREX,
        /**
         * Future market type
         */
        FUTURE,
        /**
         * Option market type
         */
        OPTION
    }
}
