package org.drools.utils.camel3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.sun.xml.xsom.impl.scd.Iterators.Map;

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
                var exBody = exchange.getIn().getBody();
                System.out.println(exBody);
                var evaluateAll = decisionService.evaluate(decisionModel, MapDataContext.from(exBody)).as(MapDataContext.class).toMap();
                exchange.getIn().setBody(evaluateAll);
            }
        };
    }
}
