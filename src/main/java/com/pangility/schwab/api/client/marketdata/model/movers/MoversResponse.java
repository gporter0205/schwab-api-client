package com.pangility.schwab.api.client.marketdata.model.movers;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object used to receive the response from the Schwab API <em>movers</em> endpoint
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MoversResponse {
    private List<Screener> screeners;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
