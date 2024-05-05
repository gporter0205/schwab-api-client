package com.pangility.schwab.api.client.marketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pangility.schwab.api.client.marketdata.model.quotes.QuoteResponse;

import java.io.IOException;

/**
 * Deserializer for capturing the quote response json formatted as a map.
 * Couldn't find a clean way for Jackson to parse it.
 */
public class QuoteResponseDeserializer extends JsonDeserializer<QuoteResponseHashMap> {

    @Override
    public QuoteResponseHashMap deserialize(JsonParser jsonParser,
                                            DeserializationContext deserializationContext)
            throws IOException {
        QuoteResponseHashMap quoteResponseHashMap = new QuoteResponseHashMap();
        JsonToken token = jsonParser.nextToken();
        while(token == JsonToken.FIELD_NAME) {
            String symbol = jsonParser.getCurrentName();
            if(!symbol.equalsIgnoreCase("errors")) {
                token = jsonParser.nextToken();
                if (token == JsonToken.START_OBJECT) {
                    QuoteResponse quote = jsonParser.readValueAs(QuoteResponse.class);
                    quoteResponseHashMap.quoteMap.put(symbol, quote);
                }
                token = jsonParser.nextToken();
                if (token == JsonToken.END_OBJECT) {
                    token = jsonParser.nextToken();
                }
            }
        }
        return quoteResponseHashMap;
    }
}
