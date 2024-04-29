package com.schwabapi.marketdata.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.schwabapi.marketdata.model.quote.QuoteResponse;

import java.util.HashMap;

@JsonDeserialize(using = QuoteResponseDeserializer.class)
public class QuoteResponseHashMap {
    public HashMap<String, QuoteResponse> quoteMap = new HashMap<>();
}
