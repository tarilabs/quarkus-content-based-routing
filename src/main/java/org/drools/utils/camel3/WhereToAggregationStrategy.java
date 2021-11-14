package org.drools.utils.camel3;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class WhereToAggregationStrategy implements AggregationStrategy {

    private String defaultTopic;

    public WhereToAggregationStrategy(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        @SuppressWarnings("unchecked")
        List<Object> h = newExchange.getIn().getHeader("topicsHeader", List.of(defaultTopic), List.class);
        var whereTo = h.stream().map(Object::toString).map(t->"kafka:"+t).collect(Collectors.joining(","));
        oldExchange.getIn().setHeader("whereTo", whereTo);
        return oldExchange;
    }

}