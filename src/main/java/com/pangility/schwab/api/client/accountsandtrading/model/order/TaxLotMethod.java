package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * TaxLotMethod
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum TaxLotMethod {
  /**
   * First-in-First-out
   */
  FIFO,
  /**
   * Last-in-First-out
   */
  LIFO,
  /**
   * High Cost
   */
  HIGH_COST,
  /**
   * Low Cost
   */
  LOW_COST,
  /**
   * Average Cost
   */
  AVERAGE_COST,
  /**
   * Specific Lot
   */
  SPECIFIC_LOT
}