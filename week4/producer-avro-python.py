from confluent_kafka import avro
from confluent_kafka.avro import AvroProducer
import json

# Define Avro schema for Payment
payment_schema = {
    "type": "record",
    "name": "Payment",
    "fields": [
        {"name": "id", "type": "string"},
        {"name": "amount", "type": "double"},
        {"name": "description", "type": "string"}
    ]
}

# Convert Avro schema to JSON format
avro_schema_json = json.dumps(payment_schema)

# AvroProducer configuration
avro_producer_config = {
    'bootstrap.servers': 'br1kafka.dev.alldataint.com:9092',
    'schema.registry.url': 'http://br1kafka.dev.alldataint.com:8081',
}

# Create an AvroProducer instance
avro_producer = AvroProducer(avro_producer_config, default_value_schema=avro.loads(avro_schema_json))

# Produce a sample payment message
payment_message1 = {"id": "12345667", "amount": 10000.00, "description": "Payment for Insurance"}
payment_message2 = {"id": "12345667", "amount": 440000.00, "description": "Payment for school"}

# Produce the message to Kafka
avro_producer.produce(topic='payments', value=payment_message1)
avro_producer.produce(topic='payments', value=payment_message2)

# Flush the producer to ensure delivery
avro_producer.flush()

print("Message produced successfully.")
