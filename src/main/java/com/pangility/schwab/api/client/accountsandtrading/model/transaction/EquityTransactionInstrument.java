package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.EquityInstrumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * EquityTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EquityTransactionInstrument extends TransactionInstrument {
    private EquityInstrumentType type;
}
