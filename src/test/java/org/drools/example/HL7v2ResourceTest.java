package org.drools.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.apache.kafka.clients.consumer.ConsumerRecords;
// import org.apache.kafka.clients.consumer.KafkaConsumer;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.assertj.core.api.Assertions;
// import org.awaitility.Awaitility;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
//@QuarkusTestResource(KafkaTCResource.class) no need to setup TestContainers since https://quarkus.io/guides/kafka-dev-services
public class HL7v2ResourceTest {

    // @ConfigProperty(name = "camel.component.kafka.brokers") 
    // String kafkaBootstrap;

    @Test
    public void testHelloEndpoint() {
        final String hl7msg1 = hl7msg("MMS", "ADT", "A03");
        given().body(hl7msg1)
                .contentType(ContentType.TEXT)
                .when()
                .post("/hl7v2/new")
                .then()
                .statusCode(200)
                .body(containsString("ACK"));
        final String hl7msg2 = hl7msg("MMS", "ADT", "A01");
        given().body(hl7msg2)
                .contentType(ContentType.TEXT)
                .when()
                .post("/hl7v2/new")
                .then()
                .statusCode(200)
                .body(containsString("ACK"));
        
        // Map<String, Object> consumerConfig = new HashMap<>();
        // consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap);
        // consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID());
        // consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig,
        //         new StringDeserializer(), new StringDeserializer())) {
        //     Awaitility.await().atMost(10, TimeUnit.SECONDS)
        //         .pollInterval(1, TimeUnit.SECONDS)
        //         .untilAsserted(() -> Assertions.assertThat(consumer.listTopics().keySet()).contains("MMSAllADT", "MMSDischarges"));

        //     final ArrayList<String> events = new ArrayList<>();

        //     consumer.subscribe(Collections.singletonList("MMSAllADT"));
        //     Awaitility.await().atMost(5, TimeUnit.SECONDS)
        //         .untilAsserted(() -> {
        //         final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        //         for (final ConsumerRecord<String, String> record : records) {
        //             events.add(record.value());
        //         }
        //         Assertions.assertThat(events).hasSize(2).containsExactly(hl7msg1, hl7msg2);
        //     });

        //     consumer.unsubscribe();
        //     events.clear();

        //     consumer.subscribe(Collections.singletonList("MMSDischarges"));
        //     Awaitility.await().atMost(5, TimeUnit.SECONDS)
        //         .untilAsserted(() -> {
        //         final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        //         for (final ConsumerRecord<String, String> record : records) {
        //             events.add(record.value());
        //         }
        //         Assertions.assertThat(events).hasSize(1).containsExactly(hl7msg1);
        //     });
        // }
    }

    private String hl7msg(String sendingApp, String p1, String p2) {
        return "MSH|^~\\&|" + sendingApp + "|DH|LABADT|DH|201301011226||" + p1 + "^" + p2
                + "|HL7MSG00001|P|2.3|\nEVN|A01|201301011223||\nPID|||MRN12345^5^M11||APPLESEED^JOHN^A^III||19710101|M||C|1 DATICA STREET^^MADISON^WI^53005-1020|GL|(414)379-1212|(414)271-3434||S||MRN12345001^2^M10|123456789|987654^NC|\nNK1|1|APPLESEED^BARBARA^J|WIFE||||||NK^NEXT OF KIN\nPV1|1|I|2000^2012^01||||004777^GOOD^SIDNEY^J.|||SUR||||ADM|A0|";
    }

}