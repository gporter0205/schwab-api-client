package com.schwabapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    final String valueAsString = jp.getValueAsString();
    return LocalDate.parse(valueAsString, DateTimeFormatter.ISO_DATE);
  }
}