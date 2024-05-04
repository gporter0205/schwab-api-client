package com.pangility.schwab.api.client.marketdata.model.instruments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Object used to receive the response from the Schwab API <em>instruments</em> endpoint
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InstrumentsResponse {
    private List<Instrument> instruments;
}
