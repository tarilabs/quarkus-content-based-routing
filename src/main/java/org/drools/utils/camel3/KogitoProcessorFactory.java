package org.drools.utils.camel3;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kie.kogito.incubation.application.AppRoot;
import org.kie.kogito.incubation.common.MapDataContext;
import org.kie.kogito.incubation.decisions.DecisionIds;
import org.kie.kogito.incubation.decisions.services.DecisionService;

@ApplicationScoped
public class KogitoProcessorFactory {

    @Inject
    AppRoot appRoot;
    @Inject
    DecisionService decisionService;

    public Processor decisionProcessor(String namespace, String name) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                var decisionModel = appRoot.get(DecisionIds.class).get(namespace, name);
                var inBody = exchange.getIn().getBody(Map.class);
                var evaluateAll = decisionService.evaluate(decisionModel, MapDataContext.from(inBody));
                exchange.getIn().setBody(evaluateAll.as(MapDataContext.class).toMap());
            }
        };
    }
}
