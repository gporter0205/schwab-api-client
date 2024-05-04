package com.pangility.schwab.api.client.marketdata.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;

import java.util.HashMap;

/**
 * Wrapper class for capturing the response json formatted as a map
 */
@JsonDeserialize(using = QuoteResponseDeserializer.class)
public class QuoteResponseHashMap {
    /**
     * map data
     */
    public HashMap<String, QuoteResponse> quoteMap = new HashMap<>();
}
