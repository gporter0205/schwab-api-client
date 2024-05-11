package com.pangility.schwab.api.client.accountsandtrading.model.accounts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Account {
    private SecuritiesAccount securitiesAccount;
}
