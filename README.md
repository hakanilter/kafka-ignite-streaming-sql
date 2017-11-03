# Kafka Ignite Streaming SQL
An example project for integrating Kafka and Ignite in order to run streaming sql queries. 

Under the example package, RandomNetworkDataProducer class initializes a Kafka producer and starts producing random network data.
 
NetworkDataProcessor is a Kafka processor that consumes the data to transform and feed an Ignite cache. 

Finally, IgniteStreamingSQLQuery class 
executes multiple SQL queries on the data. 

You can run the whole example with just running the main method on ExampleMain class, or you can run each class individually.

## Preparation
Before running the example you may want to prepare Apache Kafka installation and configurations like creating topics etc. Please follow the 
instructions below:

Download and extract Apache Kafka
```
wget http://mirror.vorboss.net/apache/kafka/1.0.0/kafka_2.11-1.0.0.tgz
tar xvf kafka_2.11-1.0.0.tgz
cd kafka_2.11-1.0.0  
```

Start Zookeeper
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka
```
bin/kafka-server-start.sh config/server.properties
```

Create the topic 
```
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic network-data --partitions 4 --replication-factor 1
```

