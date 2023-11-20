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

![c3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/5ebb6eeb-b568-4eda-bed9-0ebba9ef62f6)

![buattopicc3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/c8464027-65ad-421f-aed3-56b11ca4e5a7)

![Screenshot from 2023-11-19 18-57-33](https://github.com/mfahryan/Learning-Kafka/assets/112185850/77140271-ad4b-47a8-9eb6-4ef11830dca9)

![Screenshot from 2023-11-19 18-57-53](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6960be60-a504-48a6-b085-0eefaa35e4ed)

![Screenshot from 2023-11-19 18-58-04](https://github.com/mfahryan/Learning-Kafka/assets/112185850/7a501e55-94ca-4fa3-a5df-76a75154a977)

![Screenshot from 2023-11-19 18-58-13](https://github.com/mfahryan/Learning-Kafka/assets/112185850/8245bbec-1df2-4b57-bfcb-2edc86f7eba0)

delete : ngapusin pesan di partisi dalam topik kafka itu rentang nya tergantung retention time mau berapa hari

kalo compact : compact itu misal key unique nya disimpan terus kalo ada key yg sama maka key yg pertama masuk itu di apus.compact itu dia cuman pertahanin last message gtu untuk

setiap key terus yg message2 sebelumnya itu diapus

pertanyaan : Kalau kita set compact dan key nya null apa yang terjadi?

jawaban : jadi kalo key nya null itu berarti semua pesan itu punya key jadi tidak ada penghapusan dan tetep ada, jika key nya null semua pesan itu dianggap key nya sama

delete+compact  = kita bisa ngatur ni penghapusan sama rentang waktu yg sesuai sama kebutuhan


![Screenshot from 2023-11-19 18-58-18](https://github.com/mfahryan/Learning-Kafka/assets/112185850/bccd7174-6941-4ec9-babd-103024f2e688)

![Screenshot from 2023-11-19 19-00-45](https://github.com/mfahryan/Learning-Kafka/assets/112185850/a4c44ea3-8a1b-40d7-8ec6-5f5ce53e0936)

![Screenshot from 2023-11-19 19-00-52](https://github.com/mfahryan/Learning-Kafka/assets/112185850/92d6de71-115c-4e8e-bd27-32e5e0796728)

![Screenshot from 2023-11-19 19-01-31](https://github.com/mfahryan/Learning-Kafka/assets/112185850/7f5f4ebb-a976-4768-a851-ef8c02242336)

curl -X POST \
     -H "Content-Type: application/vnd.kafka.json.v2+json" \
     -H "Accept: application/vnd.kafka.v2+json" \
     --data '{"records":[{"key":"jsmith","value":"alarm clocks"},{"key":"htanaka","value":"batteries"},{"key":"awalther","value":"bookshelves"}]}' \
     "http://br1kafka.dev.alldataint.com:8082/topics/purchases"

![topicrest](https://github.com/mfahryan/Learning-Kafka/assets/112185850/b27c8668-7eca-430c-8fe9-1cff5f128c77)

![consumec3purchases](https://github.com/mfahryan/Learning-Kafka/assets/112185850/11278e8a-7533-490f-9069-0d047da86bd6)

#buat topic menggunakan rest

curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" -H "Accept: application/vnd.kafka.v2+json"\
--data '{"partitions": 3}' \
  http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
  
#produce meesage ke topic latihanpartisi tanpa key

for i in {1..10}
do
   curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" \
   --data '{"records":[{"value":{"message":"Message '$i'"}}]}' \
   http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
done

#produce meesage ke topic latihanpartisi key null
for i in {1..10}
do
   curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" \
   --data '{"records":[{"key":"null","value":{"message":"Message '$i'"}}]}' \
   http://br1kafka.dev.alldataint.com:8082/topics/latihanpartisi
done












