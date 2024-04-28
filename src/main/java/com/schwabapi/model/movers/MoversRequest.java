package com.schwabapi.model.movers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MoversRequest {
  private IndexSymbol indexSymbol;
  private Sort sort;
  private Integer frequency;

  @NoArgsConstructor
  public static final class Builder {

    private IndexSymbol indexSymbol;
    private Sort sort;
    private Integer frequency;

    public static Builder moversRequest() {
      return new Builder();
    }

    public Builder withIndexSymbol(IndexSymbol indexSymbol) {
      this.indexSymbol = indexSymbol;
      return this;
    }

    public Builder withSort(Sort sort) {
      this.sort = sort;
      return this;
    }

    public Builder withFrequency(Integer frequency) {
      this.frequency = frequency;
      return this;
    }

    public MoversRequest build() {
      MoversRequest moversRequest = new MoversRequest();
      moversRequest.indexSymbol = this.indexSymbol;
      moversRequest.sort = this.sort;
      moversRequest.frequency = this.frequency;

      return moversRequest;
    }
  }
  public enum IndexSymbol {
    $DJI,
    $COMPX,
    $SPX,
    NYSE,
    NASDAQ,
    OTCBB,
    INDEX_ALL,
    EQUITY_ALL,
    OPTION_ALL,
    OPTION_PUT,
    OPTION_CALL
  }
  public enum Sort {
    VOLUME,
    TRADES,
    PERCENT_CHANGE_UP,
    PERCENT_CHANGE_DOWN
  }
}