# Kafka Ignite Streaming SQL
An example project for integrating Kafka and Ignite in order to run streaming sql queries. 

Under the example package, **RandomNetworkDataProducer** class initializes a Kafka producer and starts producing random network data.
 
**NetworkDataProcessor** is a Kafka processor that consumes the data to transform and feed an Ignite cache. 

Finally, **IgniteStreamingSQLQuery** class 
executes multiple SQL queries on the data. 

You can run the whole example with just running the main method on **ExampleMain** class, or you can run each class individually.

## Preparation

Before running the example you may want to prepare Apache Kafka installation and configurations like creating topics etc. Please follow the 
instructions below:

**Download and extract Apache Kafka**
```
wget http://mirror.vorboss.net/apache/kafka/1.0.0/kafka_2.11-1.0.0.tgz
tar xvf kafka_2.11-1.0.0.tgz
cd kafka_2.11-1.0.0  
```

**Start Zookeeper**
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

**Start Kafka**
```
bin/kafka-server-start.sh config/server.properties
```

**Create the topic** 
```
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic network-data --partitions 4 --replication-factor 1
```

## Test Data

Here is some example of randomly generated json data: 
``` 
{
    "deviceId": "9e79c134-b44c-4bd2-bc0c-4ee0541838d8",
    "time": 1509712401465,
    "signals": [
        {"time":1509712401465,"networkType":"wifi","rxSpeed":51.0,"txSpeed":11.0,"rxData":279,"txData":186},
        {"time":1509712401465,"networkType":"wifi","rxSpeed":46.0,"txSpeed":87.0,"rxData":434,"txData":561},
        {"time":1509712401465,"networkType":"wifi","rxSpeed":94.0,"txSpeed":70.0,"rxData":828,"txData":338}
    ]
}
``` 

## Queries

The example runs 3 different aggregation queries:

- Top 5 most data consuming devices

    ```SELECT deviceId, SUM(rxData) AS rxTotal, SUM(txData) AS txTOTAL FROM NetworkSignalEntity " + "GROUP BY deviceId ORDER BY rxTotal DESC, txTotal DESC LIMIT 5```
    
    Example output:
    
    ```
    [bd639c9f-363b-4b2e-a3f2-a3109153981d, 13541, 10992]
    [29605015-2fa6-46cb-9f3b-f45c5a8453dd, 12871, 11605]
    [48e44f41-6c2c-422a-b69f-3c03f701793f, 12188, 11832]
    [a3dd65b4-0b90-4241-87fb-0c7e255adc61, 10542, 9704]
    [5dfc2df9-5103-4c0e-b41e-dd23fd9dcd07, 10460, 10010]
    ``` 
- Total consumed download and upload data by network types

    ```SELECT networkType, SUM(rxData) AS rxTotal, SUM(txData) AS txTotal FROM NetworkSignalEntity GROUP BY networkType```

    Example output:
    
    ```
    [mobile, 252189, 262684]
    [wifi, 272411, 275334]
    ```

- Average download and upload speeds by network types

    ```SELECT networkType, AVG(rxSpeed) AS avgRxSpeed, AVG(txSpeed) AS avgTxSpeed FROM NetworkSignalEntity GROUP BY networkType```    
    
    Example output:
    
    ```
    [mobile, 50.34661354581673, 49.35657370517928]
    [wifi, 52.37931034482759, 48.76225045372051]
    ```   
    
