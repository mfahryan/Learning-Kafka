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

lalu untuk consumer nya sendiri dengan code seperti dibawah : 

```
from kafka import KafkaConsumer

# Konfigurasi consumer
consumer = KafkaConsumer(
    'nama_topik',
    bootstrap_servers='localhost:9092',
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


