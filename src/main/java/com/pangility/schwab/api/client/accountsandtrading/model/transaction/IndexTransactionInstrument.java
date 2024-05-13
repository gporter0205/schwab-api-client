package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IndexTransactionInstrument extends TransactionInstrument {
    private Boolean activeContract;
    private Type type;

    public enum Type {
        BROAD_BASED, NARROW_BASED, UNKNOWN
    }
}
