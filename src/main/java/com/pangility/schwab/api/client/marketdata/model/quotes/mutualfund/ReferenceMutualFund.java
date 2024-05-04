package com.pangility.schwab.api.client.marketdata.model.quotes.mutualfund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pangility.schwab.api.client.marketdata.model.quotes.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ReferenceMutualFund
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceMutualFund extends Reference {
}