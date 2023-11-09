# Dokumentasi Belajar Basic-Kafka : Developer
Dokumentasi ini berisi langkah-langkah yang saya pelajari saat belajar membangun kafka produser hingga Membuat connector menggunakan kafka connect untuk melakukan koneksi ke RDBMS.

## 1. Basic-Kafka : Developer
### Langkah 1 : Start zookeeper dan kafka server
### Langkah 2 : Membangun Producer dengan Java
**2.1 Mulai mengikuti langkah yang dijelaskan pada laman berikut** : [kafka documentation API Producer](https://kafka.apache.org/documentation/#producerapi)

Pada halaman ini jika ingin menggunakan producer kita harus memasukkan maven dependency seperti berikut.

```
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.6.0</version>
</dependency>

```
Untuk kegunaan maven dependency adalah dependency merupakan file arsip seperti JAR, ZIP, dll., yang perlu dikompilasi, dibuat, diuji, dan dijalankan oleh proyek. Ketergantungan proyek ini ditentukan dalam file pom.xml, Saat menjalankan build atau tujuan maven, dependency proyek ini diselesaikan dan kemudian diambil dari local repository

**2.2 Membuat Class Kafka Producer**

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

**3.1 Mulai mengikuti langkah yang dijelaskan pada laman berikut**
Pada halaman ini sama seperti sebelumnya kita harus memasukkan maven dependency seperti berikut. [Kafka Documentation API Consumer](https://kafka.apache.org/documentation/#consumerapi)
```
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.6.0</version>
</dependency>

```
**3.2 Membuat Class Consumer**
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

**4.1 Membuat Kafka Admin Client denga Java**
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

**4.2 Membuat Create Topic dengan Admin Client**
kita bisa membuat topic langsung dengan menggunakan code seperti berikut :

```
admin.createTopics(Collections.singleton(new NewTopic("week2", 3, (short) 1)));

```
- week2 merupakan nama topic yang ingin kita buat
- 3 merupakan NUM_PARTITIONS atau number partitions yaitu jumlah partisi log per topik
- 1 merupakan REP_FACTOR atau replication factor yaitu mengacu pada banyak salinan data yang disimpan di beberapa broker Kafka. Menyetel Faktor Replikasi Kafka memungkinkan Kafka menyediakan ketersediaan data yang tinggi dan mencegah kehilangan data jika broker down atau tidak dapat menangani permintaan.

**4.3 Membuat List Topic dengan Admin Client**
membuat list topic dengan menggunakan code seperti berikut : 

```
ListTopicsResult topics = admin.listTopics();
        topics.names().get().forEach(System.out::println);

```
bertujuan agar kita dapat mengetahui topic apa saja yang sudah kita buat sebelumnya seperti contoh :

![listtopic](https://github.com/mfahryan/Learning-Kafka/assets/112185850/ab6dc893-6a2f-4dd7-8abb-4e443eee7b11)

**4.4 Membuat Delete Topic**
membuat delete topic dengan menggunakan code seperti berikut :

```
admin.deleteTopics(Collections.singleton("topik")).all().get();

```
disini saya ingin menghapus topic dengan nama "topik" dan hasilnya seperti berikut : 

![deletetopics](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6d439045-73fa-4d36-801d-05b9f985d024)

### Langkah 5 : Membangun Demo Kafka streams dengan kstream dan ktable dengan Java

Kafka adalah platform streaming terdistribusi yang menghubungkan berbagai aplikasi atau layanan mikro untuk memungkinkan pemrosesan berkelanjutan. Tujuannya adalah untuk memastikan aplikasi klien menerima informasi dari sumber secara konsisten secara waktu nyata.

Beberapa produser dapat melakukan aliran pesan ke topik yang sama atau ke beberapa topik. Hal ini memungkinkan sistem yang menggunakan Kafka untuk mengumpulkan data dari banyak sumber dan membuatnya konsisten.

Streams API dalam Apache Kafka adalah pustaka yang kuat dan ringan yang memungkinkan pemrosesan on-the-fly, memungkinkan Anda menggabungkan, membuat parameter windowing, melakukan penggabungan data dalam aliran, dan banyak lagi.

Berikut adalah cara atau demo kafka streams

**5.1 Membangun Demo Kafka Streams**
Disini kita membuat Demo Kafka Streams dengan WordCount Application
pertama kita membuat class di java

```
public class WordCountApplication {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
```

 Untuk menghitung kata, pertama-tama kita dapat memodifikasi operator flatMapValues untuk memperlakukan semuanya sebagai huruf kecil (dengan asumsi ekspresi lambda digunakan):
 
 ```
 source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
    @Override
    public Iterable<String> apply(String value) {
        return Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+"));
    }
 ```
 Untuk melakukan agregasi penghitungan, pertama-tama kita harus menentukan bahwa kita ingin memasukkan aliran pada string nilai, yaitu kata dengan huruf kecil, dengan operator groupBy. Operator ini menghasilkan aliran grup baru, yang kemudian dapat digabungkan dengan operator penghitungan, yang menghasilkan penghitungan berjalan pada masing-masing kunci yang dikelompokkan:
 ```
 KTable<String, Long> counts =
source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                return Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+"));
            }
        })
      .groupBy(new KeyValueMapper<String, String, String>() {
           @Override
           public String apply(String key, String value) {
               return value;
           }
        })
      // Materialize the result into a KeyValueStore named "counts-store".
      // The Materialized store is always of type <Bytes, byte[]> as this is the format of the inner most store.
      .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>> as("counts-store"));
```

Perhatikan bahwa operator penghitungan memiliki parameter Terwujud yang menentukan bahwa penghitungan yang berjalan harus disimpan di penyimpanan status bernama penghitungan-penyimpanan. Penyimpanan jumlah-toko ini dapat ditanyakan secara real-time, dengan rincian yang dijelaskan dalam Panduan Pengembang.

Kita juga dapat menulis kembali aliran changelog hitungan KTable ke topik Kafka lainnya, misalnya stream-wordcount-output. Karena hasilnya adalah aliran changelog, topik keluaran stream-wordcount-output harus dikonfigurasi dengan pemadatan log yang diaktifkan. Perhatikan bahwa kali ini tipe nilainya bukan lagi String melainkan Long, sehingga kelas serialisasi default tidak lagi dapat ditulis ke Kafka. Kita perlu menyediakan metode serialisasi yang diganti untuk tipe Long, jika tidak, pengecualian runtime akan muncul:

```
KStream<String, String> source = builder.stream("streams-plaintext-input");
source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
      .groupBy((key, value) -> value)
      .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
      .toStream()
      .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

```
Jika kita mendeskripsikan lagi topology augmented ini sebagai `System.out.println(topology.describe())` , kita akan mendapatkan yang berikut:
```
Sub-topologies:
  Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000(topics: streams-plaintext-input) --> KSTREAM-FLATMAPVALUES-0000000001
    Processor: KSTREAM-FLATMAPVALUES-0000000001(stores: []) --> KSTREAM-KEY-SELECT-0000000002 <-- KSTREAM-SOURCE-0000000000
    Processor: KSTREAM-KEY-SELECT-0000000002(stores: []) --> KSTREAM-FILTER-0000000005 <-- KSTREAM-FLATMAPVALUES-0000000001
    Processor: KSTREAM-FILTER-0000000005(stores: []) --> KSTREAM-SINK-0000000004 <-- KSTREAM-KEY-SELECT-0000000002
    Sink: KSTREAM-SINK-0000000004(topic: counts-store-repartition) <-- KSTREAM-FILTER-0000000005
  Sub-topology: 1
    Source: KSTREAM-SOURCE-0000000006(topics: counts-store-repartition) --> KSTREAM-AGGREGATE-0000000003
    Processor: KSTREAM-AGGREGATE-0000000003(stores: [counts-store]) --> KTABLE-TOSTREAM-0000000007 <-- KSTREAM-SOURCE-0000000006
    Processor: KTABLE-TOSTREAM-0000000007(stores: []) --> KSTREAM-SINK-0000000008 <-- KSTREAM-AGGREGATE-0000000003
    Sink: KSTREAM-SINK-0000000008(topic: streams-wordcount-output) <-- KTABLE-TOSTREAM-0000000007
Global Stores:
  none

```
 Seperti yang bisa kita lihat di atas, topologi sekarang berisi dua sub-topologi yang tidak terhubung. Node sink sub-topologi pertama `KSTREAM-SINK-0000000004` akan menulis ke topik partisi ulang jumlah-penyimpanan-partisi ulang, yang akan dibaca oleh node sumber sub-topologi kedua `KSTREAM-SOURCE-0000000006`. Topik partisi ulang digunakan untuk "mengacak" aliran sumber berdasarkan kunci agregasinya, yang dalam hal ini adalah string nilai. Selain itu, di dalam sub-topologi pertama, node `KSTREAM-FILTER-0000000005` tanpa kewarganegaraan dimasukkan di antara node pengelompokan `KSTREAM-KEY-SELECT-0000000002` dan node sink untuk menyaring rekaman perantara yang kunci agregatnya kosong.

Dalam sub-topologi kedua, simpul agregasi `KSTREAM-AGGREGATE-0000000003` dikaitkan dengan penyimpanan negara bernama counts-store (nama ditentukan oleh pengguna dalam operator hitungan). Setelah menerima setiap catatan dari node sumber aliran berikutnya, pemroses agregasi pertama-tama akan menanyakan penyimpanan jumlah penyimpanan terkait untuk mendapatkan jumlah saat ini untuk kunci tersebut, menambah satu, dan kemudian menulis jumlah baru kembali ke penyimpanan. Setiap jumlah kunci yang diperbarui juga akan disalurkan ke hilir ke node `KTABLE-TOSTREAM-0000000007`, yang menafsirkan aliran pembaruan ini sebagai aliran rekaman sebelum disalurkan lebih lanjut ke node sink `KSTREAM-SINK-0000000008` untuk menulis kembali ke Kafka.

Kode lengkapnya terlihat seperti ini (dengan asumsi ekspresi lambda digunakan):
```
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class WordCountApplication {
    public static void main(String[] args) throws Exception{

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("streams-test-wordcount-input");
        source.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .groupBy((key, value) -> value)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
                .toStream()
                .to("streams-test-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);


    }

}

```
Dan jika kita jalanin streams akan running secara terus menerus seperti pada di gambar

![Screenshot from 2023-11-02 17-12-30](https://github.com/mfahryan/Learning-Kafka/assets/112185850/1bd5699e-f824-47e3-9cd9-cd119299ff4b)

**5.2 Membuat Producer Kafka Streams**

Disini saya membangun Producer dengan:
```
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic streams-test-wordcount-input
give me sometime to understand

```
```
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic streams-test-wordcount-input
give me sometime to understand
give me someone to being grateful

```

**5.3 Membuat Consumer Kafka Streams**

Disini saya membangun Consumer dengan Pesan yang akan diproses oleh aplikasi Wordcount dan data keluaran berikut akan ditulis ke topik stream-wordcount-output dan dicetak oleh konsumen konsol: disini saya membuat topik keluaran dengan pemadatan diaktifkan karena aliran keluaran adalah aliran log perubahan: 
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
give		1
me		1
someone		1
to		1
understand	1

```
lalu saya input data yang kedua berupa : `give me someone to being grateful` dan hasilnya ialah

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
give		1
me		1
sometime		1
to		1
understand	1
give		2
me		2
someone		1
to		2
being		1
grateful	1	

```
seperti pada gambar dibawah : 

![Screenshot from 2023-11-02 17-31-12](https://github.com/mfahryan/Learning-Kafka/assets/112185850/08331fb9-ab8b-42ab-a24d-ebe0c0476c72)

jika ditabelkan maka hasilnya seperti gambar dibawah : 

![table2 drawio](https://github.com/mfahryan/Learning-Kafka/assets/112185850/9976403a-a7a2-491a-b5c0-75e94dabb14d)

**5.4 Membuat Ktable**

Ktable merupakan "Sepotong peristiwa/slice of events" dari stream pada titik waktu tertentu - sering kali merupakan "waktu terkini".

jadi tabel adalah tampilan event stream. dan tampilan ini terus diupdate setiap ada event baru yang masuk.

tabel juga dapat berupa kumpulan peristiwa atau data aliran, misalnya jika tweet oleh mr bond dianggap sebagai "Stream" maka jika kita menggabungkan jumlah tweet dalam 30 kali terakhir, maka itu akan menjadi tabel kafka atau Ktable

tabel kafka bisa berubah. baris acara baru dapat disisipkan, dan baris yang ada dapat diperbarui dan dihapus
mungkin jika buat dalam gamber seperti ini :

![ktable drawio](https://github.com/mfahryan/Learning-Kafka/assets/112185850/d7fe265a-3ee0-4707-958f-21cce6ef02e3)

jadi disini kita demo dengan java dan ingin men-stream nilai 

```
var rawRecords = List.of(
                            "orderNumber-1001",
                            "orderNumber-5000",
                            "orderNumber-999",
                            "orderNumber-3330",
                            "bogus-1",
                            "bogus-2",
                            "orderNumber-1003");

```

setalah itu kita ingin membuat table dengan cara :
1. Pertama kita membuat topic input,output ktable, dan membuat value apa yang ingin kita ambil

   ```
   final String inputTopic = "inputKtable";
   final String outputTopic = "outputKtable";
   final String orderNumberStart = "orderNumber-", bogusStart = "bogus-";

   ```
2.  Lalu membuat stream builder metode table dengan variable inputtopic
   ```

   KTable<String, String> firstKTable = builder.table(inputTopic,
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("ktable-store")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));

   ```

3. Memfilter Ktable dengan variable orderNumberStart dan BogusStart
   
   ```
   firstKTable.filter((key, value) -> value.contains(orderNumberStart) || value.contains(bogusStart))
                .mapValues(value -> value.substring(value.indexOf("-") + 1))
                .filter((key, value) -> Long.parseLong(value) > 1)
                .toStream().peek((key, value) -> System.out.println("Outgoing record - key " +key+ " value " + value))
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

   ```

4. Lalu membuat Topic loader dan membuat class run producer dengan adminclient dimana value yang ingin kita load bisa kita ubah dengan mudah

   ```
    public class TopicLoader {
            public static void main(String[] args) throws IOException {
                runProducer();
            }

            public static void runProducer() throws IOException {
                Properties props = new Properties();
                props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

                try (Admin adminClient = Admin.create(props);
                     Producer<String, String> producer = new KafkaProducer<>(props)) {
                    final String inputTopic = "inputKtable";
                    final String outputTopic = "outputKtable";
                    var topics = List.of(new NewTopic(inputTopic, 3, (short) 1), (new NewTopic(outputTopic, 3, (short) 1)));
                    adminClient.createTopics(topics);

                    Callback callback = (metadata, exception) -> {
                        if (exception != null) {
                            System.out.printf("Producing records encountered error %s %n", exception);
                        } else {
                            System.out.printf("Record produced - offset - %d timestamp - %d %n", metadata.offset(), metadata.timestamp());
                        }

                    };



                    var rawRecords = List.of(
                            "orderNumber-1001",
                            "orderNumber-5000",
                            "orderNumber-999",
                            "orderNumber-3330",
                            "bogus-1",
                            "bogus-2",
                            "orderNumber-1003");
                    var producerRecords = rawRecords.stream().map(r -> new ProducerRecord<String, String>(inputTopic, "order-key", r)).toList();
                    producerRecords.forEach((pr -> producer.send(pr, callback)));
                }
            }
        }

   ```

5. Lalu di Ktable kita memanggil dan membuat streams agar untuk run producer di topic loader

   ```
    try (KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props)) {
            final CountDownLatch shutdownLatch = new CountDownLatch(1);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                kafkaStreams.close(Duration.ofSeconds(2));
                shutdownLatch.countDown();
            }));
            TopicLoader.runProducer();
            try {
                kafkaStreams.start();
                shutdownLatch.await();
            } catch (Throwable e) {
                System.exit(1);
            }
        }
    System.exit(0);

    }
}


Lalu hasilnya jika kita masukkan value nya :

```
 var rawRecords = List.of(
                            "orderNumber-1001",
                            "orderNumber-5000",
                            "orderNumber-999",
                            "orderNumber-3330",
                            "bogus-1",
                            "bogus-2",
                            "orderNumber-8400");

```

maka hasilnya adalah :

![streams1](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6a4675f4-e437-4253-a456-1c8820cc26ea)

dan hasil pada KTable nya :

`8400`

# 6. Membuat Connector Menggunakan Kafka Connect Untuk Melakukan Koneksi Ke RDBMS
## 6.1 Apa itu RDBMS
### 6.1.1 Database management system
DBMS adalah singkatan dari sistem manajemen basis data. Sistem Manajemen Basis Data dapat didefinisikan sebagai perangkat lunak yang membantu dalam mengatur, mengendalikan dan menggunakan data yang dibutuhkan oleh program aplikasi. Mereka menyediakan fasilitas untuk membuat dan memelihara database yang terorganisir dengan baik. Sistem ini terutama digunakan untuk mengembangkan dan menganalisis database pengguna tunggal dan tidak dimaksudkan untuk dibagikan melalui jaringan atau Internet, melainkan diinstal pada perangkat dan bekerja dengan satu pengguna pada satu waktu.

Berbagai operasi yang dapat dilakukan pada file-file ini antara lain menambahkan file baru ke database, menghapus file yang ada dari database, memasukkan data ke dalam file yang sudah ada, mengubah data di file yang sudah ada, menghapus data di file yang sudah ada, dan mengambil atau menanyakan data dari file yang sudah ada. . Paket DBMS umumnya menyediakan antarmuka untuk melihat dan mengubah desain database, membuat query, dan mengembangkan laporan. Sistem Manajemen Basis Data yang tersedia secara komersial adalah Oracle, MySQL, SQL Server dan DB2 dll., sedangkan Microsoft Access dan Open Office Base adalah contoh DBMS pribadi.



### 6.1.2 Apa itu RDBMS(Relational Data Base Management System)
RDBMS adalah singkatan dari sistem manajemen basis data relasional. Basis Data Relasional memungkinkan definisi data dan strukturnya, operasi penyimpanan dan pengambilan, serta batasan integritas yang dapat diatur dalam struktur Tabel. Tabel adalah kumpulan catatan, dan setiap catatan dalam tabel berisi bidang yang sama, yang menentukan sifat data yang disimpan dalam tabel. Catatan adalah salah satu contoh dari sekumpulan bidang dalam tabel.
Tiga istilah kunci yang digunakan secara luas dalam model database relasional:
1. Relasi : adalah tabel dengan kolom dan baris
2. Atribut: Kolom yang diberi nama pada relasi disebut atribut (bidang); Dan
3. Domain: Ini adalah kumpulan nilai yang dapat diambil oleh atribut.

### 6.1.3 Keuntungan RDBMS

- **Kesederhanaan Model**
  Berbeda dengan jenis model database lainnya, model database relasional jauh lebih sederhana. Itu tidak memerlukan kueri yang rumit karena tidak memiliki pemrosesan atau 	 
  penataan kueri sehingga kueri SQL sederhana sudah cukup untuk menangani data.

- **Kemudahan penggunaan**
  Pengguna dapat dengan mudah mengakses/mengambil informasi yang diperlukan dalam hitungan detik tanpa harus terlibat dalam kompleksitas database. Structured Query Language (SQL) 
  digunakan untuk mengeksekusi query yang kompleks.

- **Ketepatan**
  Fitur utama dari database relasional adalah database tersebut didefinisikan secara ketat dan terorganisir dengan baik, sehingga data tidak terduplikasi. Basis data relasional 
  memiliki keakuratan karena strukturnya tanpa duplikasi data.

- **Integritas data**
  Basis data RDBMS juga banyak digunakan untuk integritas data karena memberikan konsistensi di semua tabel. Integritas data memastikan fitur-fitur seperti akurasi dan kemudahan  
  Penggunaan.

- **Normalisasi**
  Ketika data menjadi semakin kompleks, kebutuhan akan cara penyimpanan yang efisien pun semakin meningkat. Normalisasi adalah metode yang memecah informasi menjadi beberapa 
  bagian yang dapat dikelola untuk mengurangi ukuran penyimpanan. Data dapat dipecah menjadi beberapa level yang berbeda dengan level mana pun yang memerlukan persiapan sebelum 
  berpindah ke level lain untuk menormalkan data Anda.

  Normalisasi basis data juga memastikan bahwa basis data relasional tidak memiliki variasi atau varian dalam strukturnya dan dapat dimanipulasi secara akurat. Hal ini memastikan   Integritas tetap terjaga saat menggunakan data dari database ini untuk keputusan bisnis Anda.

- **Kolaborasi**
  Beberapa pengguna dapat mengakses database untuk mengambil informasi pada saat yang sama dan bahkan jika data sedang diperbarui.

- **Keamanan**
  Data aman karena Sistem Manajemen Basis Data Relasional hanya mengizinkan pengguna yang berwenang untuk mengakses data secara langsung. Tidak ada pengguna yang tidak sah yang 
  dapat mengakses informasi tersebut.

### 6.1.4 Kekurangan RDBMS

- **Masalah Pemeliharaan**
  Pemeliharaan database relasional menjadi sulit seiring berjalannya waktu karena bertambahnya data. Pengembang dan pemrogram harus menghabiskan banyak waktu untuk memelihara 
  database.

- **Biaya**
  Sistem database relasional mahal untuk diatur dan dipelihara. Biaya awal untuk perangkat lunaknya saja bisa sangat mahal untuk bisnis kecil, namun akan menjadi lebih buruk jika 
  Anda mempertimbangkan untuk menyewa teknisi profesional yang juga harus memiliki keahlian dengan jenis program tertentu.

- **Penyimpanan Fisik**
  Basis data relasional terdiri dari baris dan kolom, yang memerlukan banyak memori fisik karena setiap operasi yang dilakukan bergantung pada penyimpanan terpisah. Kebutuhan  
  Memori fisik dapat meningkat seiring dengan bertambahnya data.

- **Kurangnya Skalabilitas**
  Saat menggunakan database relasional melalui beberapa server, strukturnya berubah dan menjadi sulit untuk ditangani, terutama ketika jumlah datanya besar. Oleh karena itu, data 
  tidak dapat diskalakan pada server penyimpanan fisik yang berbeda. Pada akhirnya, kinerjanya terpengaruh, misalnya kurangnya ketersediaan data dan waktu buka, dll. Ketika basis 
  data menjadi lebih besar atau lebih terdistribusi dengan jumlah server yang lebih banyak, hal ini akan berdampak negatif seperti masalah latensi dan ketersediaan yang 
  memengaruhi kinerja secara keseluruhan.

- **Kompleksitas dalam Struktur**
  Basis data relasional hanya dapat menyimpan data dalam bentuk tabel sehingga sulit untuk merepresentasikan hubungan kompleks antar objek. Ini merupakan masalah karena banyak 
  aplikasi memerlukan lebih dari satu tabel untuk menyimpan semua data penting yang diperlukan oleh logika aplikasinya.

- **Penurunan kinerja seiring waktu**
  Basis data relasional bisa menjadi lebih lambat, bukan hanya karena ketergantungannya pada banyak tabel. Ketika terdapat banyak tabel dan data dalam sistem, hal ini menyebabkan 
  peningkatan kompleksitas. Hal ini dapat menyebabkan waktu respons yang lambat atas pertanyaan atau bahkan kegagalan total tergantung pada berapa banyak orang yang masuk ke  
  Server pada waktu tertentu.


## 6.2 Membuat Connector Menggunakan Kafka Connect dan Melakukan Koneksi Ke RDBMS
### 6.2.1 Cara Melakukan Kafka Connect dan Plugin Connector JDBC dan Mysql-Connector
Untuk start kafka connect sangat mirip dengan start broker yaitu memanggil script awal dengan file properti :
`bin/connect-distributed.sh config/connect-distributed.properties`
Ada beberapa konfigurasi utama untuk Connect workers :
  1. bootstrap.servers
  2. group.id
  3. plugin.path
  4. key.converter and value.converter
  5. rest.host.name and rest.port
 
Setelah pekerja aktif dan Anda memiliki sebuah cluster, pastikan cluster tersebut sudah aktif dan berjalan
memeriksa REST API:
`curl http://localhost:8083/`
`{"version":"3.6.0","commit":"60e845626d8a465a","kafka_cluster_id":"AK1_SnLZSo-ZRQZ8VgEUGg"}`

setelah itu check connector plugin dengan :
`curl http://localhost:8083/connector-plugins`

![connectorplugins](https://github.com/mfahryan/Learning-Kafka/assets/112185850/5d824ace-32ca-49d3-b6c3-04fb8fa903fd)

disini saya sudah berhasil meng-input connector JDBC(Java Data Base Connector) kedalam Kafka connector.

lalu selanjutnya download mysql-connector dari web [Kafka-Connector](https://client.hub.confluent.io/confluent-hub-client-latest.tar.gz)
setelah berhasil download Sekarang kita perlu memuat konektor ini dengan cara :
1. Buat direktori, seperti /opt/connectors/jdbc
2. Dan perbarui config/connect-distributed.properties untuk menyertakan plugin.path=/opt/connectors/jbc

### 6.2.2 Membuat Tabel di MySql dan Streaming ke Kafka menggunakan JDBC Connector

Pertama saya masuk melalui server mysql :

```
mysql -u root -p

mysql> create database test;
Query OK, 1 row affected (0.00 sec)

mysql> use test;
Database changed

mysql> create table login (username varchar(30), login_time datetime);
Query OK, 0 rows affected (0.02 sec)

mysql> insert into login values ('gwenchana', now());
Query OK, 1 row affected (0.01 sec)
mysql> insert into login values ('are you okay?', now());
Query OK, 1 row affected (0.00 sec)

```
Seperti yang Anda lihat, kami membuat database dan tabel, dan menyisipkan beberapa baris sebagai contoh.

Langkah selanjutnya adalah mengkonfigurasi konektor sumber JDBC kami. Kita dapat mengetahui opsi konfigurasi mana yang tersedia dengan melihat dokumentasinya, namun kita juga dapat menggunakan REST API untuk menemukan opsi konfigurasi yang tersedia:

```
curl -X PUT -d '{"connector.class":"JdbcSource"}' localhost:8083/connector-plugins/JdbcSourceConnector/config/validate/ --header "content-Type:application/json"

```

```
{
"configs": [
{
"definition": {
"default_value": "",
"dependents": [],
"display_name": "Timestamp Column Name",
"documentation": "The name of the timestamp column to use
to detect new or modified rows. This column may not be
nullable.",
"group": "Mode",
"importance": "MEDIUM",
"name": "timestamp.column.name",
"order": 3,
"required": false,
"type": "STRING",
"width": "MEDIUM"
},
<more stuff>
```

Kami meminta REST API untuk memvalidasi konfigurasi konektor dan mengirimkannya konfigurasi hanya dengan nama kelas (ini adalah konfigurasi minimum yang diperlukan). Sebagai tanggapan, kami mendapatkan definisi JSON dari semua konfigurasi yang tersedia.

Dengan mengingat informasi ini, saatnya membuat dan mengonfigurasi konektor JDBC kita:

```
echo '{"name":"mysql-login-connector", "config":{"connector.class":"io.confluent.connect.jdbc.JdbcSourceConnector","connection.url":"jdbc:mysql://127.0.0.1:3306/test?user=root&password=F#####","mode":"timestamp","table.whitelist":"login","vali-
date.non.null":false,"timestamp.column.name":"login_time","topic.pre-fix":"mysql."}}' | curl -X POST -d @- http://localhost:8083/connectors --header "content Type:application/json"

```

Mari kita pastikan ini berhasil dengan membaca data dari topik mysql.login:

![consumerkafkaconnect](https://github.com/mfahryan/Learning-Kafka/assets/112185850/ed93bce0-a207-48f7-b61a-7e90b6f257f2)

Setelah konektor berjalan, jika Anda memasukkan baris tambahan di tabel login, akan segera melihatnya tercermin dalam topik mysql.login.



![kafka](https://github.com/mfahryan/Learning-Kafka/assets/112185850/d6a7956c-d259-4be3-b6e7-6482d3a5d15e)










 
  



  
























