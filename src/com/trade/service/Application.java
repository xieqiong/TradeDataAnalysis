package com.trade.service;

import java.io.File;

import org.ehcache.Cache;
import org.ehcache.CacheManager;

import com.trade.ehcache.config.CacheManagerConfig;

public class Application {

	public static void main(String[] args) {
		
		TradeDataRead tradeDataRead = new TradeDataRead();
		
		File file = new File("d:\\test.csv");
		System.out.println("start:"+System.currentTimeMillis());
		long start = System.currentTimeMillis();
		
		CacheManagerConfig cacheManagerConfig=new CacheManagerConfig();
		CacheManager cacheManager= cacheManagerConfig.getCacheManagerConfig("case1");
		Cache<String, Long> preConfigured = cacheManager.getCache( "cache1", String.class, Long.class); 
		tradeDataRead.readFromDisk(file, preConfigured);
		
		System.out.println("end"+System.currentTimeMillis());
		System.out.println(System.currentTimeMillis()-start);
		
		File file1 = new File("d:\\3.csv");
		tradeDataRead.writeToDisk( file1,preConfigured);
		System.out.println("end"+System.currentTimeMillis());
	}

}
