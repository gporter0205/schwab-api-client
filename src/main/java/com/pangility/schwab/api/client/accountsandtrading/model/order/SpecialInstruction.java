package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * SpecialInstruction
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum SpecialInstruction {
  /**
   * All or None
   */
  ALL_OR_NONE,
  /**
   * Do Not Reduce
   */
  DO_NOT_REDUCE,
  /**
   * All or None/Do Not Reduce
   */
  ALL_OR_NONE_DO_NOT_REDUCE
}