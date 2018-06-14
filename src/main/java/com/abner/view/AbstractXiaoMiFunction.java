package com.abner.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.abner.controller.XiaoMiController;
import com.abner.manage.ServiceFactory;
import com.abner.manage.StatusManage;
import com.abner.manage.mi.Config;
import com.abner.pojo.mi.CustomRule;
import com.abner.pojo.mi.GoodsInfo;
import com.abner.pojo.mi.User;
/**
 * 小米抢购服务
 * @author liwei
 * @date: 2018年6月12日 下午3:49:23
 *
 */
public abstract class AbstractXiaoMiFunction {
		//小米主服务
		private static  XiaoMiController xiaoMiController = ServiceFactory.getService(XiaoMiController.class);
		
		/**
		 * 窗体控件
		 */
		public abstract Display getDisplay();
		public abstract Shell getShell();
		public abstract Button getHideButton();
		public abstract Text getLogText();
		public abstract Button getStartButton();
		public abstract Button getQuitButton();
		
		public abstract Text getUrlText();
		public abstract Text getBuyTimeText();
		public abstract Text getDurationText();
		public abstract Text getUserText();
		public abstract Text getPasswordText();
		
		public abstract Combo getOption1();
		public abstract Combo getOption2();
		public abstract Combo getOption3();
		
		
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
						System.exit(0);
					}
				}
			};
		}
		
		//窗体关闭监控
		public ShellAdapter getCloseListener(){
			return new ShellAdapter() {
				@Override
		        public void shellClosed(ShellEvent e) {
		        	if(e.doit=verifyMsg("确认要退出吗")==SWT.YES){
						System.exit(0);
					}
					
		        }
			};
		}
		
		//开始按钮
		public SelectionAdapter getStartFunction(){
			return new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					try {
						//读取用户界面配置
						readParameter(); 
						//修改状态为爬取开始
						modifyStatus(false);
						//开始抢购
						xiaoMiController.start();
						
					} catch (Exception e) {
						sendErrMsg(e.getMessage());
					}
				}
			};
		}
		
		//修改状态后控件的变化效果
		private void modifyStatus(boolean isFinish) {
			getStartButton().setVisible(isFinish);
			getUrlText().setEditable(isFinish);
			getBuyTimeText().setEditable(isFinish);
			getDurationText().setEditable(isFinish);
			getUserText().setEditable(isFinish);
			getPasswordText().setEditable(isFinish);
			
			getOption1().setEnabled(isFinish);
			getOption2().setEnabled(isFinish);
			getOption3().setEnabled(isFinish);
		}
		public void readParameter() throws Exception {
			String url = getUrlText().getText().trim();
			int index1 = getOption1().getSelectionIndex();
			int index2 = getOption2().getSelectionIndex();
			int index3 = getOption3().getSelectionIndex();
			Config.goodsInfo = new GoodsInfo(url,index1,index2,index3);
			String buyTime = getBuyTimeText().getText().trim();
			String duration = getDurationText().getText().trim();
			Config.customRule = new CustomRule(buyTime,duration);
			
			String user = getUserText().getText().trim();
			String password = getPasswordText().getText().trim();
			Config.user = new User(user,password);
			Config.submitCount = new AtomicInteger(0);
		}
		
		//初始化视图
		private void initView() {
			xiaoMiController.init();
			//设置界面参数
			setViewSetting();
		}
		
		private void setViewSetting() {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = System.currentTimeMillis()+2*60*1000;
			getBuyTimeText().setText(format.format(new Date(time)));
			
		}
		//日志输出
		private void loadLog() {
			String loadLog = xiaoMiController.loadLog();
			if(loadLog.length()==0){
				return;
			}
			if(getLogText().getLineCount()>3000){
				getLogText().setText(loadLog);
				return;
			}
			getLogText().append(loadLog);
			
		}
		
		//任务完成后的处理逻辑
		private void finishMsg() {
			if(StatusManage.isEnd){
				//修改状态为完成
				modifyStatus(true);
				//发送消息
				sendMsg(StatusManage.endMsg);
				//恢复初始状态
				StatusManage.isEnd = false;
				
				
			}
			
		}
		//打开视图
		public void openView(){
			initView();
			getShell().open();
			while (!getShell().isDisposed()) {
				//日志输出
				loadLog();
				//任务完成
				finishMsg();
				
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
		
		
		
		//错误弹框
		public void sendErrMsg(String msg) {
			MessageBox errorBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			errorBox.setText("错误");
			if (msg == null) {
				errorBox.setMessage("未知错误,请检查参数设置是否正确");
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
