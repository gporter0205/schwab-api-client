package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashEquivalentTransactionInstrument extends TransactionInstrument {
    private Type type;

    public enum Type {
        SWEEP_VEHICLE, SAVINGS, MONEY_MARKET_FUND, UNKNOWN
    }
}
