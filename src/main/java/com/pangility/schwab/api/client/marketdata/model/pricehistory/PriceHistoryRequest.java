package com.pangility.schwab.api.client.marketdata.model.pricehistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  private Long startDate;
  private Long endDate;
  private Boolean needExtendedHoursData;
  private Boolean needPreviousClose;

  @NoArgsConstructor
  public static final class Builder {

    private String symbol;
    private PeriodType periodType;
    private Integer period;
    private FrequencyType frequencyType;
    private Integer frequency;
    private Long startDate;
    private Long endDate;
    private Boolean needExtendedHoursData;
    private Boolean needPreviousClose;
    public static Builder priceHistReq() {
      return new Builder();
    }

    public Builder withSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder withPeriodType(PeriodType periodType) {
      this.periodType = periodType;
      return this;
    }

    public Builder withPeriod(Integer period) {
      this.period = period;
      return this;
    }

    public Builder withFrequencyType(FrequencyType frequencyType) {
      this.frequencyType = frequencyType;
      return this;
    }

    public Builder withFrequency(Integer frequency) {
      this.frequency = frequency;
      return this;
    }

    public Builder withStartDate(Long startDate) {
      this.startDate = startDate;
      return this;
    }

    public Builder withEndDate(Long endDate) {
      this.endDate = endDate;
      return this;
    }

    public Builder withNeedExtendedHoursData(Boolean needExtendedHoursData) {
      this.needExtendedHoursData = needExtendedHoursData;
      return this;
    }

    public Builder withNeedPreviousClose(Boolean needPreviousClose) {
      this.needPreviousClose = needPreviousClose;
      return this;
    }

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