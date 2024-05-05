package com.pangility.schwab.api.client.marketdata.model.pricehistory;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Candle used for PriceHistory
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Candle {
  private BigDecimal open;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal close;
  private Long volume;
  private Long datetime;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime datetimeISO8601;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
