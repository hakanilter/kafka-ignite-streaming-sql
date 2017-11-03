package com.datapyro.kafka.example;

import com.datapyro.kafka.processor.NetworkDataKafkaProcessor;
import com.datapyro.kafka.util.ConfigUtil;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * This class starts a Kafka processor in order to process incoming json data
 */
public class NetworkDataProcessor implements Runnable {

    private static Processor<byte[], byte[]> getProcessor() {
        return new NetworkDataKafkaProcessor();
    }

    public static final Logger logger = LoggerFactory.getLogger(NetworkDataProcessor.class);

    @Override
    public void run() {
        logger.info("Initializing kafka processor...");
        Properties properties = ConfigUtil.getConfig("network-data");

        String topics = properties.getProperty("topic.names");
        StreamsConfig config = new StreamsConfig(properties);

        logger.info("Start listening topics: " + topics);

        TopologyBuilder builder = new TopologyBuilder().addSource("SOURCE", topics.split(","))
                                                       .addProcessor("PROCESSOR", NetworkDataProcessor::getProcessor, "SOURCE");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }

    public static void main(String[] args) throws Exception {
        new NetworkDataProcessor().run();
    }

}
