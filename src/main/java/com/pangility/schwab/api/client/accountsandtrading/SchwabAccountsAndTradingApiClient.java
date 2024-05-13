package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.accountsandtrading.model.account.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.accountsandtrading.model.order.Order;
import com.pangility.schwab.api.client.accountsandtrading.model.order.OrderRequest;
import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;
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

    /**
     * fetch the list of orders for all accounts
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Order}{@literal >}
     */
    public List<Order> fetchOrders(@NotNull OrderRequest orderRequest) {
        return fetchOrders(null, orderRequest);
    }

    /**
     * fetch the list of orders for all accounts or a specified account
     * @param encryptedAccount encrypted account id
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Order}{@literal >}
     */
    public List<Order> fetchOrders(String encryptedAccount,
                                   @NotNull OrderRequest orderRequest) {
        log.info("Fetch Orders {} for {}", orderRequest, encryptedAccount == null ? "all accounts" : encryptedAccount);

        if(orderRequest.getFromEnteredTime() == null || orderRequest.getToEnteredTime() == null) {
            throw new IllegalArgumentException("Both From and To Entered date/times are required");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion);
        if(encryptedAccount != null) {
            uriBuilder.pathSegment("accounts", encryptedAccount);
        }
        uriBuilder.pathSegment( "orders")
                .queryParam("fromEnteredTime", orderRequest.getFromEnteredTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")))
                .queryParam("toEnteredTime", orderRequest.getToEnteredTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")));
        if(orderRequest.getMaxResults() != null) {
            uriBuilder.queryParam("maxResults", orderRequest.getMaxResults());
        }
        if(orderRequest.getStatus() != null) {
            uriBuilder.queryParam("status", orderRequest.getStatus());
        }
        return this.callGetApiAsList(uriBuilder, new ParameterizedTypeReference<>() {});
    }

    /**
     * fetch an order for a specified account and order id
     * @param encryptedAccount encrypted account id
     * @param orderId order id to fetch
     * @return {@link Order}
     */
    public Order fetchOrder(@NotNull String encryptedAccount,
                                   @NotNull Long orderId) {
        log.info("Fetch an Order for account number {} and order id {}", encryptedAccount, orderId);

        if(encryptedAccount.isEmpty() || orderId <= 0) {
            throw new IllegalArgumentException("Account Number and Order ID are required");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion, "accounts", encryptedAccount, "orders", orderId.toString());
        return this.callGetAPI(uriBuilder, Order.class);
    }
}