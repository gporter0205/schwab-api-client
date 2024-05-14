package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.CashEquivalentInstrumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CashEquivalentTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashEquivalentTransactionInstrument extends TransactionInstrument {
    private CashEquivalentInstrumentType type;
}
