package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FutureTransactionInstrument extends TransactionInstrument {
    private Boolean activeContract;
    private Type type;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime expirationDate;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime lastTradingDate;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime firstNoticeDate;
    private BigDecimal multiplier;

    public enum Type {
        STANDARD, UNKNOWN
    }
}
