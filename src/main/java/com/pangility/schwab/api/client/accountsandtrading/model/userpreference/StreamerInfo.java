package com.pangility.schwab.api.client.accountsandtrading.model.userpreference;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * User Preference StreamerInfo
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StreamerInfo {
    private String streamerSocketUrl;
    private String schwabClientCustomerId;
    private String schwabClientCorrelId;
    private String schwabClientChannel;
    private String schwabClientFunctionId;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
