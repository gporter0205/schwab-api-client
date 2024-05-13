package com.pangility.schwab.api.client.accountsandtrading.model.userpreference;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserPreferenceResponse {
    private List<Account> accounts;
    private List<StreamerInfo> streamerInfo;
    private List<Offer> offers;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
