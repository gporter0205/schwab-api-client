package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * OrderStrategyType - Single, One-Cancels-the-Other, Trigger.
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum OrderStrategyType {
  /**
   * Single
   */
  SINGLE,
  /**
   * One-Cancels-the-Other
   */
  OCO,
  /**
   * Trigger
   */
  TRIGGER
}