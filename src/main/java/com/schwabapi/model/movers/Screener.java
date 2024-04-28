package com.schwabapi.model.movers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Screener {
    private BigDecimal change;
    private String description;
    private String direction;
    private BigDecimal last;
    private String symbol;
    private Long totalVolume;
}