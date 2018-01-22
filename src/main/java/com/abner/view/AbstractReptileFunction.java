package com.abner.view;


import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.abner.controller.ReptileController;
import com.abner.enums.MonitorName;
import com.abner.manage.Config;
import com.abner.manage.ServiceFactory;
import com.abner.manage.StatusManage;
import com.abner.pojo.UserSetting;

/**
 * 爬虫事件
 * @author wei.li
 * @time 2017年7月4日下午12:13:27
 */
public abstract class AbstractReptileFunction {
	
	//爬虫主服务
	private static ReptileController reptileController = ServiceFactory.getService(ReptileController.class);
	
	/**
	 * 窗体控件
	 */
	public abstract Display getDisplay();
	public abstract Shell getShell();
	public abstract Label getPing();
	public abstract Text getUrlsText();
	public abstract Button getFilePathButton();
	public abstract Text getFilePathText();
	public abstract Text getFileSizeText();
	public abstract Button getHideButton();
	public abstract Button getStartButton();
	public abstract Button getPauseButton();
	public abstract Button getQuitButton();
	public abstract Text getLogText();
	public abstract Combo getCombo();
	public abstract Combo getVelocity();
	public abstract Combo getIsNowDomain();
	public abstract Label getSumUrlText();
	public abstract Label getDoneUrlText();
	public abstract Label getFailUrlText();
	public abstract Label getUrlRateText();
	public abstract Label getSumImgText();
	public abstract Label getDoneImgText();
	public abstract Label getFailImgText();
	public abstract Label getImgRateText();
	public abstract Text getEmailAddressText();
	public abstract Text getKeywordText();
	
