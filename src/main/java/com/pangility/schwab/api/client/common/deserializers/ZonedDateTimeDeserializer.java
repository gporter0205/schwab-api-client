package com.pangility.schwab.api.client.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for deserializing Schwab zoned date/time json into {@link ZonedDateTime}
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    final String valueAsString = jp.getValueAsString();
      return ZonedDateTime.parse(valueAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
  }
}