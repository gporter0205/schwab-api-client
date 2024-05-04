package com.pangility.schwab.api.client.marketdata.model.chains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * Object used to pass the request parameters to the Schwab API <em>chains</em> endpoint.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
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

  /**
   * Nested class for building request
   */
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

    /**
     * Create a builder for the request
     * @return {@link OptionChainRequest.Builder}
     */
    public static Builder optionChainRequest() {
      return new Builder();
    }

    /**
     * Add the symbol to the request
     * @param symbol String
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    /**
     * Add the contract type to the request
     * @param contractType {@link OptionChainRequest.ContractType}
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withContractType(ContractType contractType) {
      this.contractType = contractType;
      return this;
    }

    /**
     * Add the strike count to the request
     * @param strikeCount Integer
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withStrikeCount(Integer strikeCount) {
      this.strikeCount = strikeCount;
      return this;
    }

    /**
     * Add the include quotes flag to the request
     * @param includeQuotes Boolean
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withIncludeQuotes(Boolean includeQuotes) {
      this.includeQuotes = includeQuotes;
      return this;
    }

    /**
     * Add the strategy to the request
     * @param strategy {@link OptionChainRequest.Strategy}
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withStrategy(Strategy strategy) {
      this.strategy = strategy;
      return this;
    }

    /**
     * Add the interval to the request
     * @param interval Double
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withInterval(Double interval) {
      this.interval = interval;
      return this;
    }

    /**
     * Add the strike to the request
     * @param strike Double
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withStrike(Double strike) {
      this.strike = strike;
      return this;
    }

    /**
     * Add the range to the request
     * @param range {@link OptionChainRequest.Range}
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withRange(Range range) {
      this.range = range;
      return this;
    }

    /**
     * Add the from date to the request
     * @param fromDate LocalDateTime
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withFromDate(LocalDateTime fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    /**
     * Add the to date to the request
     * @param toDate LocalDateTime
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withToDate(LocalDateTime toDate) {
      this.toDate = toDate;
      return this;
    }

    /**
     * Add the volatility to the request
     * @param volatility Double
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withVolatility(Double volatility) {
      this.volatility = volatility;
      return this;
    }

    /**
     * Add the underlying price to the request
     * @param underlyingPrice Double
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withUnderlyingPrice(Double underlyingPrice) {
      this.underlyingPrice = underlyingPrice;
      return this;
    }

    /**
     * Add the interest rate to the request
     * @param interestRate Double
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withInterestRate(Double interestRate) {
      this.interestRate = interestRate;
      return this;
    }

    /**
     * Add the days to expiration to the request
     * @param daysToExpiration Integer
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withDaysToExpiration(Integer daysToExpiration) {
      this.daysToExpiration = daysToExpiration;
      return this;
    }

    /**
     * Add the month to the request
     * @param month {@link Month}
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withMonth(Month month) {
      this.month = month;
      return this;
    }

    /**
     * Add the option type to the request
     * @param optionType {@link OptionType}
     * @return {@link OptionChainRequest.Builder}
     */
    public Builder withOptionType(OptionType optionType) {
      this.optionType = optionType;
      return this;
    }

    /**
     * Build the request with the passed values
     * @return {@link OptionChainRequest}
     */
    public OptionChainRequest build() {
      OptionChainRequest optionChainRequest = new OptionChainRequest();
      optionChainRequest.symbol = this.symbol;
      optionChainRequest.contractType = this.contractType;
      optionChainRequest.strikeCount = this.strikeCount;
      optionChainRequest.includeQuotes = this.includeQuotes;
      optionChainRequest.strategy = this.strategy;
      optionChainRequest.interval = this.interval;
      optionChainRequest.strike = this.strike;
      optionChainRequest.range = this.range;
      optionChainRequest.fromDate = this.fromDate;
      optionChainRequest.toDate = this.toDate;
      optionChainRequest.volatility = this.volatility;
      optionChainRequest.underlyingPrice = this.underlyingPrice;
      optionChainRequest.interestRate = this.interestRate;
      optionChainRequest.daysToExpiration = this.daysToExpiration;
      optionChainRequest.month = this.month;
      optionChainRequest.optionType = this.optionType;

      return optionChainRequest;
    }
  }

  /**
   * Contract Types
   */
  public enum ContractType {
    /**
     * Call
     */
    CALL,
    /**
     * Put
     */
    PUT,
    /**
     * Both Call and Put
     */
    ALL
  }

  /**
   * Strategies
   */
  public enum Strategy {
    /**
     * SINGLE
     */
    SINGLE,
    /**
     * ANALYTICAL
     */
    ANALYTICAL,
    /**
     * COVERED
     */
    COVERED,
    /**
     * VERTICAL
     */
    VERTICAL,
    /**
     * CALENDAR
     */
    CALENDAR,
    /**
     * STRANGLE
     */
    STRANGLE,
    /**
     * STRADDLE
     */
    STRADDLE,
    /**
     * BUTTERFLY
     */
    BUTTERFLY,
    /**
     * CONDOR
     */
    CONDOR,
    /**
     * DIAGONAL
     */
    DIAGONAL,
    /**
     * COLLAR
     */
    COLLAR,
    /**
     * ROLL
     */
    ROLL
  }

  /**
   * Ranges
   */
  public enum Range {
    /**
     * ITM
     */
    ITM,
    /**
     * NTM
     */
    NTM,
    /**
     * OTM
     */
    OTM,
    /**
     * SAK
     */
    SAK,
    /**
     * SBK
     */
    SBK,
    /**
     * SNK
     */
    SNK,
    /**
     * ALL
     */
    ALL
  }

  /**
   * Option Types
   */
  public enum OptionType {
    /**
     * S
     */
    S,
    /**
     * NS
     */
    NS,
    /**
     * ALL
     */
    ALL
  }
}