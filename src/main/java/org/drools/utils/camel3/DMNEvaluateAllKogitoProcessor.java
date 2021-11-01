package org.drools.utils.camel3;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNResult;
import org.kie.kogito.decision.DecisionModel;

public class DMNEvaluateAllKogitoProcessor implements Processor {

    private final DecisionModel kogitoDMNModel;

    public DMNEvaluateAllKogitoProcessor(DecisionModel kogitoDMNModel) {
        this.kogitoDMNModel = kogitoDMNModel;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        DMNContext newContext = kogitoDMNModel.newContext(exchange.getIn().getBody(Map.class));
        DMNResult result = kogitoDMNModel.evaluateAll(newContext);
        exchange.getIn().setBody(result);
    }
}