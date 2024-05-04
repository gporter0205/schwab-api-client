package com.pangility.schwab.api.client.marketdata.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;

import java.util.HashMap;

/**
 * Wrapper class for capturing the response json formatted as a map
 */
@JsonDeserialize(using = MarketsResponseDeserializer.class)
public class MarketsHashMap {
    /**
     * map data
     */
    public HashMap<String, HashMap<String, Hours>> map = new HashMap<>();
}