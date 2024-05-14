package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.accountsandtrading.model.account.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.accountsandtrading.model.order.Order;
import com.pangility.schwab.api.client.accountsandtrading.model.order.OrderRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.Transaction;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.TransactionRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.userpreference.UserPreferenceResponse;
import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main client for interacting with the Schwab Accounts and Trading API.
 * Use {@code @Autowire} to create the component in any class annotated with
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

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", "accountNumbers");
        return this.callGetApiAsList(uriBuilder);
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

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts");
        if(fields != null) {
            uriBuilder.queryParam("fields", fields);
        }
        return this.callGetApiAsList(uriBuilder);
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

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount);
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

        UriComponentsBuilder uriBuilder = this.getUriBuilder();
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
        return this.callGetApiAsList(uriBuilder);
    }

    /**
     * place an order for a specified account
     * @param encryptedAccount encrypted account id
     * @param order information to place the order
     */
    public void placeOrder(@NotNull String encryptedAccount,
                           @NotNull Order order) {
        log.info("Placing Order for account[{}] -> {}", encryptedAccount, order);

        if(encryptedAccount.isEmpty()) {
            throw new IllegalArgumentException("Encrypted account number is required");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
            .pathSegment("accounts", encryptedAccount);
        this.callPostAPI(uriBuilder, Object.class, order);
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

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "orders", orderId.toString());
        return this.callGetAPI(uriBuilder, Order.class);
    }

    /**
     * fetch the list of transactions for a specified account
     * @param encryptedAccount encrypted account id
     * @param transactionRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Transaction}{@literal >}
     */
    public List<Transaction> fetchTransactions(@NotNull String encryptedAccount,
                                         @NotNull TransactionRequest transactionRequest) {
        log.info("Fetch Transactions {} for {}", transactionRequest, encryptedAccount);

        if(encryptedAccount.isEmpty()) {
            throw new IllegalArgumentException("Encrypted Account is required");
        }
        if(transactionRequest.getStartDate() == null || transactionRequest.getEndDate() == null) {
            throw new IllegalArgumentException("Both Start and End date/times are required");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "transactions")
                .queryParam("startDate", transactionRequest.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")))
                .queryParam("endDate", transactionRequest.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")));
        if(transactionRequest.getSymbol() != null) {
            uriBuilder.queryParam("symbol", transactionRequest.getSymbol());
        }
        if(transactionRequest.getTypes() != null) {
            uriBuilder.queryParam("types", transactionRequest.getTypes());
        }
        return this.callGetApiAsList(uriBuilder);
    }

    /**
     * fetch a transaction for a specified account and activity/transaction id
     * @param encryptedAccount encrypted account id
     * @param activityId activity/transaction id
     * @return {@link Transaction}
     */
    public Transaction fetchTransaction(@NotNull String encryptedAccount,
                                        @NotNull Long activityId) {
        log.info("Fetch a Transaction for account number {} and activity id {}", encryptedAccount, activityId);

        if(encryptedAccount.isEmpty()) {
            throw new IllegalArgumentException("Encrypted Account is required");
        }
        if(activityId <= 0) {
            throw new IllegalArgumentException("Activity ID is required");
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "transactions", activityId.toString());
        return this.callGetAPI(uriBuilder, Transaction.class);
    }

    /**
     * fetch the user preferences
     * @return {@link UserPreferenceResponse}
     */
    public UserPreferenceResponse fetchUserPreference() {
        log.info("Fetch User Preference");

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("userPreference");
        return this.callGetAPI(uriBuilder, UserPreferenceResponse.class);
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion);
    }
}