package com.pangility.schwab.api.client.accountsandtrading;

import com.pangility.schwab.api.client.accountsandtrading.model.account.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.accountsandtrading.model.order.Order;
import com.pangility.schwab.api.client.accountsandtrading.model.order.OrderRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.Transaction;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.TransactionRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.userpreference.UserPreferenceResponse;
import com.pangility.schwab.api.client.common.SchwabBaseApiClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link List}{@literal <}{@link EncryptedAccount}{@literal >}
     */
    @Deprecated
    public List<EncryptedAccount> fetchEncryptedAccounts(@NonNull String schwabUserId) {
        return this.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
    }

    /**
     * fetch the list of accounts and encrypted account numbers to be
     * used for subsequent API requests that require account number;
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link List}{@literal <}{@link EncryptedAccount}{@literal >}
     */
    public Flux<EncryptedAccount> fetchEncryptedAccountsToFlux(@NonNull String schwabUserId) {
        log.info("Fetch Encrypted Accounts");

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", "accountNumbers");
        return this.callGetApiToFlux(schwabUserId, uriBuilder, EncryptedAccount.class);
    }

    /**
     * fetch an account without position data
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    @Deprecated
    public Account fetchAccount(@NonNull String schwabUserId,
                                @NonNull String encryptedAccount) {
        return fetchAccount(schwabUserId, encryptedAccount, null);
    }

    /**
     * fetch an account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param fields positions to include account position data or null
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    @Deprecated
    public Account fetchAccount(@NonNull String schwabUserId,
                                @NonNull String encryptedAccount,
                                String fields) {
        return this.fetchAccountToMono(schwabUserId, encryptedAccount, fields).block();
    }

    /**
     * reactively fetch an account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @return {@link Mono}{@literal <}{@link Account}{@literal >}
     */
    public Mono<Account> fetchAccountToMono(@NonNull String schwabUserId,
                                            @NonNull String encryptedAccount) {
        return this.fetchAccountToMono(schwabUserId, encryptedAccount, null);
    }

    /**
     * reactively fetch an account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param fields positions to include account position data or null
     * @return {@link Mono}{@literal <}{@link Account}{@literal >}
     */
    public Mono<Account> fetchAccountToMono(@NonNull String schwabUserId,
                                            @NonNull String encryptedAccount,
                                            String fields) {
        log.info("Fetch Account [{}] {}", encryptedAccount, fields != null && fields.equalsIgnoreCase("positions") ? "with positions" : "");

        if(encryptedAccount.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Encrypted Account must not be empty"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount);
        if(fields != null) {
            uriBuilder.queryParam("fields", fields);
        }
        return this.callGetApiToMono(schwabUserId, uriBuilder, Account.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new AccountNotFoundException("Encrypted Account '" + encryptedAccount + "' not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(accountResponse -> {
                    if(accountResponse.getSecuritiesAccount().getAccountNumber() != null && !accountResponse.getSecuritiesAccount().getAccountNumber().isEmpty()) {
                        return Mono.just(accountResponse);
                    } else {
                        return Mono.error(new AccountNotFoundException("Encrypted Account '" + encryptedAccount + "' not found"));
                    }
                });
    }

    /**
     * fetch the list of accounts without positions
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    @Deprecated
    public List<Account> fetchAccounts(@NonNull String schwabUserId) {
        return fetchAccounts(schwabUserId, null);
    }

    /**
     * fetch the list of accounts
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param fields positions to include account position data or null
     * @return {@link List}{@literal <}{@link Account}{@literal >}
     */
    @Deprecated
    public List<Account> fetchAccounts(@NonNull String schwabUserId,
                                       String fields) {
        return this.fetchAccountsToFlux(schwabUserId, fields).toStream().toList();
    }

    /**
     * reactively fetch the list of accounts without positions
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link Flux}{@literal <}{@link Account}{@literal >}
     */
    public Flux<Account> fetchAccountsToFlux(@NonNull String schwabUserId) {
        return fetchAccountsToFlux(schwabUserId, null);
    }

    /**
     * reactively fetch the list of accounts
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param fields positions to include account position data or null
     * @return {@link Flux}{@literal <}{@link Account}{@literal >}
     */
    public Flux<Account> fetchAccountsToFlux(@NonNull String schwabUserId,
                                       String fields) {
        log.info("Fetch All Accounts {}", fields != null && fields.equalsIgnoreCase("positions") ? "with positions" : "");

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts");
        if(fields != null) {
            uriBuilder.queryParam("fields", fields);
        }
        return this.callGetApiToFlux(schwabUserId, uriBuilder, Account.class);
    }

    /**
     * fetch an order for a specified account and order id
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderId order id to fetch
     * @return {@link Order}
     */
    @Deprecated
    public Order fetchOrder(@NonNull String schwabUserId,
                            @NonNull String encryptedAccount,
                            @NonNull Long orderId) {
        return this.fetchOrderToMono(schwabUserId, encryptedAccount, orderId).block();
    }

    /**
     * reactively fetch an order for a specified account and order id
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderId order id to fetch
     * @return {@link Mono}{@literal <}{@link Order}{@literal >}
     */
    public Mono<Order> fetchOrderToMono(@NonNull String schwabUserId,
                                        @NonNull String encryptedAccount,
                                        @NonNull Long orderId) {
        log.info("Fetch Order [{}] for Encrypted Account [{}]", orderId, encryptedAccount);

        if(encryptedAccount.isEmpty() || orderId <= 0) {
            return Mono.error(new IllegalArgumentException("Account Number and Order ID are required"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "orders", orderId.toString());
        return this.callGetApiToMono(schwabUserId, uriBuilder, Order.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new OrderNotFoundException("Order [" + orderId + "] on Encrypted Account [" + encryptedAccount + "] not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(orderResponse -> {
                    if(orderResponse.getAccountNumber() > 0 && orderResponse.getOrderId().equals(orderId)) {
                        return Mono.just(orderResponse);
                    } else {
                        return Mono.error(new OrderNotFoundException("Order [" + orderId + "] on Encrypted Account [" + encryptedAccount + "] not found"));
                    }
                });
    }

    /**
     * fetch the list of orders for all accounts
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Order}{@literal >}
     */
    @Deprecated
    public List<Order> fetchOrders(@NonNull String schwabUserId,
                                   @NonNull OrderRequest orderRequest) {
        return this.fetchOrders(schwabUserId, null, orderRequest);
    }

    /**
     * fetch the list of orders for all accounts or a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Order}{@literal >}
     */
    @Deprecated
    public List<Order> fetchOrders(@NonNull String schwabUserId,
                                   String encryptedAccount,
                                   @NonNull OrderRequest orderRequest) {
        return this.fetchOrdersToFlux(schwabUserId, encryptedAccount, orderRequest).toStream().toList();
    }

    /**
     * reactively fetch the list of orders for all accounts
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link Flux}{@literal <}{@link Order}{@literal >}
     */
    public Flux<Order> fetchOrdersToFlux(@NonNull String schwabUserId,
                                       @NonNull OrderRequest orderRequest) {
        return this.fetchOrdersToFlux(schwabUserId, null, orderRequest);
    }

    /**
     * reactively fetch the list of orders for all accounts or a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link Flux}{@literal <}{@link Order}{@literal >}
     */
    public Flux<Order> fetchOrdersToFlux(@NonNull String schwabUserId,
                                           String encryptedAccount,
                                           @NonNull OrderRequest orderRequest) {
        log.info("Fetch Orders for Account [{}] -> {}", encryptedAccount == null ? "all accounts" : encryptedAccount, orderRequest);

        if(orderRequest.getFromEnteredTime() == null || orderRequest.getToEnteredTime() == null) {
            return Flux.error(new IllegalArgumentException("Both From and To Entered date/times are required"));
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
        return this.callGetApiToFlux(schwabUserId, uriBuilder, Order.class);
    }

    /**
     * fetch a transaction for a specified account and activity/transaction id
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param activityId activity/transaction id
     * @return {@link Transaction}
     */
    @Deprecated
    public Transaction fetchTransaction(@NonNull String schwabUserId,
                                        @NonNull String encryptedAccount,
                                        @NonNull Long activityId) {
        return this.fetchTransactionToMono(schwabUserId, encryptedAccount, activityId).block();
    }

    /**
     * reactively fetch a transaction for a specified account and activity/transaction id
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param activityId activity/transaction id
     * @return {@link Mono}{@literal <}{@link Transaction}{@literal >}
     */
    public Mono<Transaction> fetchTransactionToMono(@NonNull String schwabUserId,
                                            @NonNull String encryptedAccount,
                                            @NonNull Long activityId) {
        log.info("Fetch Transaction [{}] for Account [{}]", activityId, encryptedAccount);

        if(encryptedAccount.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Encrypted Account is required"));
        }
        if(activityId <= 0) {
            return Mono.error(new IllegalArgumentException("Activity ID is required"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "transactions", activityId.toString());
        return this.callGetApiToMono(schwabUserId, uriBuilder, Transaction.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new TransactionNotFoundException("Transaction [" + activityId + "] on Encrypted Account [" + encryptedAccount + "] not found"));
                        }
                    }
                    return Mono.error(throwable);
                })
                .flatMap(transactionResponse -> {
                    if(transactionResponse.getAccountNumber() != null && !transactionResponse.getAccountNumber().isEmpty()) {
                        return Mono.just(transactionResponse);
                    } else {
                        return Mono.error(new TransactionNotFoundException("Transaction [" + activityId + "] on Encrypted Account [" + encryptedAccount + "] not found"));
                    }
                });
    }

    /**
     * fetch the list of transactions for a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param transactionRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link List}{@literal <}{@link Transaction}{@literal >}
     */
    @Deprecated
    public List<Transaction> fetchTransactions(@NonNull String schwabUserId,
                                               @NonNull String encryptedAccount,
                                               @NonNull TransactionRequest transactionRequest) {
        return this.fetchTransactionsToFlux(schwabUserId, encryptedAccount, transactionRequest).toStream().toList();
    }

    /**
     * reactively fetch the list of transactions for a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param transactionRequest parameters of the orders.  FromEnteredDate and ToEnteredDate are required.
     * @return {@link Flux}{@literal <}{@link Transaction}{@literal >}
     */
    public Flux<Transaction> fetchTransactionsToFlux(@NonNull String schwabUserId,
                                                   @NonNull String encryptedAccount,
                                                   @NonNull TransactionRequest transactionRequest) {
        log.info("Fetch Transactions for Account [{}] -> {}", encryptedAccount, transactionRequest);

        if(encryptedAccount.isEmpty()) {
            return Flux.error(new IllegalArgumentException("Encrypted Account is required"));
        }
        if(transactionRequest.getStartDate() == null || transactionRequest.getEndDate() == null) {
            return Flux.error(new IllegalArgumentException("Both Start and End date/times are required"));
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
        return this.callGetApiToFlux(schwabUserId, uriBuilder, Transaction.class)
                .onErrorResume(throwable -> {
                    if(throwable instanceof WebClientResponseException) {
                        if(((WebClientResponseException) throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                            return Flux.error(new TransactionNotFoundException("Transactions on Encrypted Account [" + encryptedAccount + "] not found"));
                        }
                    }
                    return Flux.error(throwable);
                });
    }

    /**
     * fetch the user preferences
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link UserPreferenceResponse}
     */
    @Deprecated
    public UserPreferenceResponse fetchUserPreference(@NonNull String schwabUserId) {
        return this.fetchUserPreferenceToMono(schwabUserId).block();
    }

    /**
     * fetch the user preferences
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @return {@link UserPreferenceResponse}
     */
    public Mono<UserPreferenceResponse> fetchUserPreferenceToMono(@NonNull String schwabUserId) {
        log.info("Fetch User Preference");

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("userPreference");
        return this.callGetApiToMono(schwabUserId, uriBuilder, UserPreferenceResponse.class);
    }

    /**
     * place a new order for a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param order information to place the order
     * @return {@link Mono}{@literal <}String{@literal >}
     */
    public Mono<String> placeOrder(@NonNull String schwabUserId,
                           @NonNull String encryptedAccount,
                           @NonNull Order order) {
        log.info("Place Order on Account [{}] -> {}", encryptedAccount, order);

        if(encryptedAccount.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Encrypted account number is required"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "orders");
        return this.callPostApiToMono(schwabUserId, uriBuilder, order, String.class);
    }

    /**
     * replace a specified order for a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderId order to be replaced
     * @param order replacement order information
     * @return {@link Mono}{@literal <}String{@literal >}
     */
    public Mono<String> replaceOrder(@NonNull String schwabUserId,
                             @NonNull String encryptedAccount,
                             @NonNull Long orderId,
                             @NonNull Order order) {
        log.info("Replace Order [{}] on Account [{}] -> {}", orderId, encryptedAccount, order);

        if(encryptedAccount.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Encrypted account number is required"));
        }
        if(orderId <= 0) {
            return Mono.error(new IllegalArgumentException("Order ID is required"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "orders", orderId.toString());
        return this.callPutApiToMono(schwabUserId, uriBuilder, order, String.class);
    }

    /**
     * cancel a specified order for a specified account
     * @param schwabUserId the Charles Schwab user id of the account to be used for API authentication
     * @param encryptedAccount encrypted account id
     * @param orderId order to be cancelled
     * @return {@link Mono}{@literal <}String{@literal >}
     */
    public Mono<String> cancelOrder(@NonNull String schwabUserId,
                            @NonNull String encryptedAccount,
                            @NonNull Long orderId) {
        log.info("Cancel Order [{}] on Account [{}]", orderId, encryptedAccount);

        if(encryptedAccount.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Encrypted account number is required"));
        }
        if(orderId <= 0) {
            return Mono.error(new IllegalArgumentException("Order Id is required"));
        }

        UriComponentsBuilder uriBuilder = this.getUriBuilder()
                .pathSegment("accounts", encryptedAccount, "orders", orderId.toString());
        return this.callDeleteApiToMono(schwabUserId, uriBuilder, null, String.class);
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .pathSegment(schwabTraderPath, schwabApiVersion);
    }
}