package com.schwabapi.marketdata.model.optionchain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.common.deserializers.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Option
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionContract {
  private PutCall putCall;
  private String symbol;
  private String description;
  private String exchangeName;
  @JsonAlias({"bid"})
  private BigDecimal bidPrice;
  @JsonAlias({"ask"})
  private BigDecimal askPrice;
  @JsonAlias({"last"})
  private BigDecimal lastPrice;
  private String bidAskSize;
  @JsonAlias({"mark"})
  private BigDecimal markPrice;
  private Long bidSize;
  private Long askSize;
  private Long lastSize;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal openPrice;
  private BigDecimal closePrice;
  private Long totalVolume;
  private Long quoteTimeInLong;
  private Long tradeTimeInLong;
  private BigDecimal netChange;
  private BigDecimal volatility;
  private BigDecimal delta;
  private BigDecimal gamma;
  private BigDecimal theta;
  private BigDecimal vega;
  private BigDecimal rho;
  private BigDecimal timeValue;
  private BigDecimal openInterest;
  @JsonAlias({"inTheMoney"})
  private Boolean isInTheMoney;
  private BigDecimal theoreticalOptionValue;
  private BigDecimal theoreticalVolatility;
  @JsonAlias({"mini"})
  private Boolean isMini;
  @JsonAlias({"nonStandard"})
  private Boolean isNonStandard;
  private List<OptionDeliverable> optionDeliverablesList = new ArrayList<>();
  private BigDecimal strikePrice;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime expirationDate;
  private String expirationType;
  private BigDecimal multiplier;
  private String settlementType;
  private String deliverableNote;
  private Boolean isIndexOption;
  private BigDecimal percentChange;
  private BigDecimal markChange;
  private BigDecimal markPercentChange;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public enum PutCall {
    PUT,
    CALL
  }
}