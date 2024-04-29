package com.schwabapi.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    final String valueAsString = jp.getValueAsString();
    LocalDate ld;
    try {
      ld = LocalDate.parse(valueAsString, DateTimeFormatter.ISO_DATE);
    } catch (DateTimeParseException dtpe) {
      // account for unconventional data returned for dates in instruments endpoint/Fundamental model object (ie "2024-04-29 00:00:00.0")
      try {
        LocalDateTime ldt = LocalDateTime.parse(valueAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        ld = ldt.toLocalDate();
      } catch (DateTimeParseException retry) {
        throw dtpe;
      }
    }
    return ld;
  }
}