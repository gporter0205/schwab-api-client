package com.schwabapi.model.quote.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.schwabapi.model.quote.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceIndex extends Reference {
}