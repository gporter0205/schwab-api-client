package com.pangility.schwab.api.client.marketdata.model.movers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Object used to pass the request parameters to the Schwab API <em>movers</em> endpoint.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MoversRequest {
  private IndexSymbol indexSymbol;
  private Sort sort;
  private Integer frequency;

  /**
   * Nested class for building request
   */
  @NoArgsConstructor
  public static final class Builder {

    private IndexSymbol indexSymbol;
    private Sort sort;
    private Integer frequency;

    /**
     * Create a builder for the request
     * @return {@link MoversRequest.Builder}
     */
    public static Builder moversRequest() {
      return new Builder();
    }

    /**
     * Add the symbol for the market index to the request
     * @param indexSymbol {@link IndexSymbol}
     * @return {@link MoversRequest.Builder}
     */
    public Builder withIndexSymbol(IndexSymbol indexSymbol) {
      this.indexSymbol = indexSymbol;
      return this;
    }

    /**
     * Add the sort type to the request
     * @param sort {@link Sort}
     * @return {@link MoversRequest.Builder}
     */
    public Builder withSort(Sort sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Add the frequency to the request
     * @param frequency Integer
     * @return {@link MoversRequest.Builder}
     */
    public Builder withFrequency(Integer frequency) {
      this.frequency = frequency;
      return this;
    }

    /**
     * Build the request with the passed values
     * @return {@link MoversRequest}
     */
    public MoversRequest build() {
      MoversRequest moversRequest = new MoversRequest();
      moversRequest.indexSymbol = this.indexSymbol;
      moversRequest.sort = this.sort;
      moversRequest.frequency = this.frequency;

      return moversRequest;
    }
  }

  /**
   * Symbols for the various market indexes
   */
  public enum IndexSymbol {
    /**
     * Dow Jones Index
     */
    $DJI,
    /**
     * NASDAQ Index
     */
    $COMPX,
    /**
     * S{@literal &}P 500 Index
     */
    $SPX,
    /**
     * New York Stock Exchange
     */
    NYSE,
    /**
     * NASDAQ
     */
    NASDAQ,
    /**
     * <a href="https://www.investopedia.com/terms/o/otcbb.asp">Over-the-counter Bulletin Board</a>
     *
     */
    OTCBB,
    /**
     * All Indexes
     */
    INDEX_ALL,
    /**
     * All Equities
     */
    EQUITY_ALL,
    /**
     * All Options
     */
    OPTION_ALL,
    /**
     * All Put Options
     */
    OPTION_PUT,
    /**
     * All Call Options
     */
    OPTION_CALL
  }

  /**
   * Options for Sorting Movers
   */
  public enum Sort {
    /**
     * Sort by volume
     */
    VOLUME,
    /**
     * Sort by number of trades
     */
    TRADES,
    /**
     * Sort by positive percentage change
     */
    PERCENT_CHANGE_UP,
    /**
     * Sort by negative percentage change
     */
    PERCENT_CHANGE_DOWN
  }
}