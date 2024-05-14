package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * FixedIncomeTransactionInstrument
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FixedIncomeTransactionInstrument extends TransactionInstrument {
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime maturityDate;
    private BigDecimal factor;
    private BigDecimal multiplier;
    private BigDecimal variableRate;
    private Type type;

    /**
     * Fixed Income Instrument Type
     */
    public enum Type {
        /**
         * Bond Unit
         */
        BOND_UNIT,
        /**
         * Certificate of Deposit
         */
        CERTIFICATE_OF_DEPOSIT,
        /**
         * Convertible Bond
         */
        CONVERTIBLE_BOND,
        /**
         * Collateralized Mortgage Obligation
         */
        COLLATERALIZED_MORTGAGE_OBLIGATION,
        /**
         * Corporate Bond
         */
        CORPORATE_BOND,
        /**
         * Government Mortgage
         */
        GOVERNMENT_MORTGAGE,
        /**
         * GNMA Bonds
         */
        GNMA_BONDS,
        /**
         * Municipal Assessment District
         */
        MUNICIPAL_ASSESSMENT_DISTRICT,
        /**
         * Municipal Bond
         */
        MUNICIPAL_BOND,
        /**
         * Other Government
         */
        OTHER_GOVERNMENT,
        /**
         * Short Term Paper
         */
        SHORT_TERM_PAPER,
        /**
         * US Treasury Bond
         */
        US_TREASURY_BOND,
        /**
         * US Treasury Bill
         */
        US_TREASURY_BILL,
        /**
         * US Treasury Note
         */
        US_TREASURY_NOTE,
        /**
         * US Treasury Zero Coupon
         */
        US_TREASURY_ZERO_COUPON,
        /**
         * Agency Bond
         */
        AGENCY_BOND,
        /**
         * When As and If Issued Bond
         */
        WHEN_AS_AND_IF_ISSUED_BOND,
        /**
         * Asset Backed Security
         */
        ASSET_BACKED_SECURITY,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
