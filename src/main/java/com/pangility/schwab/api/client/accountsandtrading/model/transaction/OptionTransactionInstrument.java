package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

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
    private Type type;
    private String underlyingSymbol;
    private String underlyingCusip;

    public enum PutCall {
        PUT, CALL, UNKNOWN
    }

    public enum Type {
        VANILLA, BINARY, BARRIER, UNKNOWN
    }
}
