package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.accountsandtrading.SchwabAccountsAndTradingApiClient;
import com.pangility.schwab.api.client.marketdata.SchwabMarketDataApiClient;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

public class OnApiEnabledCondition extends AnyNestedCondition {

    OnApiEnabledCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnBean(SchwabMarketDataApiClient.class)
    static class onMarketData {}

    @ConditionalOnBean(SchwabAccountsAndTradingApiClient.class)
    static class onAccountsAndTrading {}
}
