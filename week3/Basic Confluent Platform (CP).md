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


![buattopicc3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/c8464027-65ad-421f-aed3-56b11ca4e5a7)

![Screenshot from 2023-11-19 18-57-33](https://github.com/mfahryan/Learning-Kafka/assets/112185850/77140271-ad4b-47a8-9eb6-4ef11830dca9)

![Screenshot from 2023-11-19 18-57-53](https://github.com/mfahryan/Learning-Kafka/assets/112185850/6960be60-a504-48a6-b085-0eefaa35e4ed)

![Screenshot from 2023-11-19 18-58-04](https://github.com/mfahryan/Learning-Kafka/assets/112185850/7a501e55-94ca-4fa3-a5df-76a75154a977)

![Screenshot from 2023-11-19 18-58-13](https://github.com/mfahryan/Learning-Kafka/assets/112185850/8245bbec-1df2-4b57-bfcb-2edc86f7eba0)

![Screenshot from 2023-11-19 18-58-18](https://github.com/mfahryan/Learning-Kafka/assets/112185850/bccd7174-6941-4ec9-babd-103024f2e688)





![c3](https://github.com/mfahryan/Learning-Kafka/assets/112185850/5ebb6eeb-b568-4eda-bed9-0ebba9ef62f6)

![consumec3purchases](https://github.com/mfahryan/Learning-Kafka/assets/112185850/11278e8a-7533-490f-9069-0d047da86bd6)

![consumeconfluent](https://github.com/mfahryan/Learning-Kafka/assets/112185850/9c47df28-f5fa-48b6-89d1-4f74613defd9)

![topicrest](https://github.com/mfahryan/Learning-Kafka/assets/112185850/b27c8668-7eca-430c-8fe9-1cff5f128c77)










