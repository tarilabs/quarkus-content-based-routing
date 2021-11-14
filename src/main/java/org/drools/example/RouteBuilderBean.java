package org.drools.example;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7;
import org.drools.utils.camel3.KogitoProcessorFactory;
import org.drools.utils.camel3.WhereToAggregationStrategy; 

@ApplicationScoped
public class RouteBuilderBean extends RouteBuilder {

    @Inject
    KogitoProcessorFactory kogitoProcessor;

    Processor kogitoDMNEvaluate;
    
    @PostConstruct
    public void init() {
        kogitoDMNEvaluate = kogitoProcessor.decisionProcessor("ns1", "routing table");
    }

    AggregationStrategy aggregationStrategy = new WhereToAggregationStrategy("CATCH_ALL");

    @Override
    public void configure() throws Exception {
        from("direct:hl7")
            .enrich("direct:label", aggregationStrategy)
            .to("log:org.drools.demo?level=DEBUG&showAll=true&multiline=true")
            .routingSlip(header("whereTo"))
            .transform(HL7.ack())
            ;

        from("direct:label")
            .unmarshal().hl7()
            .to("atlasmap:atlasmap-mapping.adm").unmarshal().json()
            .process(kogitoDMNEvaluate) // <== Rules as DMN decisions to decide which Kakfa topic queue to be sent to.
            .setHeader("topicsHeader", simple("${body[topic names]}"))
        ;
    }
}
