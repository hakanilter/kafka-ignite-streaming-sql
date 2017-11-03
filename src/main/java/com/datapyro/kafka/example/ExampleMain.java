package com.datapyro.kafka.example;

import java.util.Arrays;
import java.util.List;

/**
 * Kafka Ignite Streamin SQL Example
 *
 * This class runs RandomNetworkDataProducer, NetworkDataProcessor and IgniteStreamingSQLQuery jobs in parallel
 */
public class ExampleMain {

    public static void main(String[] args) {
        List<Runnable> jobs = Arrays.asList(
                new RandomNetworkDataProducer(),
                new NetworkDataProcessor(),
                new IgniteStreamingSQLQuery());
        jobs.parallelStream()
            .map(Thread::new)
            .forEach(Thread::run);
    }

}
