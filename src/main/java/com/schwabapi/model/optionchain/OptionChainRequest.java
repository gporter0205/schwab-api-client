package com.schwabapi.model.optionchain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.Month;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OptionChainRequest {
  private String symbol;
  private ContractType contractType;
  private Integer strikeCount;
  private Boolean includeQuotes;
  private Strategy strategy;
  private Double interval;
  private Double strike;
  private Range range;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private Double volatility;
  private Double underlyingPrice;
  private Double interestRate;
  private Integer daysToExpiration;
  private Month month;
  private OptionType optionType;

  @NoArgsConstructor
  public static final class Builder {

    private String symbol;
    private ContractType contractType;
    private Integer strikeCount;
    private Boolean includeQuotes;
    private Strategy strategy;
    private Double interval;
    private Double strike;
    private Range range;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Double volatility;
    private Double underlyingPrice;
    private Double interestRate;
    private Integer daysToExpiration;
    private Month month;
    private OptionType optionType;

    public static Builder optionChainRequest() {
      return new Builder();
    }

    public Builder withSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder withContractType(ContractType contractType) {
      this.contractType = contractType;
      return this;
    }

    public Builder withStrikeCount(Integer strikeCount) {
      this.strikeCount = strikeCount;
      return this;
    }

    public Builder withIncludeQuotes(Boolean includeQuotes) {
      this.includeQuotes = includeQuotes;
      return this;
    }

    public Builder withStrategy(Strategy strategy) {
      this.strategy = strategy;
      return this;
    }

    public Builder withInterval(Double interval) {
      this.interval = interval;
      return this;
    }

    public Builder withStrike(Double strike) {
      this.strike = strike;
      return this;
    }

    public Builder withRange(Range range) {
      this.range = range;
      return this;
    }

    public Builder withFromDate(LocalDateTime fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public Builder withToDate(LocalDateTime toDate) {
      this.toDate = toDate;
      return this;
    }

    public Builder withVolatility(Double volatility) {
      this.volatility = volatility;
      return this;
    }

    public Builder withUnderlyingPrice(Double underlyingPrice) {
      this.underlyingPrice = underlyingPrice;
      return this;
    }

    public Builder withInterestRate(Double interestRate) {
      this.interestRate = interestRate;
      return this;
    }

    public Builder withDaysToExpiration(Integer daysToExpiration) {
      this.daysToExpiration = daysToExpiration;
      return this;
    }

    public Builder withMonth(Month month) {
      this.month = month;
      return this;
    }

    public Builder withOptionType(OptionType optionType) {
      this.optionType = optionType;
      return this;
    }

    public OptionChainRequest build() {
      OptionChainRequest priceHistReq = new OptionChainRequest();
      priceHistReq.symbol = this.symbol;
      priceHistReq.contractType = this.contractType;
      priceHistReq.strikeCount = this.strikeCount;
      priceHistReq.includeQuotes = this.includeQuotes;
      priceHistReq.strategy = this.strategy;
      priceHistReq.interval = this.interval;
      priceHistReq.strike = this.strike;
      priceHistReq.range = this.range;
      priceHistReq.fromDate = this.fromDate;
      priceHistReq.toDate = this.toDate;
      priceHistReq.volatility = this.volatility;
      priceHistReq.underlyingPrice = this.underlyingPrice;
      priceHistReq.interestRate = this.interestRate;
      priceHistReq.daysToExpiration = this.daysToExpiration;
      priceHistReq.month = this.month;
      priceHistReq.optionType = this.optionType;

      return priceHistReq;
    }
  }
  public enum ContractType {
    CALL,
    PUT,
    ALL
  }

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

  public enum Range {
    ITM,
    NTM,
    OTM,
    SAK,
    SBK,
    SNK,
    ALL
  }

  public enum OptionType {
    S,
    NS,
    ALL
  }
}