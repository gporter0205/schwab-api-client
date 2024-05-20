package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * StopPriceLinkBasis
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum StopPriceLinkBasis {
  /**
   * Manual
   */
  MANUAL,
  /**
   * Base
   */
  BASE,
  /**
   * Trigger
   */
  TRIGGER,
  /**
   * Last
   */
  LAST,
  /**
   * Bid
   */
  BID,
  /**
   * Ask
   */
  ASK,
  /**
   * Ask/Bid
   */
  ASK_BID,
  /**
   * Mark
   */
  MARK,
  /**
   * Average
   */
  AVERAGE
}