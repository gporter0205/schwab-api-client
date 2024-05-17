package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Class used for simple transaction requests. If an empty request is sent, then all transactions
 * from all valid dates will be returned for the account.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@ToString
@NoArgsConstructor
public class TransactionRequest {
  private ZonedDateTime startDate;
  private ZonedDateTime endDate;
  private String symbol;
  private String types;

  /**
   * Nested class for building request
   */
  @NoArgsConstructor
  public static final class Builder {
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String symbol;
    private String types;

    /**
     * Create a builder for the request
     * @return {@link TransactionRequest.Builder}
     */
    public static TransactionRequest.Builder transactionRequest() {
      return new TransactionRequest.Builder();
    }

    /**
     * Add start date to the request
     * @param startDate {@link ZonedDateTime}
     * @return {@link TransactionRequest.Builder}
     */
    public TransactionRequest.Builder withStartDate(ZonedDateTime startDate) {
      this.startDate = startDate;
      return this;
    }

    /**
     * Add end date to the request
     * @param endDate {@link ZonedDateTime}
     * @return {@link TransactionRequest.Builder}
     */
    public TransactionRequest.Builder withEndDate(ZonedDateTime endDate) {
      this.endDate = endDate;
      return this;
    }

    /**
     * Add symbol to the request
     * @param symbol String
     * @return {@link TransactionRequest.Builder}
     */
    public TransactionRequest.Builder withSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    /**
     * Add types to the request
     * @param types String
     * @return {@link TransactionRequest.Builder}
     */
    public TransactionRequest.Builder withTypes(String types) {
      this.types = types;
      return this;
    }

    /**
     * Build the request with the passed values
     * @return {@link TransactionRequest}
     */
    public TransactionRequest build() {
      TransactionRequest transactionRequest = new TransactionRequest();
      transactionRequest.startDate = this.startDate;
      transactionRequest.endDate = this.endDate;
      transactionRequest.symbol = this.symbol;
      transactionRequest.types = this.types;
      return transactionRequest;
    }
  }
}
