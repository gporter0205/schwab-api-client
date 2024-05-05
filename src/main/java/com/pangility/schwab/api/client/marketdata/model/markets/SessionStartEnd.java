
package com.pangility.schwab.api.client.marketdata.model.markets;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pangility.schwab.api.client.common.deserializers.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * SessionStartEnd
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName("sessionStartEnd")
public class SessionStartEnd {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime end;
    @JsonIgnore
    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();
}
