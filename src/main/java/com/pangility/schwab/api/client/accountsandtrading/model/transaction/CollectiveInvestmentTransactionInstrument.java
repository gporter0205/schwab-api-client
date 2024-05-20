package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CollectiveInvestmentTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveInvestmentTransactionInstrument extends TransactionInstrument {
    private Type type;

    /**
     * Collective Investment Transaction Instrument Type
     */
    public enum Type {
        /**
         * Unit Investment Trust
         */
        UNIT_INVESTMENT_TRUST,
        /**
         * Exchange Traded Fund
         */
        EXCHANGE_TRADED_FUND,
        /**
         * Closed End Fund
         */
        CLOSED_END_FUND,
        /**
         * Index
         */
        INDEX,
        /**
         * Units
         */
        UNITS
    }
}
