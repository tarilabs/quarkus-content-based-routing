#!/bin/sh
docker run -d --rm -p 9000:9000 \
    -e KAFKA_BROKERCONNECT=test-c--j-k-uefmjp-e-hjmg.bf2.kafka.rhcloud.com:443 \
    -e KAFKA_PROPERTIES=$(cat kafdrop.properties.env | base64) \
    -e JVM_OPTS="-Xms32M -Xmx64M" \
    -e SERVER_SERVLET_CONTEXTPATH="/" \
    obsidiandynamics/kafdrop:latest
