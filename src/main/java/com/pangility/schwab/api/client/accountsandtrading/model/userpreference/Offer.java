package com.pangility.schwab.api.client.accountsandtrading.model.userpreference;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Offer {
    private Boolean level2Permissions;
    private String mktDataPermission;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
