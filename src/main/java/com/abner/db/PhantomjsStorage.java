package com.abner.db;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.abner.pojo.PhantomjsProcesses;
import com.google.common.collect.Lists;
/**
 * Phantomjs进程仓库
 * @author wei.li
 * @time 2017年11月23日下午12:49:17
 */
public class PhantomjsStorage {
	
	private static  Logger logger = LoggerFactory.getLogger(PhantomjsStorage.class);
	//phantomjs线程集合
	public static List<PhantomjsProcesses> phantomjsProcessess=Lists.newArrayList();
	
	public static void add(Process p){
		phantomjsProcessess.add(new PhantomjsProcesses(p));
	}
	
	public static void clean(){
		for(int i=0;i<phantomjsProcessess.size();i++){
			PhantomjsProcesses p = phantomjsProcessess.get(i);
			long time =p.getTime();
			if(p.isAlive()&&time>60*1000){
				logger.error("phantomjs进程懵逼, 懵逼时长:{}ms",time);
				p.destroy();
				logger.info("成功清理假死phantomjs进程");
			}
			if(!p.isAlive()){
				phantomjsProcessess.remove(i);
				i--;
			}
			
		}
	}
}
