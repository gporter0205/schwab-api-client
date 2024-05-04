package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.common.SchwabWebClient;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabOauth2Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Main client for interacting with the Schwab Accounts and Trading API.
 * Use {@literal @}Autowire to create the component in any class annotated with
 * {@literal @}EnableSchwabAccountsAndTradingApi or {@literal @}EnableSchwabApi
 */
@Service
@ConditionalOnResource(resources = {"classpath:schwabapiclient.properties"})
@Slf4j
public class SchwabAccountsAndTradingApiClient {

    @Value("${schwab-api.targetUrl}")
    private String schwabTargetUrl;
    @Value("${schwab-api.apiVersion}")
    private String schwabApiVersion;
    @Value("${schwab-api.traderPath}")
    private String schwabTraderPath;

    @Autowired
    private SchwabOauth2Controller schwabOauth2Controller;
    @Autowired
    private SchwabWebClient schwabWebClient;

    /**
     * initialize the Accounts and Trading Schwab API client controller.
     * @param schwabAccount {@link SchwabAccount}
     */
    public void init(SchwabAccount schwabAccount) {
        this.init(Collections.singletonList(schwabAccount));
    }

    /**
     * initialize the Accounts and Trading Schwab API client controller.
     * @param schwabAccounts List{@literal <}{@link SchwabAccount}{@literal >}
     */
    public void init(List<SchwabAccount> schwabAccounts) {
        schwabOauth2Controller.init(schwabAccounts);
    }
}