	//开始按钮
	public SelectionAdapter getStartFunction(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if(StatusManage.ping==0){
					sendErrMsg("网络连接已断开");
					return;
				}
				try {
					//读取用户界面配置
					readParameter(); 
					//是否接着上次爬取
					if(!reptileController.isFinish()){
						if(verifyMsg("检测到上次爬取未全部完成，是否继续上次爬取？")==SWT.NO){
							reptileController.cleanCache();
						}
					}
					//修改状态为爬取开始
					modifyStatus(false);
					//保存用户配置到文件
					reptileController.saveUserSetting();
					//开始爬取
					reptileController.start();
					
				} catch (Exception e) {
					sendErrMsg(e.getMessage());
				}
			}
		};
	}
	//暂停按钮
	public SelectionAdapter getPauseFunction(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StatusManage.isPause=true;
			}
		};
	}
	//日志显示按钮
	public SelectionAdapter getHideFunction(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(getLogText().isVisible()){
					getLogText().setVisible(false);
					getShell().setSize(650, 375);
					getHideButton().setText("显示日志");
				}else{
					getShell().setSize(650, 720);
					getLogText().setVisible(true);
					getHideButton().setText("隐藏日志");
					
				}
			}
		};
	}
	
	//退出按钮
	public SelectionAdapter getQuitFunction(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(verifyMsg("确认要退出吗")==SWT.YES){
					reptileController.saveUserSetting();
					reptileController.saveAddress();
					System.exit(0);
				}
			}
		};
	}
	
	//目录选择按钮
	public SelectionAdapter getFilePathFunction(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog folderdlg=new DirectoryDialog(getShell());
				//设置文件对话框的标题
				folderdlg.setText("图片保存路径");
				//设置初始路径
				folderdlg.setFilterPath("SystemDrive");
				//设置对话框提示文本信息
				folderdlg.setMessage("请选择图片保存文件夹");
				//打开文件对话框，返回选中文件夹目录
				String selecteddir=folderdlg.open();
				if(selecteddir!=null){
					getFilePathText().setText(selecteddir);
				}   
			}
		};
	}
	
	//速度选择下拉框
	public SelectionListener getVelocityFunction() {
		 return new SelectionAdapter(){
		     @Override
		     public void widgetSelected(SelectionEvent e) {
		    	 int index = getVelocity().getSelectionIndex();
			     Config.userSetting.setVelocity(index);
		     }
	    };
	}
	
	//窗体关闭监控
	public ShellAdapter getCloseListener(){
		return new ShellAdapter() {
			@Override
	        public void shellClosed(ShellEvent e) {
	        	if(e.doit=verifyMsg("确认要退出吗")==SWT.YES){
	        		reptileController.saveUserSetting();
					reptileController.saveAddress();
					System.exit(0);
				}
				
	        }
		};
	}
	//打开视图
	public void openView(){
		initView();
		getShell().open();
		while (!getShell().isDisposed()) {
			//监控信息
			loadMonitorData();
			//日志输出
			loadLog();
			//断网提示
			networkMsg();
			//爬取完成或暂停处理
			finishMsg();
			//定时扫描
			if (!getDisplay().readAndDispatch()) {
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//getDisplay().sleep();
		}
		getDisplay().dispose();
	}
	
	//监控信息
	private void loadMonitorData() {
		//获取数据
		String key = ""+getCombo().getSelectionIndex();
        int timeMinutes = (int) getCombo().getData(key);
		Map<String, Long> monitorData = reptileController.getAllMonitorData(timeMinutes);
		//解析数据
		Long sumUrl = monitorData.get(MonitorName.SUMURL.name());
		Long doneUrl = monitorData.get(MonitorName.DONEURL.name());
		Long failUrl = monitorData.get(MonitorName.FAILURL.name());
		double urlRate = reptileController.calculateRate(doneUrl, failUrl);
		Long sumImg = monitorData.get(MonitorName.SUMIMG.name());
		Long doneImg = monitorData.get(MonitorName.DONEIMG.name());
		Long failImg = monitorData.get(MonitorName.FAILIMG.name());
		double imgRate = reptileController.calculateRate(doneImg, failImg);
		//展示
		getSumUrlText().setText(String.valueOf(sumUrl));
		getDoneUrlText().setText(String.valueOf(doneUrl));
		getFailUrlText().setText(String.valueOf(failUrl));
		getUrlRateText().setText(String.valueOf(urlRate)+"%");
		getSumImgText().setText(String.valueOf(sumImg));
		getDoneImgText().setText(String.valueOf(doneImg));
		getFailImgText().setText(String.valueOf(failImg));
		getImgRateText().setText(String.valueOf(imgRate)+"%");
		
	}
	//爬取完成后的处理逻辑
	private void finishMsg() {
		if(StatusManage.urlFinish&&StatusManage.imgFinish){
			reptileController.end();
			//修改状态为爬取完成
			modifyStatus(true);
			//发送消息
			String msg = "爬取已完成";
			//恢复初始状态
			StatusManage.urlFinish = false;
			StatusManage.imgFinish = false;
			sendMsg(msg);
		}
		if(StatusManage.isPause){
			reptileController.end();
			//修改状态为爬取完成
			modifyStatus(true);
			//发送消息
			String msg = "爬虫服务暂停";
			//恢复初始状态
			StatusManage.isPause=false;
			sendMsg(msg);
		}
	}
	//网络异常提示
	private void networkMsg() {
		if(StatusManage.ping == 0){
			getPing().setText("网络异常");
		}else{
			getPing().setText("ping: "+StatusManage.ping+"ms");
		}
	}
	//日志输出
	private void loadLog() {
		String loadLog = reptileController.loadLog();
		if(loadLog.length()==0){
			return;
		}
		if(getLogText().getLineCount()>3000){
			getLogText().setText(loadLog);
			return;
		}
		getLogText().append(loadLog);
		
	}
	//初始化视图
	private void initView() {
		//初始化
		reptileController.init();
		//设置界面参数
		setViewSetting();
	}
	
	
	private void setViewSetting() {
		getUrlsText().setText(Config.userSetting.getUrls());
		getKeywordText().setText(Config.userSetting.getKeywords());
		getVelocity().select(Config.userSetting.getVelocity());
		getFileSizeText().setText(Config.userSetting.getFileSize());
		getFilePathText().setText(Config.userSetting.getFilePath());
		getIsNowDomain().select(Config.userSetting.getIsNowDomain());
		getEmailAddressText().setText(Config.userSetting.getEmailAddress());
		
	}
	
	//修改状态后控件的变化效果
	private void modifyStatus(boolean isFinish) {
		getStartButton().setVisible(isFinish);
		getPauseButton().setVisible(!isFinish);
		getFilePathButton().setVisible(isFinish);
		getUrlsText().setEditable(isFinish);
		getFilePathText().setEditable(isFinish);
		getFileSizeText().setEditable(isFinish);
		getKeywordText().setEditable(isFinish);
		getIsNowDomain().setEnabled(isFinish);
		getEmailAddressText().setEditable(isFinish);
	}

	//读取用户界面配置
	protected void readParameter() throws Exception {
		UserSetting userSetting = new UserSetting();
		userSetting.setUrls(getUrlsText().getText().trim());
		userSetting.setFilePath(getFilePathText().getText().trim());
		userSetting.setKeywords(getKeywordText().getText().trim());
		userSetting.setFileSize(getFileSizeText().getText().trim());
		userSetting.setIsNowDomain(getIsNowDomain().getSelectionIndex());
		userSetting.setVelocity(getVelocity().getSelectionIndex());
		userSetting.setEmailAddress(getEmailAddressText().getText().trim());
		userSetting.toConfig();
	}
	
	//错误弹框
	public void sendErrMsg(String msg) {
		MessageBox errorBox = new MessageBox(getShell(), SWT.ICON_ERROR);
		errorBox.setText("错误");
		if (msg == null) {
			errorBox.setMessage("未知错误,请检查参数设置是否正确或者清空缓存");
		} else {
			errorBox.setMessage(msg);
		}
		errorBox.open();
	}
	//确认框
	public int verifyMsg(String msg){
		MessageBox infoMsg = new MessageBox(getShell(), SWT.YES|SWT.NO|SWT.ICON_INFORMATION );
		infoMsg.setText("请确认");
		infoMsg.setMessage(msg);
		return infoMsg.open();
	}
	//提示弹框
	public void sendMsg(String msg) {
		MessageBox infoMsg = new MessageBox(getShell(), SWT.ICON_WORKING);
		infoMsg.setText("提示");
		infoMsg.setMessage(msg);
		infoMsg.open();
	}
}
