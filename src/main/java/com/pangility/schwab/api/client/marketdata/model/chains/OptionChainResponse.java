package com.pangility.schwab.api.client.marketdata.model.chains;

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
 * Object used to receive the response from the Schwab API <em>chains</em> endpoint
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionChainResponse {
  private String symbol;
  private String status;
  private Underlying underlying;
  private OptionChainRequest.Strategy strategy;
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
}