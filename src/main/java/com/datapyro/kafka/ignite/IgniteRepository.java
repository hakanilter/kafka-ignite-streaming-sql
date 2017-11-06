package com.datapyro.kafka.ignite;

import com.datapyro.kafka.util.ConfigUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;

class IgniteRepository {

    private Ignite ignite;

    IgniteRepository() {
        
    }

    void init() {
        ignite = Ignition.start(ConfigUtil.getResource("ignite.xml"));
    }

    IgniteCache getCache(CacheConfiguration config) {
        return ignite.getOrCreateCache(config);
    }

    void close() {
        if (ignite != null) {
            ignite.close();
        }
    }

}
