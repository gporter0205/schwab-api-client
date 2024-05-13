package com.pangility.schwab.api.client.accountsandtrading.model.order;

public enum OrderType {
  MARKET,
  LIMIT,
  STOP,
  STOP_LIMIT,
  TRAILING_STOP,
  MARKET_ON_CLOSE,
  EXERCISE,
  TRAILING_STOP_LIMIT,
  NET_DEBIT,
  NET_CREDIT,
  NET_ZERO
}