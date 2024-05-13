package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EquityTransactionInstrument extends TransactionInstrument {
    private Type type;

    public enum Type {
        COMMON_STOCK, PREFERRED_STOCK, DEPOSITORY_RECEIPT, PREFERRED_DEPOSITORY_RECEIPT, RESTRICTED_STOCK, COMPONENT_UNIT, RIGHT, WARRANT, CONVERTIBLE_PREFERRED_STOCK, CONVERTIBLE_STOCK, LIMITED_PARTNERSHIP, WHEN_ISSUED, UNKNOWN
    }
}
