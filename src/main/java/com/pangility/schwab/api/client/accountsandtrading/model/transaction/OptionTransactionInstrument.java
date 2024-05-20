package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.OptionInstrumentType;
import com.pangility.schwab.api.client.accountsandtrading.model.instrument.PutCall;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * OptionTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionTransactionInstrument extends TransactionInstrument {
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime expirationDate;
    private List<OptionDeliverable> optionDeliverables;
    private Integer optionPremiumMultiplier;
    private PutCall putCall;
    private BigDecimal strikePrice;
    private OptionInstrumentType type;
    private String underlyingSymbol;
    private String underlyingCusip;
}
