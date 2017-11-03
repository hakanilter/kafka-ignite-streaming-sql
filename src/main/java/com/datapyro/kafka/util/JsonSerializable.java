package com.datapyro.kafka.util;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class JsonSerializable implements Serializable {

    private static final Gson gson = new Gson();

    @Override
    public String toString() {
        return gson.toJson(this);
    }

}
