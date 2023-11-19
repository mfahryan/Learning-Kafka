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


