package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * MutualFundTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
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

    /**
     * Mutual Fund Instrument Type
     */
    public enum Type {
        /**
         * Not Applicable
         */
        NOT_APPLICABLE,
        /**
         * Open End Non-Taxable
         */
        OPEN_END_NON_TAXABLE,
        /**
         * Open End Taxable
         */
        OPEN_END_TAXABLE,
        /**
         * No Load Non-Taxable
         */
        NO_LOAD_NON_TAXABLE,
        /**
         * No Load Taxable
         */
        NO_LOAD_TAXABLE,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
