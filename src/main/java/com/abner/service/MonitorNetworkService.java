package com.abner.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.annotation.Timing;
import com.abner.manage.StatusManage;
import com.abner.enums.TimingType;
/**
 * 网络状态监控
 * @author wei.li
 * @time 2017年6月19日下午3:06:01
 */
@Service
public class MonitorNetworkService{
	
	private static  Logger logger = LoggerFactory.getLogger(MonitorNetworkService.class);
	
	@Singleton
	@Timing(initialDelay = 500, period = 500, type = TimingType.FIXED_DELAY, unit = TimeUnit.MILLISECONDS)
	public void monitorNetwork() {
		StatusManage.ping = checkNetwork();
	}
	
	private int checkNetwork() {
		Process p=null;
		try {
			p= Runtime.getRuntime().exec("ping www.baidu.com -n 1");
			InputStream is = p.getInputStream(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(is));  
			StringBuffer sbf = new StringBuffer();  
			String tmp = "";  
			while((tmp = br.readLine())!=null){  
			    sbf.append(tmp);  
			}
			br.close();
			is.close();
			if(sbf.toString().indexOf("TTL")==-1){
				return 0;
			}
			String ping = sbf.substring(sbf.lastIndexOf("=")+1).trim();
			return Integer.parseInt(ping.substring(0, ping.indexOf("ms")));
		} catch (Exception e) {
			logger.error("网络监控出现异常",e);
			return 0;
		}finally {
			if(p!=null){
				p.destroy();
			}
			
		}
	}


	
}
