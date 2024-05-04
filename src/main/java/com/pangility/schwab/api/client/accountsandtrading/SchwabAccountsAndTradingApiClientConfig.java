package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.common.SchwabWebClient;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration used for enabling the Accounts and Trading API
 */
@ComponentScan(basePackageClasses = {SchwabAccountsAndTradingApiClient.class, SchwabOauth2Controller.class, SchwabWebClient.class})
@PropertySource(value = "classpath:schwabapiclient.properties")
public class SchwabAccountsAndTradingApiClientConfig {
}