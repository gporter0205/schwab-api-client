package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.accountsandtrading.EnableSchwabAccountsAndTradingApi;
import com.pangility.schwab.api.client.marketdata.EnableSchwabMarketDataApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to enable both the Market Data and Accounts and Trading Schwab API's.
 * Use the annotation at the class level of the class that will be using the methods of the API.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableSchwabMarketDataApi
@EnableSchwabAccountsAndTradingApi
public @interface EnableSchwabApi {
}
