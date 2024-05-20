package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * IndexTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IndexTransactionInstrument extends TransactionInstrument {
    private Boolean activeContract;
    private Type type;

    /**
     * Index Instrument Type
     */
    public enum Type {
        /**
         * Broad Based
         */
        BROAD_BASED,
        /**
         * Narrow Based
         */
        NARROW_BASED,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
