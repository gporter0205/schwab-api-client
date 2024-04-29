package com.schwabapi.accountsandtrading;

import com.schwabapi.oauth2.SchwabAccount;
import com.schwabapi.oauth2.SchwabOauth2Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
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
    @Qualifier("schwabAccountsAndTradingWebClient")
    private WebClient schwabWebClient;

    public void init(SchwabAccount schwabAccount) {
        this.init(Collections.singletonList(schwabAccount));
    }
    public void init(List<SchwabAccount> schwabAccounts) {
        schwabOauth2Controller.init(schwabAccounts);
    }
}