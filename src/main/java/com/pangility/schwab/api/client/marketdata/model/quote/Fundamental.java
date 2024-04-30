package com.pangility.schwab.api.client.marketdata.model.quote;

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

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Fundamental {
  private BigDecimal avg10DaysVolume;
  private BigDecimal avg1YearVolume;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime declarationDate;
  private BigDecimal divAmount;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime divExDate;
  private Integer divFreq;
  private BigDecimal divPayAmount;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime divPayDate;
  private BigDecimal divYield;
  private BigDecimal eps;
  private BigDecimal fundLeverageFactor;
  private String fundStrategy;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime nextDivExDate;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime nextDivPayDate;
  private BigDecimal peRatio;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}