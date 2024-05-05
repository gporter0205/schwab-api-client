package com.pangility.schwab.api.client.marketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pangility.schwab.api.client.marketdata.model.markets.Hours;

import java.io.IOException;
import java.util.HashMap;

/**
 * Deserializer for capturing the markets response json formatted as a map
 * Couldn't find a clean way for Jackson to parse it.
 */
public class MarketsResponseDeserializer extends JsonDeserializer<MarketsHashMap> {

    @Override
    public MarketsHashMap deserialize(JsonParser jsonParser,
                                            DeserializationContext deserializationContext)
            throws IOException {
        MarketsHashMap marketsHashMap = new MarketsHashMap();
        JsonToken token = jsonParser.nextToken();
        while(token == JsonToken.FIELD_NAME) {
            String productName = jsonParser.getCurrentName();
            if(!productName.equalsIgnoreCase("errors")) {
                token = jsonParser.nextToken();
                if(token == JsonToken.START_OBJECT) {
                    token = jsonParser.nextToken();
                    HashMap<String, Hours> productMap = new HashMap<>();
                    while (token == JsonToken.FIELD_NAME) {
                        String product = jsonParser.getCurrentName();
                        if (!product.equalsIgnoreCase("errors")) {
                            token = jsonParser.nextToken();
                            if (token == JsonToken.START_OBJECT) {
                                Hours hours = jsonParser.readValueAs(Hours.class);
                                productMap.put(product, hours);
                            }
                            token = jsonParser.nextToken();
                        }
                    }
                    marketsHashMap.map.put(productName, productMap);
                }
            }
            token = jsonParser.nextToken();
        }
        return marketsHashMap;
    }
}
