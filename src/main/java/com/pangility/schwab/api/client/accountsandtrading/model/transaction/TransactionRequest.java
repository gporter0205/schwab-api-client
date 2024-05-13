package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Class used for simple order requests. If an empty request is sent, then all orders
 * from all valid dates will be returned for the account.
 * If a {@code fromEnteredTime} is used, a {@code toEnteredTime} must also be used, and vice versa.
 * There is a 60 day max from today's date for the {@code fromEnteredTime} param.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionRequest {
  private ZonedDateTime startDate;
  private ZonedDateTime endDate;
  private String symbol;
  private String types;
}
