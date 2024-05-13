package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MutualFundTransactionInstrument extends TransactionInstrument {
    private String fundFamilyType;
    private String fundFamilySymbol;
    private String fundGroup;
    private Type type;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime exchangeCutoffTime;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime purchaseCutoffTime;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime redemptionCutoffTime;

    public enum Type {
        NOT_APPLICABLE, OPEN_END_NON_TAXABLE, OPEN_END_TAXABLE, NO_LOAD_NON_TAXABLE, NO_LOAD_TAXABLE, UNKNOWN
    }
}
