package com.pangility.schwab.api.client.marketdata;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to enable both the Market Data Schwab API.
 * Use the annotation at the class level of the class that will be using the methods of the API.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SchwabMarketDataApiClientConfig.class})
public @interface EnableSchwabMarketDataApi {
}