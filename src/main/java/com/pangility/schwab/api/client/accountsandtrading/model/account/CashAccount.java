package com.pangility.schwab.api.client.accountsandtrading.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * CashAccount. Most calls return an abstract {@link SecuritiesAccount} instead of the concrete
 * account type so you need to cast to either a {@link CashAccount} or
 * {@link MarginAccount}. For example:
 * <pre class="code">
 *   SecuritiesAccount account = tdaClient.getAccount("2342..");
 *   if (account.getType == SecuritiesAccount.Type.Cash){
 *     CashAccount cashAcct = (CashAccount)account;
 *   }
 *   else if (account.getType == SecuritiesAccount.Type.Margin){
 *       MarginAccount marginAcct = (MarginAccount)account;
 *   }
 *   ...
 * </pre>
 *
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashAccount extends SecuritiesAccount {
  CashCurrentBalances currentBalances;
  CashInitialBalances initialBalances;
  CashProjectedBalances projectedBalances;
}
