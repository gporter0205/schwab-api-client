package com.pangility.schwab.api.client.marketdata.model.optionexpirationchain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Expiration {
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expirationDate;
    private Integer daysToExpiration;
    private String expirationType;
    private String settlementType;
    private String optionRoots;
    private Boolean standard;
}
