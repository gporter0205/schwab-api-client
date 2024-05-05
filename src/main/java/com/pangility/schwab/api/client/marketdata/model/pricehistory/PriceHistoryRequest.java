package com.pangility.schwab.api.client.marketdata.model.pricehistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Object used to pass the request parameters to the Schwab API <em>pricehistory</em> endpoint.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PriceHistoryRequest {
  private String symbol;
  private PeriodType periodType;
  private Integer period;
  private FrequencyType frequencyType;
  private Integer frequency;
  private LocalDate startDate;
  private LocalDate endDate;
  private Boolean needExtendedHoursData;
  private Boolean needPreviousClose;

  /**
   * Nested class for building request
   */
  @NoArgsConstructor
  public static final class Builder {

    private String symbol;
    private PeriodType periodType;
    private Integer period;
    private FrequencyType frequencyType;
    private Integer frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean needExtendedHoursData;
    private Boolean needPreviousClose;

    /**
     * Create a builder for the request
     * @return {@link PriceHistoryRequest.Builder}
     */
    public static Builder priceHistReq() {
      return new Builder();
    }

    /**
     * Add the symbol to the request
     * @param symbol String
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    /**
     * Add the period type to the request
     * @param periodType {@link PeriodType}
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withPeriodType(PeriodType periodType) {
      this.periodType = periodType;
      return this;
    }

    /**
     * Add the period to the request
     * @param period Integer
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withPeriod(Integer period) {
      this.period = period;
      return this;
    }

    /**
     * Add the frequency type to the request
     * @param frequencyType {@link FrequencyType}
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withFrequencyType(FrequencyType frequencyType) {
      this.frequencyType = frequencyType;
      return this;
    }

    /**
     * Add the frequency to the request
     * @param frequency Integer
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withFrequency(Integer frequency) {
      this.frequency = frequency;
      return this;
    }

    /**
     * Add the start date to the request
     * @param startDate LocalDate
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withStartDate(LocalDate startDate) {
      this.startDate = startDate;
      return this;
    }

    /**
     * Add the end date to the request
     * @param endDate LocalDate
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withEndDate(LocalDate endDate) {
      this.endDate = endDate;
      return this;
    }

    /**
     * Add the need extended hours data flag to the request
     * @param needExtendedHoursData Boolean
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withNeedExtendedHoursData(Boolean needExtendedHoursData) {
      this.needExtendedHoursData = needExtendedHoursData;
      return this;
    }

    /**
     * Add the need previous close flag to the request
     * @param needPreviousClose Boolean
     * @return {@link PriceHistoryRequest.Builder}
     */
    public Builder withNeedPreviousClose(Boolean needPreviousClose) {
      this.needPreviousClose = needPreviousClose;
      return this;
    }

    /**
     * Build the request with the passed values
     * @return {@link PriceHistoryRequest}
     */
    public PriceHistoryRequest build() {
      PriceHistoryRequest priceHistoryRequest = new PriceHistoryRequest();
      priceHistoryRequest.symbol = this.symbol;
      priceHistoryRequest.periodType = this.periodType;
      priceHistoryRequest.period = this.period;
      priceHistoryRequest.frequencyType = this.frequencyType;
      priceHistoryRequest.frequency = this.frequency;
      priceHistoryRequest.startDate = this.startDate;
      priceHistoryRequest.endDate = this.endDate;
      priceHistoryRequest.needExtendedHoursData = this.needExtendedHoursData;
      priceHistoryRequest.needPreviousClose = this.needPreviousClose;
      return priceHistoryRequest;
    }
  }
}