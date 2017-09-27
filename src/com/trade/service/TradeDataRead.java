package com.trade.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.core.spi.store.Store.Iterator;

import com.trade.utils.BufferedRandomAccessFile;

/**
 * 从文件读入数据到缓存
 * @author xieqiong
 * @time 2017/9/25
 */
public class TradeDataRead {
	
	/**
	 * 从
	 * @param file
	 * @param cache
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void readFromDisk(File file,Cache<String, Long> cache) {
		
		//BufferedRandomAccessFile bufferedRandomAccessFile = null;
		RandomAccessFile bufferedRandomAccessFile=null;
		try {
			Long lastPos = cache.get("position");
			if(lastPos==null){
				lastPos=(long) 0;
			}
			System.out.println(cache.containsKey("position"));;
			System.out.println("pos:"+lastPos);
			bufferedRandomAccessFile = new RandomAccessFile (file,"rw");
			bufferedRandomAccessFile.seek(lastPos);
			String line=bufferedRandomAccessFile.readLine();
			while((line=bufferedRandomAccessFile.readLine())!=null)	{
				Long position=bufferedRandomAccessFile.getFilePointer();
				cache.put("position", position);
				
				String[] attrList = line.split(",");
				String participantId = attrList[6];
				String clientId = attrList[7];
				String instrumentId = attrList[10];
				//买卖方向
				String direction =attrList[5];
				//买卖手数
				Long volume = Long.valueOf(attrList[14]);
				if(direction.equals("0")){
					volume = (-1)*volume;
				}
				String key = participantId+","+clientId+","+instrumentId;
				Long last = cache.get(key);
				if(last!=null){
					cache.put(key, last+volume);
				}else{
					cache.put(key, volume);
				}
				
			}
			bufferedRandomAccessFile.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			
			try {
				bufferedRandomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeToDisk(File file,Cache<String, Long> cache){
		RandomAccessFile bufferedRandomAccessFile=null;
		try {
			bufferedRandomAccessFile = new RandomAccessFile (file,"rw");
			
			java.util.Iterator<Entry<String, Long>> it= cache.iterator();
			while(it.hasNext()){
				Entry<String, Long> entity=it.next();
				
				//System.out.println(entity.getValue() +":"+entity.getKey());
				String line=entity.getKey()+","+String.valueOf(entity.getValue())+"\n";
				bufferedRandomAccessFile.write(line.getBytes());
				
			}
			bufferedRandomAccessFile.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			
			try {
				bufferedRandomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		

}
