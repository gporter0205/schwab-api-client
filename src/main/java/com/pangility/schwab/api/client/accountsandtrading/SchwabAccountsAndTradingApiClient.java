package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.accountsandtrading.model.accounts.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Main client for interacting with the Schwab Accounts and Trading API.
 * Use {@literal @}Autowire to create the component in any class annotated with
 * {@literal @}EnableSchwabAccountsAndTradingApi or {@literal @}EnableSchwabApi
 */
@Service
@ConditionalOnResource(resources = {"classpath:schwabapiclient.properties"})
@Slf4j
public class SchwabAccountsAndTradingApiClient extends SchwabBaseApiClient {

    @Value("${schwab-api.traderPath}")
    private String schwabTraderPath;

    /**
     * fetch the list of accounts and encrypted account numbers to be
     * used for subsequent API requests that require account number;
     * @return {@link List}{@literal <}{@link EncryptedAccount}{@literal >}
     */
    public List<EncryptedAccount> fetchEncryptedAccounts() {
        log.info("Fetch Encrypted Accounts");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion, "accounts", "accountNumbers");
        return this.callGetApiAsList(uriBuilder, new ParameterizedTypeReference<>() {});
    }

    /**
     * fetch the list of accounts without positions
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    public List<Account> fetchAccounts() {
        return fetchAccounts(null);
    }

    /**
     * fetch the list of accounts
     * @param fields positions to include account position data or null
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    public List<Account> fetchAccounts(String fields) {
        log.info("Fetch Accounts");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion, "accounts");
        if(fields != null) {
            uriBuilder.queryParam("fields", fields);
        }
        return this.callGetApiAsList(uriBuilder, new ParameterizedTypeReference<>() {});
    }

    /**
     * fetch an account without position data
     * @param encryptedAccount encrypted account id
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    public Account fetchAccount(@NotNull String encryptedAccount) {
        return fetchAccount(encryptedAccount, null);
    }

    /**
     * fetch an account
     * @param encryptedAccount encrypted account id
     * @param fields positions to include account position data or null
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    public Account fetchAccount(@NotNull String encryptedAccount,
                                      String fields) {
        log.info("Fetch Accounts");

        if(encryptedAccount.isEmpty()) {
            throw new IllegalArgumentException("Encrypted Account must not be empty");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion, "accounts", encryptedAccount);
        if(fields != null) {
            uriBuilder.queryParam("fields", fields);
        }
        return this.callGetAPI(uriBuilder, Account.class);
    }
}