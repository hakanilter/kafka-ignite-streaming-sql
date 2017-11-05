#!/bin/bash

cd ..
mvn clean package -Pmake-jar
java -Xms1g -Xmx1g -cp target/kafka-ignite-streaming-sql-*-dist.jar com.datapyro.kafka.example.ExampleMain