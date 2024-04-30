package com.pangility.schwab.api.client.marketdata.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;

import java.util.HashMap;

@JsonDeserialize(using = MarketsResponseDeserializer.class)
public class MarketsHashMap {
    public HashMap<String, HashMap<String, Hours>> map = new HashMap<>();
}