# SSL(Secure Socket Layer)

## SSL Handshake(OneWay)

![ssloneway drawio](https://github.com/mfahryan/Learning-Kafka/assets/112185850/67d5b067-8323-4bf2-8f5a-c0c1aeb81e14)

untuk SSL Handshake (One way) client mau connect ke server, jadi pada gambar tersebut server nyediain id lalu client akan check ID tersebut dari server dan nge autentikasi bahwa itu ID server tersebut.

### SSL Handshake (TwoWay)

![ssltwoway drawio](https://github.com/mfahryan/Learning-Kafka/assets/112185850/c696ce0a-2097-4ef0-8990-cff38f943a7f)

untuk SSL handshake (Twoway) sama juga seperti oneway tetapi perbedaanya ialah tidak hanya client yang nyediain ID nya tetapi clientnya juga dan server akan mengautentikasi id nya juga, ini disebut SSL Handshake (Twoway)

nah, untuk apache kafka sendiri kita menggunakan Twoway untuk SSL nya sendiri.

steps untuk SSL security ada beberapa langkah,yaitu 
1. fix the certificate authority(CA)
2. create Truststore
3. create KeyStore
4. Create Certificate signing request
5. sign the certificate using CA
6. import the Signed certificate and CA into KeyStore

Untuk Pembuatan Ca-Cert dan Ca-Key kita menggunakan command : 

`openssl req -new -x509 -keyout ca-key -out ca-cert -days 365 -addext subjectAltName=dns:(hostname)`

sesudah membuat CA-cert dan CA-KEY

Untuk Zookeeper

```
1.	keytool -keystore kafka.zookeeper.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.zookeeper.keystore.jks -alias zookeeper -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.zookeeper.keystore.jks -alias zookeeper -certreq -file ca-request-zookeeper
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-zookeeper -out ca-signed-zookeeper -days 365 -CAcreateserial
5.	keytool -keystore kafka.zookeeper.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.zookeeper.keystore.jks -alias zookeeper -import -file ca-signed-zookeeper
```


Untuk Zookeeper.client

```
1.	keytool -keystore kafka.zookeeper-client.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.zookeeper-client.keystore.jks -alias zookeeper-client -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.zookeeper-client.keystore.jks -alias zookeeper-client -certreq -file ca-request-zookeeper-client 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-zookeeper-client -out ca-signed-zookeeper-client -days 365 -CAcreateserial 
5.	keytool -keystore kafka.zookeeper-client.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.zookeeper-client.keystore.jks -alias zookeeper-client -import -file ca-signed-zookeeper-client
```


Untuk Kafka.Broker

```
1.	keytool -keystore kafka.broker.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.broker.keystore.jks -alias broker -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.broker.keystore.jks -alias broker -certreq -file ca-request-broker 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-broker -out ca-signed-broker -days 365 -CAcreateserial 
5.	keytool -keystore kafka.broker.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.broker.keystore.jks -alias broker -import -file ca-signed-broker
```


Untuk Producer

```
1.	keytool -keystore kafka.producer.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.producer.keystore.jks -alias producer -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.producer.keystore.jks -alias producer -certreq -file ca-request-producer 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-producer -out ca-signed-producer -days 365 -CAcreateserial 
5.	keytool -keystore kafka.producer.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.producer.keystore.jks -alias producer -import -file ca-signed-producer
```


Untuk Consumer

```
1.	keytool -keystore kafka.consumer.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.consumer.keystore.jks -alias consumer -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.consumer.keystore.jks -alias consumer -certreq -file ca-request-consumer 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-consumer -out ca-signed-consumer -days 365 -CAcreateserial 
5.	keytool -keystore kafka.consumer.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.consumer.keystore.jks -alias consumer -import -file ca-signed-consumer
```


Untuk Schema-Registry
1.	keytool -keystore kafka.schema-registry.truststore.jks -alias ca-cert -import -file ../ca-cert 
2.	keytool -keystore kafka.schema-registry.keystore.jks -alias schema-registry -validity 365 -genkey -keyalg RSA -ext SAN=dns:br1kafka.dev.alldataint.com
3.	keytool -keystore kafka.schema-registry.keystore.jks -alias schema-registry -certreq -file ca-request-schema-registry 
4.	openssl x509 -req -CA ../ca-cert -CAkey ../ca-key -in ca-request-schema-registry -out ca-signed-schema-registry -days 365 -CAcreateserial 
5.	keytool -keystore kafka.schema-registry.keystore.jks -alias ca-cert -import -file ../ca-cert 
6.	keytool -keystore kafka.schema-registry.keystore.jks -alias schema-registry -import -file ca-signed-schema-registry

Untuk Kafka Connect

```
1.	keytool -keystore kafka.kafka-connect.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.kafka-connect.keystore.jks -alias kafka-connect -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.kafka-connect.keystore.jks -alias kafka-connect -certreq -file ca-request-kafka-connect 
4.	openssl x509 -req -CA ../ca-cert -CAkey ../ca-key -in ca-request-kafka-connect -out ca-signed-kafka-connect -days 365 -CAcreateserial 
5.	keytool -keystore kafka.kafka-connect.keystore.jks -alias ca-cert -import -file ../ca-cert 
6.	keytool -keystore kafka.kafka-connect.keystore.jks -alias kafka-connect -import -file ca-signed-kafka-connect
```


Untuk Ksql Server

```
1.	keytool -keystore kafka.ksql-server.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.ksql-server.keystore.jks -alias ksql-server -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.ksql-server.keystore.jks -alias ksql-server -certreq -file ca-request-ksql-server 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-ksql-server -out ca-signed-ksql-server -days 365 -CAcreateserial 
5.	keytool -keystore kafka.ksql-server.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.ksql-server.keystore.jks -alias ksql-server -import -file ca-signed-ksql-server
```


Untuk Control Center

```
1.	keytool -keystore kafka.control-center.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.control-center.keystore.jks -alias control-center -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname)
3.	keytool -keystore kafka.control-center.keystore.jks -alias control-center -certreq -file ca-request-control-center 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-control-center -out ca-signed-control-center -days 365 -CAcreateserial 
5.	keytool -keystore kafka.control-center.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.control-center.keystore.jks -alias control-center -import -file ca-signed-control-center
```


Untuk Kafka Client

```
1.	keytool -keystore kafka.kafka-client.truststore.jks -alias ca-cert -import -file ca-cert 
2.	keytool -keystore kafka.kafka-client.keystore.jks -alias kafka-client -validity 365 -genkey -keyalg RSA -ext SAN=dns:(hostname) 
3.	keytool -keystore kafka.kafka-client.keystore.jks -alias kafka-client -certreq -file ca-request-kafka-client 
4.	openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-kafka-client -out ca-signed-kafka-client -days 365 -CAcreateserial 
5.	keytool -keystore kafka.kafka-client.keystore.jks -alias ca-cert -import -file ca-cert 
6.	keytool -keystore kafka.kafka-client.keystore.jks -alias kafka-client -import -file ca-signed-kafka-client
```
