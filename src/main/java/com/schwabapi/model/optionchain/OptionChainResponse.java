package com.schwabapi.model.optionchain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Option Chains
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionChainResponse {
  private String assetMainType;
  private String assetSubType;
  private String symbol;
  private String status;
  private Underlying underlying;
  private Strategy strategy;
  private BigDecimal interval;
  private Boolean isDelayed;
  private Boolean isIndex;
  private Boolean isChainTruncated;
  private BigDecimal daysToExpiration;
  private BigDecimal interestRate;
  private BigDecimal underlyingPrice;
  private BigDecimal volatility;
  private Integer numberOfContracts;
  private Map<String, Map<BigDecimal, List<OptionContract>>> callExpDateMap;
  private Map<String, Map<BigDecimal, List<OptionContract>>> putExpDateMap;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public enum Strategy {
    SINGLE,
    ANALYTICAL,
    COVERED,
    VERTICAL,
    CALENDAR,
    STRANGLE,
    STRADDLE,
    BUTTERFLY,
    CONDOR,
    DIAGONAL,
    COLLAR,
    ROLL
  }
}