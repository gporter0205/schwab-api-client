package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveInvestmentTransactionInstrument extends TransactionInstrument {
    private Type type;

    public enum Type {
        UNIT_INVESTMENT_TRUST, EXCHANGE_TRADED_FUND, CLOSED_END_FUND, INDEX, UNITS
    }
}
