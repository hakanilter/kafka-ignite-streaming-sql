package com.datapyro.kafka.util;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class JsonSerializable implements Serializable {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
