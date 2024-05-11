package com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EncryptedAccount {
    private String accountNumber;
    private String hashValue;
}