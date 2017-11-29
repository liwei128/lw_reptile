package com.abner.service.task;

import com.abner.annotation.Service;
import com.abner.manage.Config;
import com.abner.manage.FilePathManage;
import com.abner.enums.TaskName;
import com.abner.utils.CommonUtil;
import com.abner.utils.JsonUtil;

/**
 * 用户配置保存服务
 * @author wei.li
 * @time 2017年11月23日下午1:27:55
 */
@Service(name = TaskName.SAVEUSERSETTING)
public class SaveUserSettingTask implements Task{

	@Override
	public void execute() {
		CommonUtil.checkPath(FilePathManage.configPath);
		CommonUtil.writeToFile(JsonUtil.toString(Config.userSetting), FilePathManage.userSetting);
	}

	@Override
	public void stop() {

	}

}
