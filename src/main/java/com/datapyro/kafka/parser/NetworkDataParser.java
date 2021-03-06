package com.datapyro.kafka.parser;

import com.datapyro.kafka.entity.NetworkSignalEntity;
import com.datapyro.kafka.model.NetworkData;
import com.datapyro.kafka.model.NetworkSignal;
import com.datapyro.kafka.util.HashCodeUtil;
import com.datapyro.kafka.validation.NetworkSignalValidator;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class parses nested json and converts it to NetworkSignalEntity objects
 */
public class NetworkDataParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkDataParser.class);

    private Gson gson = new Gson();

    private NetworkSignalValidator validator;

    public NetworkDataParser(NetworkSignalValidator validator) {
        this.validator = validator;
    }

    public List<NetworkSignalEntity> parse(String json) throws Exception {
        // parse incoming json
        NetworkData networkData = gson.fromJson(json, NetworkData.class);

        // enrich, filter and return network signal entities
        if (networkData == null || networkData.getSignals() == null) {
            return new ArrayList<>(0);
        }
        return networkData.getSignals().stream()
                          .map(networkSignal -> convert(networkData.getDeviceId(), networkSignal)) // convert objects
                          .map(entity -> entity.setId(generateUniqueId(entity))) // add unique id
                          .filter(entity -> validator.isValid(entity))
                          .collect(Collectors.toList());
    }

    /**
     * Converts NetworkSignal object to NetworkSignalEntity using deviceId data from NetworkData object
     */
    private NetworkSignalEntity convert(String deviceId, NetworkSignal networkSignal) {
        return new NetworkSignalEntity()
            .setDeviceId(deviceId)
            .setTime(networkSignal.getTime())
            .setLatitude(networkSignal.getLatitude())
            .setLongitude(networkSignal.getLongitude())
            .setNetworkType(networkSignal.getNetworkType())
            .setRxSpeed(networkSignal.getRxSpeed())
            .setTxSpeed(networkSignal.getTxSpeed())
            .setRxData(networkSignal.getRxData())
            .setTxData(networkSignal.getTxData());
    }

    /**
     * Generate an unique hash from given input
     */
    private String generateUniqueId(NetworkSignalEntity entity) {
        try {
            return HashCodeUtil.getHashString(
                    entity.getDeviceId(),
                    entity.getTime(),
                    entity.getNetworkType(),
                    entity.getRxData(),
                    entity.getTxData(),
                    entity.getRxSpeed(),
                    entity.getTxSpeed(),
                    entity.getLatitude(),
                    entity.getLongitude());
        } catch (Exception e) {
            LOGGER.error("Error generating unique id", e);
            return null;
        }
    }

}
