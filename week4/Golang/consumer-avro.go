package main

import (
        "fmt"
	"os"
	"os/signal"
        "syscall"

        "github.com/confluentinc/confluent-kafka-go/kafka"
        "github.com/linkedin/goavro/v2"
)

const (
       	broker            = "br1kafka.dev.alldataint.com:9092"
        topic             = "testavrogolang"
        groupID           = "my-group-id"
        schemaRegistryURL = "http://br1kafka.dev.alldataint.com:8081"
)

type Payment struct {
        ID          string  `avro:"id"`
        Amount      float64 `avro:"amount"`
        Description string  `avro:"description"`
}

func main() {
	configMap := kafka.ConfigMap{
                "bootstrap.servers": broker,
                "group.id":           groupID,
                "auto.offset.reset":  "earliest",
        }

	c, err := kafka.NewConsumer(&configMap)
        if err != nil {
                fmt.Printf("Error creating Kafka consumer: %v\n", err)
                return
        }
	defer c.Close()

        // Subscribe to the topic
        err = c.SubscribeTopics([]string{topic}, nil)
        if err != nil {
                fmt.Printf("Error subscribing to topic: %v\n", err)
                return
        }

	fmt.Println("Consumer started. Waiting for messages...")
  sigchan := make(chan os.Signal, 1)
        signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

        run := true

        for run == true {
                select {
                case sig := <-sigchan:
                        fmt.Printf("Caught signal %v: terminating\n", sig)
                        run = false

                default:
                        ev := c.Poll(100)
                        if ev == nil {
                                continue
                        }

                        switch e := ev.(type) {
                        case *kafka.Message:
                                // Process the Avro data
                                payment, err := deserializeAvro(e.Value)
                                if err != nil {
                                        fmt.Printf("Error deserializing Avro data: %v\n", err)
                                        continue
                                }

                                // Now you can use the 'payment' struct as needed
                                fmt.Printf("Received Avro payment: %+v\n", payment)

                        case kafka.PartitionEOF:
                                fmt.Printf("Got to the end of partition %v\n", e)

                        case kafka.Error:
                                fmt.Fprintf(os.Stderr, "Error: %v\n", e)
                        }
                }
        }
}

// deserializeAvro deserializes Avro data to Payment struct
func deserializeAvro(avroData []byte) (Payment, error) {
        codec, err := goavro.NewCodec(`{"type":"record","name":"Payment","fields":[{"name":"id","type":"string"},{"name":"amount","type":"double"},{"name":"description","type":"string"}]}`)
        if err != nil {
                return Payment{}, err
        }

	native, _, err := codec.NativeFromTextual(avroData)
        if err != nil {return Payment{}, err
        }

	record, ok := native.(map[string]interface{})
        if !ok {
                return Payment{}, fmt.Errorf("Avro data is not in the expected format")
        }

	payment := Payment{
                ID:          record["id"].(string),
                Amount:      record["amount"].(float64),
                Description: record["description"].(string),
        }

	return payment, nil
}
