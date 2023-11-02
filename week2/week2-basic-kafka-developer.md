# Dokumentasi Belajar Basic-Kafka : Developer
Dokumentasi ini berisi langkah-langkah yang saya pelajari saat belajar membangun kafka produser hingga Membuat connector menggunakan kafka connect untuk melakukan koneksi ke RDBMS.

## 1. Basic-Kafka : Developer
### Langkah 1 : Start zookeeper dan kafka server
### Langkah 2 : Membangun Producer dengan Java
2.1 Mulai mengikuti langkah yang dijelaskan pada laman berikut : [kafka documentation API Producer](https://kafka.apache.org/documentation/#producerapi)

Pada halaman ini jika ingin menggunakan producer kita harus memasukkan maven dependency seperti berikut.

```
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.6.0</version>
</dependency>

```
Untuk kegunaan maven dependency adalah dependency merupakan file arsip seperti JAR, ZIP, dll., yang perlu dikompilasi, dibuat, diuji, dan dijalankan oleh proyek. Ketergantungan proyek ini ditentukan dalam file pom.xml, Saat menjalankan build atau tujuan maven, dependency proyek ini diselesaikan dan kemudian diambil dari local repository

2.2 Membuat Class Kafka Producer

Class producer sendiri sudah disediakan oleh apache kafka itu sendiri yang terdapat pada halaman web berikut: [Class Kafka Producer](https://kafka.apache.org/36/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html)

```
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class contohProduser {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String>  producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 100; i ++){
            ProducerRecord<String, String> record = new ProducerRecord<>("test1", "Message : " + i);
            producer.send(record);
        }
        producer.close();

    }
}

```

```
properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

```
untuk penjelasan KEY_SERIALIZER_CLASS di bagian producer menggunakan serializer misal jika kita mengirim String akan masuk ke topic berupa byte
lalu disaat kita ingin menggunakan consumer kita menggunakan KEY_DESERIALIZER_CLASS untuk mengembalikan byte menjadi type data yang di produce ke topic

disini saya ingin mengirim ke topic yaitu Message 1 sampai Message 100 yang terdapat pada coding

```
for (int i = 0; i < 100; i ++){
            ProducerRecord<String, String> record = new ProducerRecord<>("test1", "Message : " + i);
            producer.send(record);
System.out.println("Pesan Berhasil Terkirim dengan Message : "+i);
}
producer.closes();
```
![Screenshot from 2023-11-02 09-39-42](https://github.com/mfahryan/Learning-Kafka/assets/112185850/072e1f2e-ce5c-48a7-a184-d6e1691a6902)

### Langkah 3 : Membangun Consumer dengan Java

3.1 Mulai mengikuti langkah yang dijelaskan pada laman berikut
Pada halaman ini sama seperti sebelumnya kita harus memasukkan maven dependency seperti berikut. [Kafka Documentation API Consumer](https://kafka.apache.org/documentation/#consumerapi)
```
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.6.0</version>
</dependency>

```
3.2 Membuat Class Consumer
Class consumer sendiri sudah disediakan oleh apache kafka itu sendiri yang terdapat pada halaman web berikut:[Class Kafka Consumer](https://kafka.apache.org/36/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html)
```
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class contohConsumer {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "week");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.setProperty("org.apache.kafka.clients.consumer.ConsumerConfig", "WARN");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("week1"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("Receive this data : "+record.value());
            }

                }
    }
}
```
sama seperti penjelasan sebelumnya yaitu di class consumer menggunakan KEY_DESERIALIZER_CLASS dikarenakan mengembalikan type data yang ada di topic(byte) menjadi type data yang dikirim producer ke topic
disini saya ingin mengconsume data dari topic yang sudah saya kirim melalui producer dengan topic "week1" dan hasilnya seperti berikut : 
![Screenshot from 2023-11-02 10-35-57](https://github.com/mfahryan/Learning-Kafka/assets/112185850/19cf2558-1add-45dc-869a-19cf5d90a609)

### Langkah 4 : Bangun Kafka Admin Client Java untuk melakukan Create dan Delete topic dengan Java

4.1 Membuat Kafka Admin Client denga Java
Kafka Admin Client berguna mendukung pengelolaan dan pemeriksaan topik, broker, konfigurasi, dan ACL. Instance yang dikembalikan dari metode pembuatan antarmuka ini dijamin aman untuk thread.
dengan code seperti berikut :

```
import org.apache.kafka.clients.admin.*;

import java.time.Duration;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class adminClient  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        AdminClient admin = AdminClient.create(props);
```

4.2 Membuat Create Topic dengan Admin Client
kita bisa membuat topic langsung dengan menggunakan code seperti berikut :

```
admin.createTopics(Collections.singleton(new NewTopic("week2", 3, (short) 1)));

```
- week2 merupakan nama topic yang ingin kita buat
- 3 merupakan NUM_PARTITIONS atau number partitions yaitu jumlah partisi log per topik
- 1 merupakan REP_FACTOR atau replication factor yaitu mengacu pada banyak salinan data yang disimpan di beberapa broker Kafka. Menyetel Faktor Replikasi Kafka memungkinkan Kafka menyediakan ketersediaan data yang tinggi dan mencegah kehilangan data jika broker down atau tidak dapat menangani permintaan.

4.3 Membuat List Topic dengan Admin Client
membuat list topic dengan menggunakan code seperti berikut : 

```
ListTopicsResult topics = admin.listTopics();
        topics.names().get().forEach(System.out::println);

```
bertujuan agar kita dapat mengetahui topic apa saja yang sudah kita buat sebelumnya seperti contoh :

![listtopic](https://github.com/mfahryan/Learning-Kafka/assets/112185850/ab6dc893-6a2f-4dd7-8abb-4e443eee7b11)

4.4 Membuat Delete Topic
membuat delete topic dengan menggunakan code seperti berikut :

```
admin.deleteTopics(Collections.singleton("topik")).all().get();

```
disini saya ingin menghapus topic dengan nama "topik" dan hasilnya seperti berikut : 

![deletetopics](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6d439045-73fa-4d36-801d-05b9f985d024)

### Langkah 5 Membangun Demo Kafka streams dengan kstream dan ktable dengan Java

5.1 membangun demo kafka streams











