package com.datapyro.kafka.ignite;

import com.datapyro.kafka.entity.NetworkSignalEntity;
import com.datapyro.kafka.model.NetworkSignal;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkSignalIgniteRepository {

    private static final String CACHE = "NetworkSignal";
    
    private static final Duration DURATION = new Duration(TimeUnit.MINUTES, 15); // 15 minutes window

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkSignalIgniteRepository.class);

    private IgniteRepository igniteRepository;

    private IgniteCache<String, NetworkSignalEntity> cache;

    public NetworkSignalIgniteRepository() {
        this.igniteRepository = IgniteRepositoryFactory.getInstance();

        CacheConfiguration<String, NetworkSignal> cacheConfig = new CacheConfiguration<>(CACHE);
        cacheConfig.setCacheMode(CacheMode.REPLICATED);
        cacheConfig.setIndexedTypes(String.class, NetworkSignalEntity.class);
        this.cache = igniteRepository.getCache(cacheConfig)
                                     .withExpiryPolicy(new CreatedExpiryPolicy(DURATION));
    }

    public void save(NetworkSignalEntity entity) {
        cache.put(entity.getId(), entity);
        LOGGER.info("Saved in ignite: " + entity);
    }

    public List<List<?>> sqlQuery(String query) {
        SqlFieldsQuery sql = new SqlFieldsQuery(query);
        try (QueryCursor<List<?>> cursor = cache.query(sql)) {
            return cursor.getAll();
        }
    }

    public void close() {
        igniteRepository.close();
    }

}
