package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ForexTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ForexTransactionInstrument extends TransactionInstrument {
    private Type type;
    private CurrencyTransactionInstrument baseCurrency;
    private CurrencyTransactionInstrument counterCurrency;

    /**
     * Forex Instrument Type
     */
    public enum Type {
        /**
         * Standard
         */
        STANDARD,
        /**
         * NBBO
         */
        NBBO,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
