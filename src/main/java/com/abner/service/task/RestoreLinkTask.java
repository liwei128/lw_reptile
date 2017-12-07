package com.abner.service.task;

import java.util.List;

import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.db.UrlStorage;
import com.abner.enums.TaskName;
import com.abner.pojo.MyUrl;
/**
 * 启动时,修复残留链接（上次在访问中，但是未完成）
 * @author wei.li
 * @time 2017年11月23日下午9:01:12
 */
@Service(TaskName.RESTORELINK)
public class RestoreLinkTask implements Task{

	@Override
	@Singleton
	public void execute() {
		restoreLink(UrlStorage.getImgs());
		restoreLink(UrlStorage.getUrls());
	}

	@Override
	public void stop() {
		
	}
	
	private void restoreLink(List<MyUrl> urls){
		for(MyUrl url : urls){
			if(!url.isAlreadyLoad()){
				url.setStartTime(0);
			}
		}
	}

}
