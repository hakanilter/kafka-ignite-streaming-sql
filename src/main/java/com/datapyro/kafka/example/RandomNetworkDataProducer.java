package com.datapyro.kafka.example;

import com.datapyro.kafka.model.NetworkData;
import com.datapyro.kafka.model.NetworkSignal;
import com.datapyro.kafka.util.ConfigUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class initializes a Kafka Producer and produces random network data
 */
public class RandomNetworkDataProducer implements Runnable {

    private static final long INCOMING_DATA_INTERVAL = 500;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        logger.info("Initializing kafka producer...");

        Properties properties = ConfigUtil.getConfig("network-data");
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", StringSerializer.class);
        properties.put("acks", "all");
        properties.put("retries", 0);

        String topic = properties.getProperty("topic.names");
        logger.info("Start producing random network data to topic: " + topic);

        Producer<String, String> producer = new KafkaProducer<>(properties);

        Random random = new Random();

        final int deviceCount = 100;
        List<String> deviceIds = new ArrayList<>();
        for (int i = 0; i < deviceCount; i++) {
            deviceIds.add(UUID.randomUUID().toString());
        }

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            NetworkData networkData = new NetworkData();

            networkData.setDeviceId(deviceIds.get(random.nextInt(deviceCount-1)));
            networkData.setSignals(new ArrayList<>());
            for (int j = 0; j < random.nextInt(4)+1; j++) {
                NetworkSignal networkSignal = new NetworkSignal();
                networkSignal.setNetworkType(i % 2 == 0 ? "mobile" : "wifi");
                networkSignal.setRxData((long) random.nextInt(1000));
                networkSignal.setTxData((long) random.nextInt(1000));
                networkSignal.setRxSpeed((double) random.nextInt(100));
                networkSignal.setTxSpeed((double) random.nextInt(100));
                networkSignal.setTime(System.currentTimeMillis());
                networkData.getSignals().add(networkSignal);
            }

            String key = "key-" + System.currentTimeMillis();
            String value = networkData.toString();

            if (logger.isDebugEnabled()) {
                logger.debug("Random data generated: " + key + ", " + value);
            }

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record);

            try {
                Thread.sleep(INCOMING_DATA_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.close();
    }

    public static void main(String[] args) throws Exception {
        new RandomNetworkDataProducer().run();
    }

}
