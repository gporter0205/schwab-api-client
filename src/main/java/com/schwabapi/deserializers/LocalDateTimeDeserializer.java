package com.schwabapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    final String valueAsString = jp.getValueAsString();
    return LocalDateTime.parse(valueAsString, DateTimeFormatter.ISO_DATE_TIME);
  }
}