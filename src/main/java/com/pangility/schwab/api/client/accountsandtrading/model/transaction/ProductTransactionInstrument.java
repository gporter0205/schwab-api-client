package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ProductTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductTransactionInstrument extends TransactionInstrument {
    private Type type;

    /**
     * Product Transaction Type
     */
    public enum Type {
        /**
         * TBD
         */
        TBD,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
