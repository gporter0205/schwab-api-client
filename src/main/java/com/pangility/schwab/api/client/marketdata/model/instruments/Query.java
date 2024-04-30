package com.pangility.schwab.api.client.marketdata.model.instruments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * Query TDA for <em>instruments</em> , either using symbol, name, description, cusip, etc.
 * Apparently the following instrument types are queryable: {@link Instrument.AssetType}.
 *</p>
 *
 * <p>
 * The following {@link QueryType QueryTypes} can be made:
 * </p>
 *
 * <ul>
 *   <li><em><strong>SYMBOL_SEARCH</strong></em>: retrieve an instrument using the exact symbol name or CUSIP</li>
 *   <li><em><strong>SYMBOL_REGEX</strong></em>: Retrieve instrument data for all symbols matching regex. For example <em>XYZ.*</em> will return all symbols beginning with <em>XYZ</em></li>
 *   <li><em><strong>DESCRIPTION_SEARCH</strong></em>: Retrieve instrument data for instruments whose description contains the word supplied. Example: <em>Bank</em> will return all instruments with <em>Bank</em> in the description.</li>
 *   <li><em><strong>DESCRIPTION_REGEX</strong></em>: Search description with full regex support.
 *   For example <em>XYZ.[A-C]</em> returns all instruments whose descriptions contain a word beginning with <em>XYZ</em> followed by a character <em>A</em> through <em>C</em>.</li>
 * </ul>
 *
 * @see Instrument.AssetType
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Query {
  private String searchStr;
  private QueryType queryType;

  public Query(String searchStr, QueryType queryType) {
    this.searchStr = searchStr;
    this.queryType = queryType;
  }

  @Getter
  @ToString
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public enum QueryType {
    SYMBOL_SEARCH("symbol-search"),
    SYMBOL_REGEX("symbol-regex"),
    DESCRIPTION_SEARCH("desc-search"),
    DESCRIPTION_REGEX("desc-regex");

    private final String queryType;

    QueryType(String queryType) {
      this.queryType = queryType;
    }
  }
}
