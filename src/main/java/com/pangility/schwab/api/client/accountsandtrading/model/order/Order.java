package com.pangility.schwab.api.client.accountsandtrading.model.order;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.LocalDateDeserializer;
import com.pangility.schwab.api.client.common.deserializers.ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Order both sent and received when making trades. Some of the easier examples are described on the
 * TDA Developer page.
 *
 * @see <a href="https://developer.tdameritrade.com/content/place-order-samples">Place Order Samples</a>
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Order implements Serializable {
  private Session session;
  private Duration duration;
  private OrderType orderType;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate cancelTime;
  private ComplexOrderStrategyType complexOrderStrategyType;
  private BigDecimal quantity;
  private BigDecimal filledQuantity;
  private BigDecimal remainingQuantity;
  private RequestedDestination requestedDestination;
  private String destinationLinkName;
  private Date releaseTime;
  private BigDecimal stopPrice;
  private StopPriceLinkBasis stopPriceLinkBasis;
  private StopPriceLinkType stopPriceLinkType;
  private BigDecimal stopPriceOffset;
  private StopType stopType;
  private PriceLinkBasis priceLinkBasis;
  private PriceLinkType priceLinkType;
  private BigDecimal price;
  private TaxLotMethod taxLotMethod;
  private List<OrderLegCollection> orderLegCollection = new ArrayList<>();
  private BigDecimal activationPrice;
  private SpecialInstruction specialInstruction;
  private OrderStrategyType orderStrategyType;
  private Long orderId;
  private Boolean cancelable;
  private Boolean editable;
  private Status status;
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime enteredTime;
  @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
  private ZonedDateTime closeTime;
  private String tag;
  private Long accountId;
  private List<OrderActivity> orderActivityCollection = new ArrayList<>();
  private List<Object> replacingOrderCollection = new ArrayList<>();
  private List<Object> childOrderStrategies = new ArrayList<>();
  private String statusDescription;
  @JsonIgnore
  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();
}
