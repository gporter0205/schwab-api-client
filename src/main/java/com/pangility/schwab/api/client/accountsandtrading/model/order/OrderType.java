package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * OrderType
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum OrderType {
  /**
   * Market
   */
  MARKET,
  /**
   * Limit
   */
  LIMIT,
  /**
   * Stop
   */
  STOP,
  /**
   * Stop Limit
   */
  STOP_LIMIT,
  /**
   * Trailing Stop
   */
  TRAILING_STOP,
  /**
   * Market on Close
   */
  MARKET_ON_CLOSE,
  /**
   * Exercise
   */
  EXERCISE,
  /**
   * Trailing Stop Limit
   */
  TRAILING_STOP_LIMIT,
  /**
   * Net Debit
   */
  NET_DEBIT,
  /**
   * Net Credit
   */
  NET_CREDIT,
  /**
   * Net Zero
   */
  NET_ZERO
}