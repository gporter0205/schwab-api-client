package com.schwabapi.marketdata.model.instruments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InstrumentsRequest {
    private String symbol;
    private Projection projection;

    @NoArgsConstructor
    public static final class Builder {

        private String symbol;
        private Projection projection;
        public static Builder instrumentsRequest() {
            return new InstrumentsRequest.Builder();
        }

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withProjection(Projection projection) {
            this.projection = projection;
            return this;
        }

        public InstrumentsRequest build() {
            InstrumentsRequest instrumentsRequest = new InstrumentsRequest();
            instrumentsRequest.symbol = this.symbol;
            instrumentsRequest.projection = this.projection;
            return instrumentsRequest;
        }
    }

    public enum Projection {
        SYMBOL_SEARCH("symbol-search"),
        SYMBOL_REGEX("symbol-regex"),
        DESC_SEARCH("desc-search"),
        DESC_REGEX("desc-regex"),
        SEARCH("search"),
        FUNDAMENTAL("fundamental");

        private final String value;

        Projection(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}