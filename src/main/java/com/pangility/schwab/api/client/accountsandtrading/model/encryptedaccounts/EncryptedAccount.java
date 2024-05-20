package com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * EncryptedAccount. Account numbers in plain text cannot be used outside of headers or
 * request/response bodies. Retrieve the list of plain text/encrypted value pairs, and use
 * encrypted account values for all subsequent calls for any accountNumber request.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EncryptedAccount {
    private String accountNumber;
    private String hashValue;
}