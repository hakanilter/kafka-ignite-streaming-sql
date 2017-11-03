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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkSignalIgniteRepository {

    private static final String CACHE = "NetworkSignal";
    private static final long WINDOW = 15;
    private final Duration duration = new Duration(TimeUnit.MINUTES, WINDOW); // 15 minutes window

    private Logger logger = LoggerFactory.getLogger(getClass());

    private IgniteRepository igniteRepository;
    private IgniteCache<String, NetworkSignalEntity> cache;

    public NetworkSignalIgniteRepository() {
        this.igniteRepository = IgniteRepositoryFactory.getInstance();

        CacheConfiguration<String, NetworkSignal> cacheConfig = new CacheConfiguration<>(CACHE);
        cacheConfig.setCacheMode(CacheMode.REPLICATED);
        cacheConfig.setIndexedTypes(String.class, NetworkSignalEntity.class);
        this.cache = igniteRepository.getCache(cacheConfig)
                                     .withExpiryPolicy(new CreatedExpiryPolicy(duration));
    }

    public void save(NetworkSignalEntity entity) {
        cache.put(entity.getId(), entity);
        logger.info("Saved in ignite: " + entity);
    }

    public List<List<?>> sqlQuery(String query) {
        SqlFieldsQuery sql = new SqlFieldsQuery(query);
        List<List<?>> rows = new ArrayList<>();
        try (QueryCursor<List<?>> cursor = cache.query(sql)) {
            for (List<?> row : cursor) {
                rows.add(row);
            }
        }
        return rows;
    }

    public void close() {
        igniteRepository.close();
    }

}
