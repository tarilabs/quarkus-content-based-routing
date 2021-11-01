package org.drools.example;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7;
// import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.ClaimCheckOperation;
import org.drools.utils.camel3.DMNResultDecisionsToHeadersProcessor;
import org.drools.utils.camel3.KogitoProcessorFactory;

@ApplicationScoped
public class RouteBuilderBean extends RouteBuilder {

    @Inject
    KogitoProcessorFactory kogitoProcessor;

    Processor kogitoDMNEvaluate;
    
    @PostConstruct
    public void init() {
        kogitoDMNEvaluate = kogitoProcessor.decisionProcessor("ns1", "routing table");
    }

    @Override
    public void configure() throws Exception {
        from("direct:hl7")
            .unmarshal().hl7()
            .claimCheck(ClaimCheckOperation.Push)
                .to("atlasmap:atlasmap-mapping.adm").unmarshal().json()
                .process(kogitoDMNEvaluate) // <== Rules as DMN decisions to decide which Kakfa topic queue to be sent to.
                .setHeader("topicsHeader", simple("${body[topic names]}"))
            .claimCheck(ClaimCheckOperation.Pop)
            .to("log:org.drools.demo?level=DEBUG&showAll=true&multiline=true")
            .to("direct:kafka")
            .transform(HL7.ack())
            ;
        
        from("direct:kafka")
            .marshal().hl7()
            .to("log:org.drools.demo?level=INFO&showAll=true&multiline=true")
            ;
        //     .transform().simple("${body.messageData}")
        //     .choice()
        //     .when(simple("${header.topicsHeader.size} > 0"))
        //         .loop(simple("${header.topicsHeader.size}"))
        //             .setHeader(KafkaConstants.OVERRIDE_TOPIC, 
        //                 simple("${header.topicsHeader[${exchangeProperty.CamelLoopIndex}]}"))
        //             .to("kafka:default")
        //         .endChoice()
        //     .otherwise()
        //         .to("kafka:CATCH_ALL")
        //     ;
    }
}
