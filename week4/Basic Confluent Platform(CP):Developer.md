# Bangun Kafka Producer, Kafka Consumer, Kafka Admin Client dengan bahasa selain Java (python,  go)

## Membuat Producer dengan Python

Karena saya sudah ada python 3 jadi saya tinggal ngambil atau install kafka-pyhton dengan `pip3 install kafka-python`
lalu untuk menjalankan producer dengan cara seperti 

```
from kafka import KafkaProducer

# Konfigurasi producer
producer = KafkaProducer(
    bootstrap_servers='br1kafka.###:9092',
    value_serializer=lambda v: str(v).encode('utf-8')
)

# Kirim pesan ke topik
topic_name = 'nama_topik'
message = 'Hello, Kafka!'
producer.send(topic_name, value=message)

# Tutup produser
producer.close()

```
## Membuat Consumer dengan bahasa Python 

lalu untuk consumer nya sendiri dengan code seperti dibawah : 

```
from kafka import KafkaConsumer

# Konfigurasi consumer
consumer = KafkaConsumer(
    'nama_topik',
    bootstrap_servers='br1kafka.###:9092',
    group_id='nama_grup',
    auto_offset_reset='earliest',  # Ganti dengan 'latest' jika ingin membaca pesan yang masuk setelah aplikasi dimulai
    value_deserializer=lambda x: x.decode('utf-8')
)

# Membaca pesan dari topik
for message in consumer:
    print(f"Received message: {message.value}")

# Jangan lupa menutup consumer
consumer.close()

```

## Membuat Admin Client dengan bahasa Python

Untuk kafka client admin nya sendiri jika di python seperti ini :
disini saya masih menggunakan satu-satu admin client mungkin selanjutnya saya mungkin bisa membuat buat,list,dan delete topic berada dalam satu code

1. untuk membuat topic baru

```
# ini untuk membuat topic
from kafka import KafkaAdminClient
from kafka.admin import NewTopic

# Konfigurasi admin client
admin_client = KafkaAdminClient(
    bootstrap_servers='br1kafka.###:9092',
)

# Definisikan konfigurasi topik baru
new_topic = NewTopic(
    name='nama_topik',
    num_partitions=1,
    replication_factor=1,
)

# Buat topik baru menggunakan admin client
admin_client.create_topics([new_topic])

# Jangan lupa menutup admin client
admin_client.close()

```
2. Untuk melihat list topic baru

```
from kafka import KafkaAdminClient

# Konfigurasi admin client
admin_client = KafkaAdminClient(
    bootstrap_servers='br1kafka.###:9092',
)

# Dapatkan metadata cluster
cluster_metadata = admin_client.list_topics()

# Pastikan bahwa cluster_metadata adalah objek yang benar
if cluster_metadata:
    # Ambil daftar topik dari metadata cluster
    topics = cluster_metadata
    # Cetak daftar topik
    print("Daftar Topik:")
    for topic in topics:
        print(topic)
else:
    print("Tidak ada metadata cluster yang diperoleh.")

# Jangan lupa menutup admin client
admin_client.close()

```

3. Untuk menghapus topic

```
from kafka import KafkaAdminClient

# Konfigurasi admin client
admin_client = KafkaAdminClient(
    bootstrap_servers='br1kafka.###:9092',
)

# Tentukan nama topik yang akan dihapus
topic_name = 'nama_topik'

# Hapus topik menggunakan admin client
admin_client.delete_topics(topics=[topic_name])

# Jangan lupa menutup admin client
admin_client.close()


```

## membuat Producer Dengan Bahasa GO

Jadi setelah saya membuat producer,consumer,admin client dengan bahasa python saya mencoba membuat dengan bahasa yang lain disini saya memilih untuk menggunakan bahasa GO, pertama untuk membuat producer dapat dilihat di code berikut :

```

package main

import (
        "fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func main() {
	// Konfigurasi Producer
        config := &kafka.ConfigMap{
                "bootstrap.servers": "br1kafka.##:9092",
        }

	producer, err := kafka.NewProducer(config)
        if err != nil {
                fmt.Printf("Failed to create producer: %s\n", err)
                return
        }
	defer producer.Close()

        // Topik yang akan di-produce
        topic := "test-golang"

        // Loop untuk mengirim 100 pesan
        for i := 1; i <= 100; i++ {
                message := &kafka.Message{
                        TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
                        Value:          []byte(fmt.Sprintf("Message %d", i)),
                }

                // Produce message ke Kafka
                err := producer.Produce(message, nil)
                if err != nil {
                        fmt.Printf("Failed to produce message %d: %s\n", i, err)
                }
        }

	// Flush producer
        producer.Flush(10000)
        fmt.Println("Producing 100 messages completed.")
}


```


## Membuat Consumer Dengan Bahasa GO

Setelah membuat producer selanjutnya saya membuat consumer dengan bahasa GO dengan code seperti berikut

```
package main

import (
    "fmt"
    "github.com/confluentinc/confluent-kafka-go/kafka"
    "os"
)

func main() {
    // Konfigurasi Consumer
    config := &kafka.ConfigMap{
        "bootstrap.servers": "br1kafka.##:9092",
        "group.id":          "my-consumer-group",
        "auto.offset.reset": "earliest", // Pilih "earliest" atau "latest" sesuai kebutuhan.
    }

    consumer, err := kafka.NewConsumer(config)
    if err != nil {
        fmt.Printf("Failed to create consumer: %s\n", err)
        return
    }
    defer consumer.Close()

    // Subscribe ke topik "test"
    consumer.SubscribeTopics([]string{"test_kafka_python"}, nil)

    // Loop terus menerima pesan
    for {
	ev := consumer.Poll(100)
        if ev == nil {
            continue
        }

	switch e := ev.(type) {
        case *kafka.Message:
            fmt.Printf("Received message: %s\n", string(e.Value))
        case kafka.Error:
            fmt.Fprintf(os.Stderr, "Error: %v\n", e)
            break
        }
    }
}


```

## Membuat AdminClient dengan Bahasa GO

```
package main

import (
        "context"
        "fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
        "os"
)

func main() {
	// Configure the admin client
        config := &kafka.ConfigMap{
                "bootstrap.servers": "br1kafka.dev.alldataint.com:9092",
        }

	adminClient, err := kafka.NewAdminClient(config)
        if err != nil {
                fmt.Printf("Failed to create admin client: %s\n", err)
                os.Exit(1)
        }
	defer adminClient.Close()

        // List topics
        fmt.Println("List of topics:")
        topics, err := adminClient.GetMetadata(nil, true, 5000)
        if err != nil {
                fmt.Printf("Failed to get metadata: %s\n", err)
                os.Exit(1)
        }
	for _, topic := range topics.Topics {
                fmt.Println(topic.Topic)
        }

	/* // Specify the topic to delete
        topicToDelete := "topicdelete"

        // Delete the topic
         results, err := adminClient.DeleteTopics(context.Background(), []string{topicToDelete}, nil)
        if err != nil {
                fmt.Printf("Failed to delete topic %s: %s\n", topicToDelete, err)
                os.Exit(1)
        }

        // Check the deletion results
        fmt.Println("\nDeletion results:")
        for _, result := range results {
                if result.Error.Code() != kafka.ErrNoError {
                        fmt.Printf("Failed to delete topic %s: %s\n", result.Topic, result.Error)}
                else {
                        fmt.Printf("Topic %s deleted successfully\n", result.Topic)
                }
        }*/
}



```


### Membuat Avro Producer dengan Java, Python, Golang


