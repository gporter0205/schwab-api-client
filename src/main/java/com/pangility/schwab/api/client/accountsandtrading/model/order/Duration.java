package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * Duration
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum Duration {
  /**
   * Day
   */
  DAY,
  /**
   * Good Till Cancel
   */
  GOOD_TILL_CANCEL,
  /**
   * Fill or Kill
   */
  FILL_OR_KILL
}