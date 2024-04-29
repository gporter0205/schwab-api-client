package com.schwabapi.marketdata.model.instruments;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.common.deserializers.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Fundamental Data
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Fundamental {
  private String symbol;
  private BigDecimal high52;
  private BigDecimal low52;
  private BigDecimal dividendAmount;
  private BigDecimal dividendYield;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate dividendDate;
  private BigDecimal peRatio;
  private BigDecimal pegRatio;
  private BigDecimal pbRatio;
  private BigDecimal prRatio;
  private BigDecimal pcfRatio;
  private BigDecimal grossMarginTTM;
  private BigDecimal grossMarginMRQ;
  private BigDecimal netProfitMarginTTM;
  private BigDecimal netProfitMarginMRQ;
  private BigDecimal operatingMarginTTM;
  private BigDecimal operatingMarginMRQ;
  private BigDecimal returnOnEquity;
  private BigDecimal returnOnAssets;
  private BigDecimal returnOnInvestment;
  private BigDecimal quickRatio;
  private BigDecimal currentRatio;
  private BigDecimal interestCoverage;
  private BigDecimal totalDebtToCapital;
  private BigDecimal ltDebtToEquity;
  private BigDecimal totalDebtToEquity;
  private BigDecimal epsTTM;
  private BigDecimal epsChangePercentTTM;
  private BigDecimal epsChangeYear;
  private BigDecimal epsChange;
  private BigDecimal revChangeYear;
  private BigDecimal revChangeTTM;
  private BigDecimal revChangeIn;
  private BigDecimal sharesOutstanding;
  private BigDecimal marketCapFloat;
  private BigDecimal marketCap;
  private BigDecimal bookValuePerShare;
  private BigDecimal shortIntToFloat;
  private BigDecimal shortIntDayToCover;
  private BigDecimal divGrowthRate3Year;
  private BigDecimal dividendPayAmount;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate dividendPayDate;
  private BigDecimal beta;
  private BigDecimal vol1DayAvg;
  private BigDecimal vol10DayAvg;
  private BigDecimal vol3MonthAvg;
  private Long avg10DaysVolume;
  private Long avg1DaysVolume;
  private Long avg3MonthVolume;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate declarationDate;
  private Integer dividendFreq;
  private BigDecimal eps;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate corpactionDate;
  private Long dtnVolume;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate nextDividendDate;
  private BigDecimal fundLeverageFactor;
  private String fundStrategy;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}