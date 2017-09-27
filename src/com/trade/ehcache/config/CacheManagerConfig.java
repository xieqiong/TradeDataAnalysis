package com.trade.ehcache.config;

import java.io.File;

import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.config.executor.PooledExecutionServiceConfiguration;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

/**
 * ehcache的的配置类
 * @author xieqiong
 * @time 2017/9/25
 */
public class CacheManagerConfig {

	/**
	 * 获取cacheManager的初始配置
	 * @param cacheName cache的名字
	 * @return
	 */
	public CacheManager getCacheManagerConfig(String cacheName){
		
		CacheManager cacheManager
        = CacheManagerBuilder.newCacheManagerBuilder()
        .using(PooledExecutionServiceConfigurationBuilder.newPooledExecutionServiceConfigurationBuilder() 
        		.defaultPool("dflt", 0, 10)
        		.pool("defaultDiskPool", 1, 3)
            .build())
        .with(new CacheManagerPersistenceConfiguration(new File( "myData")))
        .withDefaultDiskStoreThreadPool("defaultDiskPool") 
        .withCache("cache1",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Long.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(1000000, EntryUnit.ENTRIES)
                    .disk(400L, MemoryUnit.MB)))
        .build(true);
		return cacheManager;
		
	}
}
