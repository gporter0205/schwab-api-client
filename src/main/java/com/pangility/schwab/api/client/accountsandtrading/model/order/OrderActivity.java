package com.pangility.schwab.api.client.accountsandtrading.model.order;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Order Activity
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderActivity {
  private ActivityType activityType;
  private ExecutionType executionType;
  private BigDecimal quantity;
  private BigDecimal orderRemainingQuantity;
  private List<ExecutionLeg> executionLegs = new ArrayList<>();
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  /**
   * Execution Type
   */
  public enum ExecutionType {
    /**
     * Fill
     */
    FILL,
    /**
     * Canceled
     */
    CANCELED
  }
}
