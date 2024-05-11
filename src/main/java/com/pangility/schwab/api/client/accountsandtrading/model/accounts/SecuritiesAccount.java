package com.pangility.schwab.api.client.accountsandtrading.model.accounts;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TDA SecuritiesAccount. This is an abstract class and you need to check the {@link
 * Type} to cast to either a {@link CashAccount} or {@link MarginAccount}. For
 * example:
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
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
    use = Id.NAME,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MarginAccount.class, name = "MARGIN"),
    @JsonSubTypes.Type(value = CashAccount.class, name = "CASH"),
})
public class SecuritiesAccount {
  private Type type;
  private String accountNumber;
  private Long roundTrips;
  private Boolean isDayTrader;
  private Boolean isClosingOnlyRestricted;
  private Boolean pfcbFlag;
  private List<Position> positions = new ArrayList<>();
  //private List<OrderStrategy> orderStrategies = new ArrayList<>();
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public enum Type {
    CASH,
    MARGIN
  }
}
