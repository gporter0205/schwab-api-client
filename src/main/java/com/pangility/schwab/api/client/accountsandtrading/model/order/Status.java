package com.pangility.schwab.api.client.accountsandtrading.model.order;

/**
 * Status
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
public enum Status {
  /**
   * Awaiting Parent Order
   */
  AWAITING_PARENT_ORDER,
  /**
   * Awaiting Condition
   */
  AWAITING_CONDITION,
  /**
   * Awaiting Stop Condition
   */
  AWAITING_STOP_CONDITION,
  /**
   * Awaiting Manual Review
   */
  AWAITING_MANUAL_REVIEW,
  /**
   * Accepted
   */
  ACCEPTED,
  /**
   * Awaiting UR Out
   */
  AWAITING_UR_OUT,
  /**
   * Pending Activation
   */
  PENDING_ACTIVATION,
  /**
   * Queued
   */
  QUEUED,
  /**
   * Working
   */
  WORKING,
  /**
   * Rejected
   */
  REJECTED,
  /**
   * Pending Cancel
   */
  PENDING_CANCEL,
  /**
   * Canceled
   */
  CANCELED,
  /**
   * Pending Replace
   */
  PENDING_REPLACE,
  /**
   * Replaced
   */
  REPLACED,
  /**
   * Filled
   */
  FILLED,
  /**
   * Expired
   */
  EXPIRED,
  /**
   * New
   */
  NEW,
  /**
   * Awaiting Release Time
   */
  AWAITING_RELEASE_TIME,
  /**
   * Pending Acknowledgement
   */
  PENDING_ACKNOWLEDGEMENT,
  /**
   * Pending Recall
   */
  PENDING_RECALL,
  /**
   * Unknown
   */
  UNKNOWN
}
