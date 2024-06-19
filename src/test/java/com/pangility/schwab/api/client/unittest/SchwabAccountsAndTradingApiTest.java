package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.accountsandtrading.EnableSchwabAccountsAndTradingApi;
import com.pangility.schwab.api.client.accountsandtrading.OrderNotFoundException;
import com.pangility.schwab.api.client.accountsandtrading.SchwabAccountsAndTradingApiClient;
import com.pangility.schwab.api.client.accountsandtrading.TransactionNotFoundException;
import com.pangility.schwab.api.client.accountsandtrading.model.account.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.accountsandtrading.model.order.Order;
import com.pangility.schwab.api.client.accountsandtrading.model.order.OrderRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.Transaction;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.TransactionRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.userpreference.UserPreferenceResponse;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableSchwabAccountsAndTradingApi
@ActiveProfiles("unittest")
public class SchwabAccountsAndTradingApiTest {

    @Value("${schwab-api.oauth2.userId}")
    private String schwabUserId;
    @Value("${schwab-api.oauth2.refreshToken}")
    private String refreshToken;
    @Value("${schwab-api.oauth2.refreshExpiration}")
    private String refreshExpiration;
    private final SchwabAccount schwabAccount = new SchwabAccount();

    @Autowired
    private SchwabAccountsAndTradingApiClient schwabAccountsAndTradingApiClient;
    @Autowired
    private TestTokenHandler testTokenHandler;

    @BeforeEach
    public void setUpEachTest() {
        schwabAccount.setUserId(schwabUserId);
        schwabAccount.setRefreshToken(refreshToken);
        schwabAccount.setRefreshExpiration(LocalDateTime.parse(refreshExpiration, DateTimeFormatter.ISO_DATE_TIME));
        schwabAccountsAndTradingApiClient.init(schwabAccount, testTokenHandler);
    }

