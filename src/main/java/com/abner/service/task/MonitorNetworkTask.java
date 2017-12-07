package com.abner.service.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.manage.StatusManage;
import com.abner.enums.TaskName;
import com.abner.utils.MyThreadPool;
/**
 * 网络状态监控
 * @author wei.li
 * @time 2017年6月19日下午3:06:01
 */
@Service(TaskName.MONITORNETWORK)
public class MonitorNetworkTask implements Task{
	
	private static  Logger logger=Logger.getLogger(MonitorNetworkTask.class);
	
	
	@Override
	@Singleton
	public void execute() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if(isBrokenNetwork()){
					if(StatusManage.isNetwork){
						logger.error("网络连接已断开。。。");
					}
					StatusManage.isNetwork=false;
				}else{
					if(!StatusManage.isNetwork){
						logger.info("网络已恢复");
					}
					StatusManage.isNetwork=true;
				}
			}
		};
		MyThreadPool.scheduleWithFixedDelay(runnable, 500, 500, TimeUnit.MILLISECONDS);

	}
	
	private boolean isBrokenNetwork() {
		Process p=null;
		try {
			p= Runtime.getRuntime().exec("ping www.baidu.com");
			InputStream is = p.getInputStream(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(is));  
			StringBuffer sbf = new StringBuffer();  
			String tmp = "";  
			while((tmp = br.readLine())!=null){  
			    sbf.append(tmp);  
			}
			br.close();
			is.close();
			return sbf.toString().indexOf("TTL")==-1;
		} catch (IOException e) {
			logger.error("网络监控出现异常",e);
			return true;
		}finally {
			if(p!=null){
				p.destroy();
			}
			
		}
	}

	@Override
	public void stop() {
		
	}


	
}
