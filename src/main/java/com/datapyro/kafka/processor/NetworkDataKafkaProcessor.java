package com.datapyro.kafka.processor;

import com.datapyro.kafka.ignite.NetworkSignalIgniteRepository;
import com.datapyro.kafka.parser.NetworkDataParser;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Kafka Processor processes incoming json data and saves processed data into Ignite
 */
public class NetworkDataKafkaProcessor implements Processor<byte[], byte[]> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessorContext context;

    private NetworkSignalIgniteRepository networkSignalRepository;

    private NetworkDataParser parser;

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        this.context.schedule(1000);

        networkSignalRepository = new NetworkSignalIgniteRepository();

        parser = new NetworkDataParser();
    }

    @Override
    public void process(byte[] key, byte[] value) {
        String json = new String(value);
        try {
            parser.parse(json)
                  .forEach(networkSignalRepository::save);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("Error processing request, data: " + json, e);
            } else {
                logger.error("Error processing request", e);
            }
        }

    }

    @Override
    public void punctuate(long timestamp) {
        context.commit();
    }

    @Override
    public void close() {
        networkSignalRepository.close();
    }
    
}
