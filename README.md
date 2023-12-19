# Learning-Kafka

cluster = kumpulan beberapa aplikasi kafka

retention policy

log adalah data yang bertambah terus

log retention time = 
log retention bytes = 
offset retention time = lokasi terakhir subscribe mengconsume data atau seberapa lama data offset disimpan di kafka

## KTable
Ktable digunakan untuk menyimpan status aplikasi kita
mereka berbasis nilai kunci, sehingga cocok dengan pesan kunci/nilai kafka dan konsep partisi

tabel dapat direpresentasikan sebagai Streams
setiap kali terjadi perubahan pada tabel, perubahan ini dapat dialirkan,
dalam arti tertentu, "Stream" dapat dilihat sebagai log perubahan untuk sebuah tabel

juga, aliran dapat digunakan untuk merekonstruksi tabel, dengan menerapkan operasi penyisipan atau pembaruan pada kunci yang sama

## message key
-key menyimpan informasi tambahan bersama dengan value
-key juga membantu dalam pemilihan partisi
-semua record dengan key yang sama masuk ke beberapa partisi
-jika key nya null maka partisi acak digunakan
-hash of the key is used to find partition
-Produser membutuhkan metadata data tentang setiap partisi pada broker untuk mengambil keputusan

## Key Value (K,V)
Kafka menggunakan abstraksi log terdistribusi yang terdiri dari partisi. Memisahkan log menjadi beberapa partisi memungkinkan untuk memperluas skala sistem.

`key` digunakan untuk menentukan partisi dalam log tempat pesan ditambahkan. Sedangkan `value` adalah payload pesan yang sebenarnya. Contoh-contoh tersebut sebenarnya tidak terlalu "baik" dalam hal ini; biasanya Anda akan memiliki tipe kompleks sebagai nilai (seperti tipe tupel atau JSON atau serupa) dan Anda akan mengekstrak satu bidang sebagai kunci.

Secara umum `key` dan/atau nilainya juga bisa `null`. Jika `key` adalah `null`,random partisi akan dipilih `roundrobin`. Jika `value` `null`, ia dapat memiliki semantik "hapus" khusus jika Anda mengaktifkan pemadatan log alih-alih kebijakan penyimpanan log untuk suatu topik

Dan yang penting, kunci juga memainkan peran yang relevan dalam API streaming Kafka, dengan KStream dan KTable 

Menentukan `key` sehingga semua pesan pada `key` yang sama masuk ke partisi yang sama, sangat penting untuk pengurutan pemrosesan pesan yang tepat jika Anda akan memiliki banyak konsumen dalam grup konsumen pada suatu topik.

Tanpa `key`, dua pesan pada `key` yang sama dapat masuk ke partisi berbeda dan diproses oleh konsumen berbeda dalam grup secara tidak berurutan.

## topology

Topologi adalah grafik asiklik sumber, prosesor, dan sink. Sumber adalah simpul dalam grafik yang menggunakan satu atau beberapa topik Kafka dan meneruskannya ke simpul penerusnya. Prosesor adalah sebuah node dalam grafik yang menerima rekaman masukan dari node hulu, memproses rekaman tersebut, dan secara opsional meneruskan rekaman baru ke satu atau semua node hilirnya. Terakhir, sink adalah node dalam grafik yang menerima catatan dari node upstream dan menulisnya ke topik Kafka. Topologi memungkinkan Anda membuat grafik asiklik dari node-node ini, dan kemudian diteruskan ke instance KafkaStreams baru yang kemudian akan mulai menggunakan, memproses, dan memproduksi rekaman.


## RDBMS

![JDBC-connector](https://github.com/mfahryan/Learning-Kafka/assets/112185850/d88032d6-c210-4f35-b5b4-6186cd7f5a8f)

Konektor MySQL Kafka adalah alat yang berguna untuk mengalirkan data dari database eksternal seperti MySQL ke Topik Kafka. Alat ini memungkinkan Anda mengambil data dari tabel database relasional MySQL dan mencatat semua perubahan tingkat baris yang dihasilkan pada data Anda. Konektor tersedia di situs web Confluent dan disebut Confluent JDBC MySQL Source Connector. Ini berjalan di cloud.

Konektor MySQL Kafka mendukung berbagai format data seperti Avro, Skema JSON, Protobuf, atau JSON untuk menyinkronkan semua peristiwa database untuk setiap tabel database sebagai Topik Kafka terpisah. Dengan menggunakan MySQL ke Kafka Connector, Anda dapat mentransfer data penting yang berada di tabel database MySQL Anda seperti informasi pelanggan, dan data pemangku kepentingan, serta melakukan Pemrosesan Aliran pada data ini menggunakan fungsi bawaan Kafka.


## control center
kesalahan saya mengurangi partisi yang sudah dibuat 12 lalu saya buat menjadi 2
alhasil control center tidak bisa berjalan 

## offset topic
untuk melacak kemajuan kelompok konsumen saat mereka menggunakan pesan dari topik Kafka.



# SSL

port web http 8080 https 443


kenapa di matiin keystore nya?
Kalau lagi pake SASL, biasanya keystore di matiin di producer sama consumer karena SASL udah nanganin urusan autentikasi, jadi keystore yang biasanya buat sertifikat gak terlalu diperlukan.

Jadi intinya, SASL itu bikin proses autentikasi jadi lebih simpel, gak perlu repot-repot pake keystore buat sertifikat. jadi kayak bilang ke SASL, "Eh, urusin ini autentikasi pake kredensial ini ya," terus SASL yang nangani tanpa perlu repot-repot sama keystore.



cara copy dari server ke local

scp -r  root@192.168.10.231:/etc/pki/certs/kafka-client /home/muhammad/Downloads


curl -X GET http://br1kafka.dev.alldataint.com:8081/permission -u admin
curl -X GET http://br1kafka.dev.alldataint.com:8081
