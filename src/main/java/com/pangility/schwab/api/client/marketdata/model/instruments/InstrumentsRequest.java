package com.pangility.schwab.api.client.marketdata.model.instruments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * Object used to pass the request parameters to the Schwab API <em>instruments</em> endpoint
 * either using symbol, name, description, cusip, etc.
 * The following instrument types are queryable: {@link Instrument.AssetType}.
 *</p>
 *
 * <p>
 * The following {@link com.pangility.schwab.api.client.marketdata.model.instruments.InstrumentsRequest.Projection} can be made:
 * </p>
 *
 * <ul>
 *   <li><em><strong>SYMBOL_SEARCH</strong></em>: retrieve an instrument using the exact symbol name or CUSIP</li>
 *   <li><em><strong>SYMBOL_REGEX</strong></em>: Retrieve instrument data for all symbols matching regex. For example <em>XYZ.*</em> will return all symbols beginning with <em>XYZ</em></li>
 *   <li><em><strong>DESCRIPTION_SEARCH</strong></em>: Retrieve instrument data for instruments whose description contains the word supplied. Example: <em>Bank</em> will return all instruments with <em>Bank</em> in the description.</li>
 *   <li><em><strong>DESCRIPTION_REGEX</strong></em>: Search description with full regex support.</li>
 *   <li><em><strong>SEARCH</strong></em>:</li>
 *   <li><em><strong>FUNDAMENTAL</strong></em>:</li>
 *   <li>For example <em>XYZ.[A-C]</em> returns all instruments whose descriptions contain a word beginning with <em>XYZ</em> followed by a character <em>A</em> through <em>C</em>.</li>
 *   <li>See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information.</li>
 * </ul>
 *
 * @see Instrument.AssetType
 */
@Getter
@ToString
@NoArgsConstructor
public class InstrumentsRequest {
    private String symbol;
    private Projection projection;

    /**
     * Nested class for building request
     */
    @NoArgsConstructor
    public static final class Builder {

        private String symbol;
        private Projection projection;

        /**
         * Create a builder for the request
         * @return {@link InstrumentsRequest.Builder}
         */
        public static Builder instrumentsRequest() {
            return new InstrumentsRequest.Builder();
        }

        /**
         * Add the symbol to the request
         * @param symbol String
         * @return {@link InstrumentsRequest.Builder}
         */
        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        /**
         * Add the projection to the request
         * @param projection {@link Projection}
         * @return {@link InstrumentsRequest.Builder}
         */
        public Builder withProjection(Projection projection) {
            this.projection = projection;
            return this;
        }

        /**
         * Build the request with the passed values
         * @return {@link InstrumentsRequest}
         */
        public InstrumentsRequest build() {
            InstrumentsRequest instrumentsRequest = new InstrumentsRequest();
            instrumentsRequest.symbol = this.symbol;
            instrumentsRequest.projection = this.projection;
            return instrumentsRequest;
        }
    }

    /**
     * Values allowed for the projection parameter of the Instruments request.
     */
    public enum Projection {
        /**
         * symbol-search
         */
        SYMBOL_SEARCH("symbol-search"),
        /**
         * symbol-regex
         */
        SYMBOL_REGEX("symbol-regex"),
        /**
         * desc-search
         */
        DESC_SEARCH("desc-search"),
        /**
         * desc-regex
         */
        DESC_REGEX("desc-regex"),
        /**
         * search
         */
        SEARCH("search"),
        /**
         * fundamental
         */
        FUNDAMENTAL("fundamental");

        private final String value;

        Projection(String value) {
            this.value = value;
        }

        /**
         * Get the value of the projection to be passed to the API
         * @return String
         */
        public String value() {
            return this.value;
        }
    }
}