package com.schwabapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.schwabapi.model.quote.QuoteResponse;

import java.io.IOException;

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
