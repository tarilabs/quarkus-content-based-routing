package org.drools.utils.camel3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kie.dmn.api.core.DMNResult;

public class DMNResultDecisionsToHeadersProcessor implements Processor {

    private final Map<String, String> toHeadersMap;

    public DMNResultDecisionsToHeadersProcessor(Map<String, String> toHeadersMap) {
        Objects.requireNonNull(toHeadersMap);
        this.toHeadersMap = new HashMap<>(toHeadersMap);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        DMNResult body = exchange.getIn().getBody(DMNResult.class);
        for (Entry<String, String> kv : toHeadersMap.entrySet()) {
            exchange.getIn().setHeader(kv.getKey(), body.getDecisionResultByName(kv.getValue()).getResult());
        }
    }

    public static Builder builder(String headerKey,
                                  String decisionName) {
        return new Builder(headerKey, decisionName);
    }

    public static class Builder {

        private final Map<String, String> toHeadersMap = new HashMap<>();

        public Builder(String headerKey,
                       String decisionName) {
            Objects.requireNonNull(headerKey);
            Objects.requireNonNull(decisionName);
            toHeadersMap.put(headerKey, decisionName);
        }

        public Builder with(String headerKey, String decisionName) {
            Objects.requireNonNull(headerKey);
            Objects.requireNonNull(decisionName);
            toHeadersMap.put(headerKey, decisionName);
            return this;
        }

        public DMNResultDecisionsToHeadersProcessor build() {
            return new DMNResultDecisionsToHeadersProcessor(this.toHeadersMap);
        }
    }

}
