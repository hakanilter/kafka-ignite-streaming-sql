package com.datapyro.kafka.entity;

import com.datapyro.kafka.util.JsonSerializable;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class NetworkSignalEntity extends JsonSerializable {

    private static final long serialVersionUID = -6650766189688673925L;

    @QuerySqlField
    private String id;

    @QuerySqlField(index = true)
    private String deviceId;

    @QuerySqlField(index = true)
    private Long time;

    @QuerySqlField(index = true)
    private String networkType;

    @QuerySqlField
    private Double rxSpeed;

    @QuerySqlField
    private Double txSpeed;

    @QuerySqlField
    private Long rxData;

    @QuerySqlField
    private Long txData;

    @QuerySqlField
    private Double latitude;

    @QuerySqlField
    private Double longitude;

    public String getId() {
        return id;
    }

    public NetworkSignalEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public NetworkSignalEntity setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public NetworkSignalEntity setTime(Long time) {
        this.time = time;
        return this;
    }

    public String getNetworkType() {
        return networkType;
    }

    public NetworkSignalEntity setNetworkType(String networkType) {
        this.networkType = networkType;
        return this;
    }

    public Double getRxSpeed() {
        return rxSpeed;
    }

    public NetworkSignalEntity setRxSpeed(Double rxSpeed) {
        this.rxSpeed = rxSpeed;
        return this;
    }

    public Double getTxSpeed() {
        return txSpeed;
    }

    public NetworkSignalEntity setTxSpeed(Double txSpeed) {
        this.txSpeed = txSpeed;
        return this;
    }

    public Long getRxData() {
        return rxData;
    }

    public NetworkSignalEntity setRxData(Long rxData) {
        this.rxData = rxData;
        return this;
    }

    public Long getTxData() {
        return txData;
    }

    public NetworkSignalEntity setTxData(Long txData) {
        this.txData = txData;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public NetworkSignalEntity setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public NetworkSignalEntity setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
    
}
