package com.schwabapi.marketdata.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.marketdata.model.markets.Hours;

import java.util.HashMap;

@JsonDeserialize(using = MarketsResponseDeserializer.class)
public class MarketsHashMap {
    public HashMap<String, HashMap<String, Hours>> map = new HashMap<>();
}