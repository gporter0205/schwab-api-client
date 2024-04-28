package com.schwabapi.model.pricehistory;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.deserializers.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PriceHistory meant to display on a graph
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "candles",
    "symbol",
    "empty"
})
public class PriceHistoryResponse {
  /**
   * List of candles
   */
  private List<Candle> candles = new ArrayList<>();
  private String symbol;
  private Boolean empty;
  private BigDecimal previousClose;
  private Long previousCloseDate;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate previousCloseDateISO8601;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
