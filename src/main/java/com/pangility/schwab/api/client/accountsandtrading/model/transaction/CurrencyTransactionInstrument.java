package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurrencyTransactionInstrument extends TransactionInstrument {
}
