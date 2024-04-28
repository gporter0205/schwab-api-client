package com.schwabapi.model.optionexpirationchain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExpirationChainResponse {
    private String status;
    private List<Expiration> expirationList;
}
