package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ForexTransactionInstrument extends TransactionInstrument {
    private Type type;
    private CurrencyTransactionInstrument baseCurrency;
    private CurrencyTransactionInstrument counterCurrency;

    public enum Type {
        STANDARD, NBBO, UNKNOWN
    }
}
