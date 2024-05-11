package com.pangility.schwab.api.client.unittest;

import com.pangility.schwab.api.client.accountsandtrading.EnableSchwabAccountsAndTradingApi;
import com.pangility.schwab.api.client.accountsandtrading.SchwabAccountsAndTradingApiClient;
import com.pangility.schwab.api.client.accountsandtrading.model.accounts.Account;
import com.pangility.schwab.api.client.accountsandtrading.model.encryptedaccounts.EncryptedAccount;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        List<EncryptedAccount> response = schwabAccountsAndTradingApiClient.fetchEncryptedAccounts();
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);
    }

    @Test
    public void accountsTest() {
        List<Account> response = schwabAccountsAndTradingApiClient.fetchAccounts();
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);

        response = schwabAccountsAndTradingApiClient.fetchAccounts("positions");
        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThan(0);
    }

    public static class TestTokenHandler implements SchwabTokenHandler {

        @Override
        public void onAccessTokenChange(@NotNull SchwabAccount schwabAccount) {
            System.out.println("Test Access Token Change");
        }

        @Override
        public void onRefreshTokenChange(@NotNull SchwabAccount schwabAccount) {
            System.out.println("Test Refresh Token Change");
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
