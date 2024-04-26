package com.schwabapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * TDA seems to return invalid JSON sometimes with an OptionChain.theta. When it gets very close to
 * 0, the JSON returns '{theta: "NAN"}'.
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