package main

import (
        "fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
        //"github.com/linkedin/goavro"
)

const (
       	broker             = "br1kafka.dev.alldataint.com:9092"
        topic              = "testavrogolang"
        schemaRegistryURL  = "http://br1kafka.dev.alldataint.com:8081"
)

type Payment struct {
        ID          string  `avro:"id"`
        Amount      float64 `avro:"amount"`
        Description string  `avro:"description"`
}

func main() {
	config := kafka.ConfigMap{
                "bootstrap.servers":    broker,
                //"schema.registry.url":  schemaRegistryURL,
                // Add other Kafka producer config options if needed
        }

	p, err := kafka.NewProducer(&config)
        if err != nil {
                fmt.Printf("Error creating Kafka producer: %v\n", err)
                return
        }
	defer p.Close()

        // Create Payment instance
        payment := Payment{
                ID:          "1",
                Amount:      500010.00,
                Description: "Payment for transportation",
        }
	topicVar:= topic

         // Send Avro data to Kafka topic
    err = p.Produce(&kafka.Message{
        TopicPartition: kafka.TopicPartition{Topic: &topicVar, Partition: kafka.PartitionAny},
        Value:          payment.ToMap(),
    }, nil)
	if err != nil {
        fmt.Println("Error producing to Kafka:", err)
        return
    }
    p.Flush(15 * 1000) // 15 seconds timeout for flushing messages
    fmt.Println("Avro data sent to Kafka topic", topic)
}

// ToMap converts Payment struct to map for Avro encoding
func (p Payment) ToMap() []byte {
    return []byte(fmt.Sprintf(`{"id": "%s", "amount": %f, "description": "%s"}`, p.ID, p.Amount, p.Description))
}
