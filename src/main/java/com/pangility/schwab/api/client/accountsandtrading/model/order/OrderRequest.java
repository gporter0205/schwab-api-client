package com.pangility.schwab.api.client.accountsandtrading.model.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Class used for simple order requests. If an empty request is sent, then all orders
 * from all valid dates will be returned for the account.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Builder(setterPrefix = "with")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
  private Integer maxResults;
  private ZonedDateTime fromEnteredTime;
  private ZonedDateTime toEnteredTime;
  private Status status;

  /**
   * Nested class for building request
   */
  @Deprecated
  @NoArgsConstructor
  public static final class Builder {
    private Integer maxResults;
    private ZonedDateTime fromEnteredTime;
    private ZonedDateTime toEnteredTime;
    private Status status;

    /**
     * Create a builder for the request
     * @return {@link OrderRequest.Builder}
     */
    @Deprecated
    public static OrderRequest.Builder orderRequest() {
      return new OrderRequest.Builder();
    }

    /**
     * Add max results to the request
     * @param maxResults Integer
     * @return {@link OrderRequest.Builder}
     */
    @Deprecated
    public OrderRequest.Builder withMaxResults(Integer maxResults) {
      this.maxResults = maxResults;
      return this;
    }

    /**
     * Add from entered date to the request
     * @param fromEnteredDate {@link ZonedDateTime}
     * @return {@link OrderRequest.Builder}
     */
    @Deprecated
    public OrderRequest.Builder withFromEnteredDate(ZonedDateTime fromEnteredDate) {
      this.fromEnteredTime = fromEnteredDate;
      return this;
    }

    /**
     * Add to entered date to the request
     * @param toEnteredDate {@link ZonedDateTime}
     * @return {@link OrderRequest.Builder}
     */
    @Deprecated
    public OrderRequest.Builder withToEnteredDate(ZonedDateTime toEnteredDate) {
      this.toEnteredTime = toEnteredDate;
      return this;
    }

    /**
     * Add status to the request
     * @param status {@link Status}
     * @return {@link OrderRequest.Builder}
     */
    @Deprecated
    public OrderRequest.Builder withStatus(Status status) {
      this.status = status;
      return this;
    }

    /**
     * Build the request with the passed values
     * @return {@link OrderRequest}
     */
    @Deprecated
    public OrderRequest build() {
      OrderRequest orderRequest = new OrderRequest();
      orderRequest.maxResults = this.maxResults;
      orderRequest.fromEnteredTime = this.fromEnteredTime;
      orderRequest.toEnteredTime = this.toEnteredTime;
      orderRequest.status = this.status;
      return orderRequest;
    }
  }
}