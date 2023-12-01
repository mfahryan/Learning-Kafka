package org.example;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class AvroProducerExample {
    public static void main(String[] args) {


        String bootstrapServers = "br1kafka.dev.alldataint.com:9092";

        // Set the Schema Registry URL
        String schemaRegistryUrl = "http://br1kafka.dev.alldataint.com:8081";

        // Set the topic name
        String topic = "transactions";

        // Create producer properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put("schema.registry.url", schemaRegistryUrl);

        try (Producer<String, GenericRecord> producer = new KafkaProducer<>(properties)) {
            // Create a Payment object
            Payment payment = new Payment("2", 10000.00);

            // Create a GenericRecord based on the Payment object
            GenericRecord genericRecord = new GenericData.Record(payment.getSchema());
            genericRecord.put("id", payment.getId());
            genericRecord.put("amount", payment.getAmount());

            // Produce the Avro record to Kafka
            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic, "key", genericRecord);
            producer.send(record);

            System.out.println("Avro record sent: " + payment.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




