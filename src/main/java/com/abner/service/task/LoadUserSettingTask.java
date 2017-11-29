package com.abner.service.task;

import com.abner.utils.JsonUtil;
import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.manage.FilePathManage;
import com.abner.enums.TaskName;
import com.abner.pojo.UserSetting;
import com.abner.utils.CommonUtil;
/**
 * 读取用户配置文件
 * @author wei.li
 * @time 2017年11月23日下午1:25:33
 */
@Service(name = TaskName.LOADUSERSETTING)
public class LoadUserSettingTask implements Task{
	
	private static  Logger logger=Logger.getLogger(LoadUserSettingTask.class);

	@Override
	public void execute() {
		try{
			String setting = CommonUtil.readFileToString(FilePathManage.userSetting);
			UserSetting userSetting = JsonUtil.toBean(setting, UserSetting.class);
			userSetting.toConfig();
		}catch(Exception e){
			logger.error("用户配置加载失败",e);
		}
	}

	@Override
	public void stop() {
		
	}

}
