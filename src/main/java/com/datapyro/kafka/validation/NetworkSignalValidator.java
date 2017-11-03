package com.datapyro.kafka.validation;

import com.datapyro.kafka.entity.NetworkSignalEntity;

public class NetworkSignalValidator {

    public boolean isValid(NetworkSignalEntity entity) {
        return
            entity.getId() != null &&
            entity.getDeviceId() != null &&
            entity.getTime() != null &&
            entity.getRxSpeed() != null &&
            entity.getTxSpeed() != null &&
            entity.getRxData() != null &&
            entity.getTxData() != null &&
            entity.getRxSpeed() > 0 &&
            entity.getTxSpeed() > 0 &&
            entity.getRxData() > 0 &&
            entity.getTxData() > 0;
    }

}
