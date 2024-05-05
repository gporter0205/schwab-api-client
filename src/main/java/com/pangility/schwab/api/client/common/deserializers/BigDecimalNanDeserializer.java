package com.pangility.schwab.api.client.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Schwab sometimes returns invalid numeric data in it's JSON responses. When it gets very close to
 * 0, the JSON returns '{theta: "NAN"}'.  This class deserializes all BigDecimal model types as well as ignoring
 * (by returning null) "null" or "nan" values when passed as a numeric value.
 */
public class BigDecimalNanDeserializer extends JsonDeserializer<BigDecimal> {
  @Override
  public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext context)
      throws IOException {
    BigDecimal ret = null;
    final String valueAsString = jsonParser.getValueAsString();
    if(valueAsString != null && !valueAsString.isEmpty()) {
      if ((!valueAsString.equalsIgnoreCase("null")) && (!valueAsString.equalsIgnoreCase("nan"))) {
        try {
          ret = new BigDecimal(valueAsString);
        } catch (NumberFormatException ignored) {}
      }
    }
    return ret;
  }
}