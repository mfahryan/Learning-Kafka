# Install CP via Package Manager with all services (zookeeper, kafka, schema registry, kafka connect, ksqldb, kafka rest, Control center) with security enabled (SASL_SSL)
- source :
* [Confluent Platform](https://docs.confluent.io/platform/current/installation/installing_cp/rhel-centos.html)

## 1. Instalasi

Didalam Dokumentasi ini saya meng-install Platform Confluent memakai systemd
1. Pertama saya meginstall Confluent Platform public key.
`sudo rpm --import https://packages.confluent.io/rpm/7.5/archive.key`
2. Lalu saya ke folder `/etc/yum.repos.d` dan membuat file dengan `touch confluent.repo`
3. Masukkan variable berikut ke dalam `~/.bashrc`
```
[Confluent]
name=Confluent repository
baseurl=https://packages.confluent.io/rpm/7.5
gpgcheck=1
gpgkey=https://packages.confluent.io/rpm/7.5/archive.key
enabled=1

[Confluent-Clients]
name=Confluent Clients repository
baseurl=https://packages.confluent.io/clients/rpm/centos/$releasever/$basearch
gpgcheck=1
gpgkey=https://packages.confluent.io/clients/rpm/archive.key
enabled=1
```
4. Lalu clear yum caches dan install Confluent Platform
```
sudo yum clean all && \
sudo yum install confluent-platform && \
sudo yum install confluent-security
```
5. Jika sudah membuat confluent home nya dan export path nya selanjutnya koonfigurasi zookeeper.properties(`/etc/kafka/zookeeper.properties`), server.properties (`/etc/kafka/server.properties`), confluent control center properties (`/etc/confluent-control-center/control-center-production.properties`), Kafka Connect properties file (/etc/kafka/connect-distributed.properties), Confluent REST Proxy properties file (/etc/kafka-rest/kafka-rest.properties), dan Schema Registry properties file (/etc/schema-registry/schema-registry.properties

## 2. Menjalankan C3 (Confluent Control Center)

untuk menjalankan c3 cukup mudah karena saya disini masih memakai systemd jadi cara menjalankannya sebagai berikut : 
1. Menjalankan confluent-zookeeper dengan
   
   `sudo systemctl start confluent-zookeeper`
   
3. Menjalankan confluent-server
   
   `sudo systemctl start confluent-server`
   
4. Menjalankan Schema-registry
   
   `sudo systemctl start confluent-schema-registry`
   
5. Selanjutnya ada beberapa komponen di Confluent Platform seperti
   
   - Control Center
     
   `sudo systemctl start confluent-control-center`
   
   - Kafka Connect

   `sudo systemctl start confluent-kafka-connect`

   - Confluent REST Proxy
     
   `sudo systemctl start confluent-kafka-rest`

   - KsqlDB
     
   `sudo systemctl start confluent-ksqldb`

## 3. Selanjutnya Meng Explore C3 (Confluent Control System)

### 3.1 Membuat Topic dan Mendelete Topic

Disini saya sudah dapat memasuki Confluent control System dapat dilihat di bawah.

![c3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/5ebb6eeb-b568-4eda-bed9-0ebba9ef62f6)

lalu untuk membuat topic cukup mudah tinggal press/tekan add topic seperti yang ada pada gambar dibawah dan kita bisa memasukkan nama topic sesuai yang kita mau berikut dengan partisi yang kita mau seperti gambar yang berada di bawah : 

![buattopicc3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/c8464027-65ad-421f-aed3-56b11ca4e5a7)

![Screenshot from 2023-11-19 18-57-33](https://github.com/mfahryan/Learning-Kafka/assets/112185850/77140271-ad4b-47a8-9eb6-4ef11830dca9)


dan untuk delete topic juga cukup mudah hanya tingal press/tekan delete topic yang terdapat pada gambar dibawah : 


![Screenshot from 2023-11-19 18-57-53](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6960be60-a504-48a6-b085-0eefaa35e4ed)

![Screenshot from 2023-11-19 18-58-04](https://github.com/mfahryan/Learning-Kafka/assets/112185850/7a501e55-94ca-4fa3-a5df-76a75154a977)


kita juga dapat mengatur waktu untuk menghapus topic dan ada beberapa opsi seperti :

1. Delete : untuk menghapus pesan di partisi dalam topik kafka dan rentang nya tergantung retention time yang kita inginkan disini terdapat opsi 1 jam, 1 hari, 1 minggu, 1 tahun,dan infinite
   
2. Compact : Jika didalam topic terdapat key unique maka key unique tersebutlah yang hanya disimpan, jika ada key yg sama maka key yg pertama masuk itu hapus.
   dan compact itu dia hanya mempertahankan last message untuk setiap key dan message sebelumnya itu bakal di hapus

pertanyaan : Kalau kita set compact dan key nya null apa yang terjadi?

jawaban : jika key nya null itu berarti semua pesan itu punya key, jadi tidak ada penghapusan dan tetep ada, jika key nya null semua pesan itu dianggap key nya sama

delete+compact  = kita bisa ngatur ni penghapusan sama rentang waktu yg sesuai sama kebutuhan.


![Screenshot from 2023-11-19 18-58-13](https://github.com/mfahryan/Learning-Kafka/assets/112185850/8245bbec-1df2-4b57-bfcb-2edc86f7eba0)


pertanyaan : Kalau kita set compact dan key nya null apa yang terjadi?

jawaban : jadi kalo key nya null itu berarti semua pesan itu punya key jadi tidak ada penghapusan dan tetep ada, jika key nya null semua pesan itu dianggap key nya sama

delete+compact  = kita bisa ngatur ni penghapusan sama rentang waktu yg sesuai sama kebutuhan

![Screenshot from 2023-11-19 18-58-18](https://github.com/mfahryan/Learning-Kafka/assets/112185850/bccd7174-6941-4ec9-babd-103024f2e688)

### 3.2 MemProduce dan Mengconsume Via C3

Lalu selanjutnya untuk memproduce message ke topic disini saya menggunakan nama topic "latihan1" kita bisa langsung memproduce message di Contfluent Control Center dan juga dapat meng Consume langsung di C3 seperti gambar dibawah : 

![Screenshot from 2023-11-19 19-00-45](https://github.com/mfahryan/Learning-Kafka/assets/112185850/a4c44ea3-8a1b-40d7-8ec6-5f5ce53e0936)

![Screenshot from 2023-11-19 19-00-52](https://github.com/mfahryan/Learning-Kafka/assets/112185850/92d6de71-115c-4e8e-bd27-32e5e0796728)

### 3.3 Membuat Connector Source/Sink Via C3

Lalu gambar dibawah berupa connector source dan sink yang saya gunakan yaitu JDBC seperti gambar dibawah :


![Screenshot from 2023-11-19 19-01-31](https://github.com/mfahryan/Learning-Kafka/assets/112185850/7f5f4ebb-a976-4768-a851-ef8c02242336)

### 3.4 Produce dan Consume Via Kafka Rest

Selanjutnya saya mencoba menggunakan Kafka Rest untuk membuat topic dan memproduce topic berikut saya membuat topic "purchases" dengan ada beberapa key dan value :

```
curl -X POST \
     -H "Content-Type: application/vnd.kafka.json.v2+json" \
     -H "Accept: application/vnd.kafka.v2+json" \
     --data '{"records":[{"key":"jsmith","value":"alarm clocks"},{"key":"htanaka","value":"batteries"},{"key":"awalther","value":"bookshelves"}]}' \
     "http://br1kafka.dev.alldataint.com:8082/topics/purchases"
```

![topicrest](https://github.com/mfahryan/Learning-Kafka/assets/112185850/b27c8668-7eca-430c-8fe9-1cff5f128c77)

![consumec3purchases](https://github.com/mfahryan/Learning-Kafka/assets/112185850/11278e8a-7533-490f-9069-0d047da86bd6)

Lalu saya membuat 1 topic lagi karena saya mau mencoba untuk latihan partisi, disini saya membuat topic "latihan partisi" dengan partisi 3.

```
curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" -H "Accept: application/vnd.kafka.v2+json"\
--data '{"partitions": 3}' \
  http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
```

setelah itu saya ingin memproduce message 1 sampai 10 karena saya ingin melihat semua messagenya ada di offset berapa aja dan ternyata message yang pertama masuk ke partisi 0

```
#produce meesage ke topic latihanpartisi tanpa key

for i in {1..10}
do
   curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" \
   --data '{"records":[{"value":{"message":"Message '$i'"}}]}' \
   http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
done
```
![Screenshot from 2023-11-24 10-34-22](https://github.com/mfahryan/Learning-Kafka/assets/112185850/b72409bf-ea67-4988-ad4c-8a3c747a9891)



selanjutnya saya ingin memproduce message nya lagi tetapi dengan key random atau key "NULL" dan ternyata message yang kedua masuk ke partisi 2

```
#produce meesage ke topic latihanpartisi key null
for i in {1..10}
do
   curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" \
   --data '{"records":[{"key":"null","value":{"message":"Message '$i'"}}]}' \
   http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
done
```
![Screenshot from 2023-11-24 10-35-29](https://github.com/mfahryan/Learning-Kafka/assets/112185850/a8ced059-74ac-41c5-97b2-541872165553)


### 3.5 Membuat stream dan table dengan KSQLDB

#### 3.5.1 Pertama saya membuat Stream dengan nama variable readings seperti yang ada dibawah

```
CREATE STREAM readings (
  sensor VARCHAR KEY,
  reading DOUBLE,
  area VARCHAR
) WITH (
  kafka_topic = 'readings',
  value_format = 'json',
  partitions = 3
);
```
#### 3.5.2 Lalu saya membuat Table dengan nama brands

```
CREATE TABLE brands (
  sensor VARCHAR PRIMARY KEY,
  brand_name VARCHAR
) WITH (
  kafka_topic = 'brands',
  value_formant = 'json',
  partitions = 3
);
```

### 3.5.3 Lalu saya membuat nama brand dan valuenya

```
INSERT INTO BRANDS(SENSOR, BRAND_NAME) VALUES ('sensor-1','Cassin Inc');
INSERT INTO BRANDS(SENSOR, BRAND_NAME) VALUES ('sensor-2','Carrol Ltd');
INSERT INTO BRANDS(SENSOR, BRAND_NAME) VALUES ('sensor-3','Hirthe Inc');
INSERT INTO BRANDS(SENSOR, BRAND_NAME) VALUES ('sensor-4','Walker LLC');
```
Lalu saya mengambil semua yang ada di table brands

![Screenshot from 2023-11-21 09-51-52](https://github.com/mfahryan/Learning-Kafka/assets/112185850/672617e9-8549-41a3-ba4c-89ecaed0b89e)

### 3.5.4 Lalu saya membuat di variable readings nya berupa sensor,reading, dan area sensornya

```
INSERT INTO readings(sensor, reading, area) VALUES ('sensor-1',45,'wheel');
INSERT INTO readings(sensor, reading, area) VALUES ('sensor-2',41,'motor');
INSERT INTO readings(sensor, reading, area) VALUES ('sensor-1',92,'wheel');
INSERT INTO readings(sensor, reading, area) VALUES ('sensor-2',13,'engine');
```

lalu saya mengambil semua yang ada di table readings

![Screenshot from 2023-11-21 09-52-05](https://github.com/mfahryan/Learning-Kafka/assets/112185850/84445080-6b4c-4070-b63f-abd6fcb3aaa1)

### 3.5.5 Lalu setelah semua berhasil saya menggunakan JOIN untuk brands dan readings

```
CREATE STREAM enriched_readings AS
SELECT r.reading, r.area, b.brand_name
FROM readings r
INNER JOIN brands b 
ON b.sensor = r.sensor
PARTITION BY r.area
EMIT CHANGES;
```

![Screenshot from 2023-11-21 09-51-00](https://github.com/mfahryan/Learning-Kafka/assets/112185850/8cc7be36-2624-4cb6-b514-46fe5dde48eb)

### 3.5.6 Lalu jika saya memasukkan nilai ke dalam readings maka hasilnya dapat di lihat dan dia bakal men-stream berikut dengan tabel nya seperti gambar dibawah

![Screenshot from 2023-11-21 09-50-32](https://github.com/mfahryan/Learning-Kafka/assets/112185850/4d2b9d97-9b23-4d05-9ac3-3d43ffba8ed7)
![Screenshot from 2023-11-21 09-50-34](https://github.com/mfahryan/Learning-Kafka/assets/112185850/0b62caef-7e3b-46d7-b560-093987bf04cc)
![Screenshot from 2023-11-21 09-51-00](https://github.com/mfahryan/Learning-Kafka/assets/112185850/8cc7be36-2624-4cb6-b514-46fe5dde48eb)

![Screenshot from 2023-11-21 09-52-05](https://github.com/mfahryan/Learning-Kafka/assets/112185850/84445080-6b4c-4070-b63f-abd6fcb3aaa1)












