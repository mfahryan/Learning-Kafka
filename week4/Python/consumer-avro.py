from confluent_kafka import Consumer, KafkaError
from confluent_kafka.avro import AvroConsumer
import json

# AvroConsumer configuration
avro_consumer_config = {
    'bootstrap.servers': 'br1kafka.dev.alldataint.com:9092',
    'group.id': 'group',
    'schema.registry.url': 'http://br1kafka.dev.alldataint.com:8081',
    'auto.offset.reset': 'earliest'
}

# Create an AvroConsumer instance
avro_consumer = AvroConsumer(avro_consumer_config)

# Subscribe to the 'payments' topic
avro_consumer.subscribe(['payments'])

try:
    while True:
        # Poll for messages
        msg = avro_consumer.poll(1.0)

        if msg is None:
            continue
        if msg.error():
            if msg.error().code() == KafkaError._PARTITION_EOF:
                # End of partition event - not an error
                continue
            else:
                print(msg.error())
                break

        # Decode and print the Avro message
        decoded_msg = json.dumps(msg.value())
        print(f"Received message: {decoded_msg}")

except KeyboardInterrupt:
    pass
finally:
    # Close down consumer to commit final offsets.
    avro_consumer.close()
