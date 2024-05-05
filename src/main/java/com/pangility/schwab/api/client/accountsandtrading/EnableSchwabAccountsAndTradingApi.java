package com.pangility.schwab.api.client.accountsandtrading;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to enable the Accounts and Trading Schwab API.
 * Use the annotation at the class level of the class that will be using the methods of the API.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SchwabAccountsAndTradingApiClientConfig.class)
public @interface EnableSchwabAccountsAndTradingApi {
}