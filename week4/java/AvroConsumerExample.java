package org.example;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AvroConsumerExample {
    public static void main(String[] args) {
        // Set the Kafka broker address
        String bootstrapServers = "br1kafka.dev.alldataint.com:9092";

        // Set the Schema Registry URL
        String schemaRegistryUrl = "http://br1kafka.dev.alldataint.com:8081";

        // Set the topic name
        String topic = "transactions";

        // Create consumer properties
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_avro_python");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (Consumer<String, GenericRecord> consumer = new KafkaConsumer<>(properties)) {
            // Subscribe to the Kafka topic
            consumer.subscribe(Collections.singletonList(topic));

            // Poll for records
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));

                records.forEach(record -> {
                    // Retrieve the Avro record
                    GenericRecord avroRecord = record.value();

                    // Create a Payment object from the Avro record
                    Payment payment = new Payment();
                    payment.setId(avroRecord.get("id").toString());
                    payment.setAmount((Double) avroRecord.get("amount"));

                    // Process the Payment object
                    System.out.println("Received Payment: " + payment.toString());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