    @Test
    public void encryptedAccountsTest() {
        Flux<EncryptedAccount> accountsResponse = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId);
        StepVerifier
                .create(accountsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getAccountNumber() != null && !element.getAccountNumber().isEmpty() &&
                        element.getHashValue() != null && !element.getHashValue().isEmpty())
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void accountTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountNumber = encryptedAccounts.get(0).getAccountNumber();
        String accountHash = encryptedAccounts.get(0).getHashValue();
        Mono<Account> accountResponse = schwabAccountsAndTradingApiClient.fetchAccountToMono(schwabUserId, accountHash);
        StepVerifier
                .create(accountResponse)
                .expectNextMatches(response -> response.getSecuritiesAccount() != null &&
                        response.getSecuritiesAccount().getAccountNumber() != null &&
                        response.getSecuritiesAccount().getAccountNumber().equals(accountNumber))
                .expectComplete()
                .verify();
    }

    @Test
    public void accountWithPositionsTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountNumber = encryptedAccounts.get(0).getAccountNumber();
        String accountHash = encryptedAccounts.get(0).getHashValue();
        Mono<Account> accountResponse = schwabAccountsAndTradingApiClient.fetchAccountToMono(schwabUserId, accountHash, "positions");
        StepVerifier
                .create(accountResponse)
                .expectNextMatches(response -> response.getSecuritiesAccount() != null &&
                        response.getSecuritiesAccount().getAccountNumber() != null &&
                        response.getSecuritiesAccount().getAccountNumber().equals(accountNumber) &&
                        response.getSecuritiesAccount().getPositions() != null)
                .verifyComplete();
    }

    // Schwab returns a 400 - Bad Request rather than a 404 - Not Found for an unknown encrypted account
    /*@Test
    public void accountNotFoundTest() {

        Mono<Account> accountResponse = schwabAccountsAndTradingApiClient.fetchAccountToMono(schwabUserId, "12345678".repeat(8));
        StepVerifier
                .create(accountResponse)
                .expectError(AccountNotFoundException.class)
                .verify();
    }*/

    @Test
    public void accountsTest() {
        Flux<Account> accountsResponse = schwabAccountsAndTradingApiClient.fetchAccountsToFlux(schwabUserId);
        StepVerifier
                .create(accountsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getSecuritiesAccount() != null &&
                        element.getSecuritiesAccount().getAccountNumber() != null &&
                        !element.getSecuritiesAccount().getAccountNumber().isEmpty())
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void accountsWithPositionsTest() {
        Flux<Account> accountsResponse = schwabAccountsAndTradingApiClient.fetchAccountsToFlux(schwabUserId, "positions");
        StepVerifier
                .create(accountsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getSecuritiesAccount() != null &&
                        element.getSecuritiesAccount().getAccountNumber() != null &&
                        !element.getSecuritiesAccount().getAccountNumber().isEmpty() &&
                        element.getSecuritiesAccount().getPositions() != null)
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void accountsNotFoundTest() {
        Flux<Account> accountsResponse = schwabAccountsAndTradingApiClient.fetchAccountsToFlux("InvalidUserId");
        StepVerifier
                .create(accountsResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getSecuritiesAccount() != null &&
                        element.getSecuritiesAccount().getAccountNumber() != null &&
                        !element.getSecuritiesAccount().getAccountNumber().isEmpty())
                .expectRecordedMatches(Collection::isEmpty)
                .verifyComplete();
    }

    @Test
    public void orderByOrderIdTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        Long accountNumber = Long.valueOf(encryptedAccounts.get(0).getAccountNumber());
        String accountHash = encryptedAccounts.get(0).getHashValue();
        Long orderId = 1000292980781L;
        Mono<Order> orderResponse = schwabAccountsAndTradingApiClient.fetchOrderToMono(schwabUserId, accountHash, orderId);
        StepVerifier
                .create(orderResponse)
                .expectNextMatches(response -> response.getAccountNumber() != null &&
                        response.getAccountNumber().equals(accountNumber) &&
                        response.getOrderId() != null &&
                        response.getOrderId().equals(orderId))
                .verifyComplete();
    }

    @Test
    public void orderOrderIdNotFoundTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountHash = encryptedAccounts.get(0).getHashValue();
        Long orderId = 9999999999999L;
        Mono<Order> orderResponse = schwabAccountsAndTradingApiClient.fetchOrderToMono(schwabUserId, accountHash, orderId);
        StepVerifier
                .create(orderResponse)
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    // 400 - Bad Request for an invalid account - no need to test
    /*@Test
    public void orderAccountNotFoundTest() {
        String encryptedAccount = "12345678".repeat(8);
        Long orderId = 1000292980781L;
        Mono<Order> orderResponse = schwabAccountsAndTradingApiClient.fetchOrderToMono(schwabUserId, encryptedAccount, orderId);
        StepVerifier
                .create(orderResponse)
                .expectError(OrderNotFoundException.class)
                .verify();
    }*/

    @Test
    public void ordersTest() {
        OrderRequest orderRequest = OrderRequest.Builder.orderRequest()
                .withFromEnteredDate(ZonedDateTime.now().minusDays(80))
                .withToEnteredDate(ZonedDateTime.now())
                .build();
        Flux<Order> ordersResponse = schwabAccountsAndTradingApiClient.fetchOrdersToFlux(schwabUserId, orderRequest);
        StepVerifier
                .create(ordersResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getAccountNumber() != null &&
                        element.getAccountNumber() > 0 &&
                        element.getOrderId() != null &&
                        element.getOrderId() > 0)
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void ordersForAccountTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        Long accountNumber = Long.valueOf(encryptedAccounts.get(0).getAccountNumber());
        String accountHash = encryptedAccounts.get(0).getHashValue();
        OrderRequest orderRequest = OrderRequest.Builder.orderRequest()
                .withFromEnteredDate(ZonedDateTime.now().minusDays(80))
                .withToEnteredDate(ZonedDateTime.now())
                .build();
        Flux<Order> ordersResponse = schwabAccountsAndTradingApiClient.fetchOrdersToFlux(schwabUserId, accountHash, orderRequest);
        StepVerifier
                .create(ordersResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getAccountNumber() != null &&
                        element.getAccountNumber() > 0 &&
                        element.getAccountNumber().equals(accountNumber) &&
                        element.getOrderId() != null &&
                        element.getOrderId() > 0)
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void transactionForAccountActivityIdTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountNumber = encryptedAccounts.get(0).getAccountNumber();
        String accountHash = encryptedAccounts.get(0).getHashValue();
        Long activityId = 80570034343L;
        Mono<Transaction> transactionResponse = schwabAccountsAndTradingApiClient.fetchTransactionToMono(schwabUserId, accountHash, activityId);
        StepVerifier
                .create(transactionResponse)
                .expectNextMatches(response -> response.getAccountNumber() != null &&
                        response.getAccountNumber().equals(accountNumber) &&
                        response.getActivityId() != null &&
                        response.getActivityId().equals(activityId))
                .verifyComplete();
    }

    @Test
    public void transactionForAccountActivityIdNotFoundTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountHash = encryptedAccounts.get(0).getHashValue();
        Long activityId = 99999999999L;
        Mono<Transaction> transactionResponse = schwabAccountsAndTradingApiClient.fetchTransactionToMono(schwabUserId, accountHash, activityId);
        StepVerifier
                .create(transactionResponse)
                .expectError(TransactionNotFoundException.class)
                .verify();
    }

    @Test
    public void transactionsForAccountTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId).toStream().toList();
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        String accountNumber = encryptedAccounts.get(0).getAccountNumber();
        String accountHash = encryptedAccounts.get(0).getHashValue();
        TransactionRequest transactionRequest = TransactionRequest.Builder.transactionRequest()
                        .withStartDate(ZonedDateTime.now().minusDays(80))
                        .withEndDate(ZonedDateTime.now())
                        .build();
        Flux<Transaction> transactionResponse = schwabAccountsAndTradingApiClient.fetchTransactionsToFlux(schwabUserId, accountHash, transactionRequest);
        StepVerifier
                .create(transactionResponse)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(element -> element.getAccountNumber() != null &&
                        !element.getAccountNumber().isEmpty() &&
                        element.getAccountNumber().equals(accountNumber))
                .expectRecordedMatches(elements -> !elements.isEmpty())
                .verifyComplete();
    }

    @Test
    public void userPreferenceTest() {

        Mono<UserPreferenceResponse> userPreferenceResponseMono = schwabAccountsAndTradingApiClient.fetchUserPreferenceToMono(schwabUserId);
        StepVerifier
                .create(userPreferenceResponseMono)
                .expectNextMatches(response -> response.getAccounts() != null &&
                        !response.getAccounts().isEmpty())
                .verifyComplete();
    }

    /*@Test
    public void placeEquityOrderTest() {
        Optional<EncryptedAccount> optionalEncryptedAccount = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId)
                .toStream()
                .filter(account -> account.getAccountNumber().equals("39760601"))
                .findFirst();
        assertThat(optionalEncryptedAccount).isNotNull();
        assertThat(optionalEncryptedAccount.isPresent()).isTrue();

        EquityInstrument instrument = new EquityInstrument();
        instrument.setAssetType(AssetType.EQUITY);
        instrument.setSymbol("TQQQ");
        OrderLegCollection orderLegCollection = new OrderLegCollection();
        orderLegCollection.setInstruction(OrderLegCollection.Instruction.BUY);
        orderLegCollection.setQuantity(new BigDecimal("525"));
        orderLegCollection.setInstrument(instrument);
        Order order = new Order();
        order.setSession(Session.NORMAL);
        order.setDuration(Duration.DAY);
        order.setOrderType(OrderType.LIMIT);
        order.setComplexOrderStrategyType(ComplexOrderStrategyType.NONE);
        order.setOrderStrategyType(OrderStrategyType.SINGLE);
        order.setPrice(new BigDecimal("63.70"));
        order.setOrderLegCollection(List.of(orderLegCollection));

        Mono<String> orderResponse = schwabAccountsAndTradingApiClient.placeOrder(schwabUserId, optionalEncryptedAccount.get().getHashValue(), order);
        StepVerifier
                .create(orderResponse)
                .verifyComplete();
    }

    @Test
    public void placeOptionOrderTest() {
        Optional<EncryptedAccount> optionalEncryptedAccount = schwabAccountsAndTradingApiClient.fetchEncryptedAccountsToFlux(schwabUserId)
                .toStream()
                .filter(account -> account.getAccountNumber().equals("39760601"))
                .findFirst();
        assertThat(optionalEncryptedAccount).isNotNull();
        assertThat(optionalEncryptedAccount.isPresent()).isTrue();

        EquityInstrument instrument = new EquityInstrument();
        instrument.setAssetType(AssetType.OPTION);
        instrument.setSymbol("TQQQ  240607C00064500");
        OrderLegCollection orderLegCollection = new OrderLegCollection();
        orderLegCollection.setInstruction(OrderLegCollection.Instruction.SELL_TO_OPEN);
        orderLegCollection.setQuantity(new BigDecimal("5"));
        orderLegCollection.setInstrument(instrument);
        Order order = new Order();
        order.setSession(Session.NORMAL);
        order.setDuration(Duration.DAY);
        order.setOrderType(OrderType.LIMIT);
        order.setComplexOrderStrategyType(ComplexOrderStrategyType.NONE);
        order.setOrderStrategyType(OrderStrategyType.SINGLE);
        order.setPrice(new BigDecimal("1.0"));
        order.setOrderLegCollection(List.of(orderLegCollection));

        Mono<String> orderResponse = schwabAccountsAndTradingApiClient.placeOrder(schwabUserId, optionalEncryptedAccount.get().getHashValue(), order);
        StepVerifier
                .create(orderResponse)
                .verifyComplete();
    }*/

    public static class TestTokenHandler implements SchwabTokenHandler {

        @Override
        public void onAccessTokenChange(@NonNull SchwabAccount schwabAccount) {
            System.out.println("Testing - Access Token Change");
        }

        @Override
        public void onRefreshTokenChange(@NonNull SchwabAccount schwabAccount) {
            System.out.println("Testing - Refresh Token Change");
        }
    }

    @SpringBootConfiguration
    public static class TestConfig {
        @Bean
        public TestTokenHandler testTokenHandler() {
            return new TestTokenHandler();
        }
    }
}
