package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.accountsandtrading.EnableSchwabAccountsAndTradingApi;
import com.pangility.schwab.api.client.accountsandtrading.SchwabAccountsAndTradingApiClient;
import com.pangility.schwab.api.client.accountsandtrading.model.account.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
import com.pangility.schwab.api.client.accountsandtrading.model.order.Order;
import com.pangility.schwab.api.client.accountsandtrading.model.order.OrderRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.Transaction;
import com.pangility.schwab.api.client.accountsandtrading.model.transaction.TransactionRequest;
import com.pangility.schwab.api.client.accountsandtrading.model.userpreference.UserPreferenceResponse;
import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import com.pangility.schwab.api.client.oauth2.SchwabTokenHandler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
        List<EncryptedAccount> response = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);
    }

    @Test
    public void placeOrderTest() {
        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        try {
            schwabAccountsAndTradingApiClient.placeOrder(schwabUserId, encryptedAccounts.get(0).getHashValue(), new Order());
        } catch(WebClientResponseException e) {
            // Bad Request because we've sent an empty order
            assertThat(e).isNotNull();
            assertThat(e.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        } catch(Exception e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void accountsTest() {
        List<Account> response = schwabAccountsAndTradingApiClient.fetchAccounts(schwabUserId);
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);

        response = schwabAccountsAndTradingApiClient.fetchAccounts(schwabUserId, "positions");
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);
    }

    @Test
    public void accountTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        Account response = schwabAccountsAndTradingApiClient.fetchAccount(schwabUserId, encryptedAccounts.get(0).getHashValue());
        assertThat(response).isNotNull();
        assertThat(response.getSecuritiesAccount().getAccountNumber()).isEqualToIgnoringCase(encryptedAccounts.get(0).getAccountNumber());

        response = schwabAccountsAndTradingApiClient.fetchAccount(schwabUserId, encryptedAccounts.get(0).getHashValue(), "positions");
        assertThat(response).isNotNull();
        assertThat(response.getSecuritiesAccount().getAccountNumber()).isEqualToIgnoringCase(encryptedAccounts.get(0).getAccountNumber());
    }

    @Test
    public void ordersTest() {

        OrderRequest orderRequest = OrderRequest.Builder.orderRequest()
                .withFromEnteredDate(ZonedDateTime.now().minusDays(80))
                .withToEnteredDate(ZonedDateTime.now())
                .build();
        List<Order> response = schwabAccountsAndTradingApiClient.fetchOrders(schwabUserId, orderRequest);
        assertThat(response).isNotNull();
    }

    @Test
    public void ordersForAccountTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        OrderRequest orderRequest = OrderRequest.Builder.orderRequest()
                .withFromEnteredDate(ZonedDateTime.now().minusDays(80))
                .withToEnteredDate(ZonedDateTime.now())
                .build();
        List<Order> response = schwabAccountsAndTradingApiClient.fetchOrders(schwabUserId, encryptedAccounts.get(0).getHashValue(), orderRequest);
        assertThat(response).isNotNull();
    }

    @Test
    public void orderByOrderIdTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        Order response = schwabAccountsAndTradingApiClient.fetchOrder(schwabUserId, encryptedAccounts.get(0).getHashValue(), 1000292980781L);
        assertThat(response).isNotNull();
        assertThat(response.getOrderId()).isEqualTo(1000292980781L);
    }

    @Test
    public void transactionsForAccountTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        TransactionRequest transactionRequest = TransactionRequest.Builder.transactionRequest()
                        .withStartDate(ZonedDateTime.now().minusDays(80))
                        .withEndDate(ZonedDateTime.now())
                        .build();
        List<Transaction> response = schwabAccountsAndTradingApiClient.fetchTransactions(schwabUserId, encryptedAccounts.get(0).getHashValue(), transactionRequest);
        assertThat(response).isNotNull();
    }

    @Test
    public void transactionForAccountActivityIdTest() {

        List<EncryptedAccount> encryptedAccounts = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts(schwabUserId);
        assertThat(encryptedAccounts).isNotNull();
        assertThat(encryptedAccounts.size()).isGreaterThan(0);

        Transaction response = schwabAccountsAndTradingApiClient.fetchTransaction(schwabUserId, encryptedAccounts.get(0).getHashValue(), 80570034343L);
        assertThat(response).isNotNull();
    }

    @Test
    public void userPreferenceTest() {

        UserPreferenceResponse response = schwabAccountsAndTradingApiClient.fetchUserPreference(schwabUserId);
        assertThat(response).isNotNull();
    }

    public static class TestTokenHandler implements SchwabTokenHandler {

        @Override
        public void onAccessTokenChange(@NotNull SchwabAccount schwabAccount) {
            System.out.println("Testing - Access Token Change");
        }

        @Override
        public void onRefreshTokenChange(@NotNull SchwabAccount schwabAccount) {
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
