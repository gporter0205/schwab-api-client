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
public class FixedIncomeTransactionInstrument extends TransactionInstrument {
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime maturityDate;
    private BigDecimal factor;
    private BigDecimal multiplier;
    private BigDecimal variableRate;
    private Type type;

    public enum Type {
        BOND_UNIT, CERTIFICATE_OF_DEPOSIT, CONVERTIBLE_BOND, COLLATERALIZED_MORTGAGE_OBLIGATION, CORPORATE_BOND, GOVERNMENT_MORTGAGE, GNMA_BONDS, MUNICIPAL_ASSESSMENT_DISTRICT, MUNICIPAL_BOND, OTHER_GOVERNMENT, SHORT_TERM_PAPER, US_TREASURY_BOND, US_TREASURY_BILL, US_TREASURY_NOTE, US_TREASURY_ZERO_COUPON, AGENCY_BOND, WHEN_AS_AND_IF_ISSUED_BOND, ASSET_BACKED_SECURITY, UNKNOWN
    }
}
