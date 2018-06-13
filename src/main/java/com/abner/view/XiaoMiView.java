package com.abner.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 小米抢购服务
 * @author liwei
 * @date: 2018年6月12日 下午3:49:47
 *
 */
public class XiaoMiView extends AbstractXiaoMiFunction{
	private Display display;
	private Shell shell;
	
	private Label url;
	private Text urlText;
	
	private Label optionLable;
	private Combo option1;
	private Combo option2;
	private Combo option3;
	
	private Label buyTime;
	private Text buyTimeText;
	
	private Label duration;
	private Text durationText;
	
	private Label user;
	private Text userText;
	
	private Label password;
	private Text passwordText;

	private Button hideButton;
	private Button startButton;
	private Button quitButton;
	
	private Text logText;
	
	

	
	
	
	public XiaoMiView() {
		
		//设置窗体样式
		display = new Display();
		shell = new Shell(display, SWT.MIN);
		shell.setText("fuckXiaoMi");
		shell.setSize(650, 375);
		
		
		/**
		 * 购买链接
		 */
		url = new Label(shell, SWT.PUSH);
		url.setText("购买链接:");
		url.setLocation(20, 30);
		url.setSize(100, 20);
		urlText = new Text(shell, SWT.BORDER);
		urlText.setLocation(120, 30);
		urlText.setSize(500, 20);
		
		
		optionLable = new Label(shell, SWT.PUSH);
		optionLable.setText("自定义选项:");
		optionLable.setLocation(20, 69);
		optionLable.setSize(100, 20);
		
		/**
         * 选项1
         */
		option1 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		option1.add("默认");
		option1.add("1");
		option1.add("2");
		option1.add("3");
		option1.add("4");
		option1.add("5");
		option1.select(0);
		option1.setLocation(120,66);
		option1.setSize(150, 20);
        
		
        
        
        /**
         * 选项2
         */
		option2 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		option2.add("默认");
		option2.add("1");
		option2.add("2");
		option2.add("3");
		option2.add("4");
		option2.add("5");
		option2.select(0);
		option2.setLocation(295,66);
		option2.setSize(150, 20);
        
        
        /**
         * 选项3
         */
		option3 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		option3.add("默认");
		option3.add("1");
		option3.add("2");
		option3.add("3");
		option3.select(0);
		option3.setLocation(470,66);
		option3.setSize(150, 20);
		
		/**
		 * 抢购时间
		 */
		buyTime = new Label(shell, SWT.PUSH);
		buyTime.setText("抢购时间:");
		buyTime.setLocation(20, 112);
		buyTime.setSize(100, 20);

		buyTimeText = new Text(shell, SWT.BORDER);
		buyTimeText.setLocation(120, 112);
		buyTimeText.setSize(500, 20);

		
		/**
		 * 抢购时长(分钟)
		 */
		duration = new Label(shell, SWT.PUSH);
		duration.setText("抢购时长(分钟):");
		duration.setLocation(20, 158);
		duration.setSize(100, 20);
        
		durationText = new Text(shell, SWT.BORDER);
		durationText.setLocation(120, 158);
		durationText.setSize(500, 20);

		/**
		 * 小米账号
		 */
		user = new Label(shell, SWT.PUSH);
		user.setText("账号:");
		user.setLocation(20, 204);
		user.setSize(100, 20);

		userText = new Text(shell, SWT.BORDER);
		userText.setLocation(120, 204);
		userText.setSize(500, 20);
		
		/**
		 * 密码
		 */
        password = new Label(shell, SWT.PUSH);
        password.setText("密码:");
        password.setLocation(20, 250);
        password.setSize(100, 20);
        
        passwordText = new Text(shell, SWT.BORDER|SWT.PASSWORD);
        passwordText.setLocation(120, 250);
        passwordText.setSize(500, 20);
        
      
		
        /**
         * 操作按钮
         */
		hideButton = new Button(shell, SWT.PUSH);
		hideButton.setLocation(20, 300);
		hideButton.setSize(80, 30);
		hideButton.setText("显示日志");
		
		
		startButton = new Button(shell, SWT.PUSH);
		startButton.setLocation(430, 300);
		startButton.setSize(80, 30);
		startButton.setText("开始");
		
		quitButton = new Button(shell, SWT.PUSH);
		quitButton.setLocation(540, 300);
		quitButton.setSize(80, 30);
		quitButton.setText("退出");

		
		
		/**
		 * 日志框
		 */
		logText = new Text(shell,SWT.H_SCROLL|SWT.V_SCROLL);
		logText.setLocation(0, 345);
		logText.setSize(645, 346);
		logText.setVisible(false);
		
		/**
		 * 按钮绑定事件
		 */
		startButton.addSelectionListener(getStartFunction());
		hideButton.addSelectionListener(getHideFunction());
		quitButton.addSelectionListener(getQuitFunction());
		shell.addShellListener(getCloseListener());
		
		/**
		 * 打开视图
		 */
		openView();
	}


	@Override
	public Display getDisplay() {
		return display;
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public Button getHideButton() {
		return hideButton;
	}

	@Override
	public Text getLogText() {

		return logText;
	}


	@Override
	public Button getStartButton() {
		
		return startButton;
	}


	@Override
	public Button getQuitButton() {
		return quitButton;
	}


	@Override
	public Text getUrlText() {
		return urlText;
	}


	@Override
	public Text getBuyTimeText() {
		return buyTimeText;
	}


	@Override
	public Text getDurationText() {
		return durationText;
	}


	@Override
	public Text getUserText() {
		return userText;
	}


	@Override
	public Text getPasswordText() {
		return passwordText;
	}


	@Override
	public Combo getOption1() {
		return option1;
	}


	@Override
	public Combo getOption2() {
		return option2;
	}


	@Override
	public Combo getOption3() {
		return option3;
	}
		
}
