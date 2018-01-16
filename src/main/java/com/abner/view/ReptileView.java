package com.abner.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 爬虫界面
 * @author wei.li
 * @time 2017年6月19日下午3:03:29
 */
public class ReptileView extends AbstractReptileFunction{
	
	private Display display;
	private Shell shell;
	private Label ping;
	private Label urls;
	private Text urlsText;
	private Label keyword;
	private Text keywordText;
	private Label filePath;
	private Button filePathButton;
	private Text filePathText;
	private Label fileSize;
	private Text fileSizeText;
	private Combo combo;
	private Label comboLable;
	private Label sumUrl;
	private Label sumUrlText;
	private Label doneUrl;
	private Label doneUrlText;
	private Label failUrl;
	private Label failUrlText;
	private Label urlRate;
	private Label urlRateText;
	private Label sumImg;
	private Label sumImgText;
	private Label doneImg;
	private Label doneImgText;
	private Label failImg;
	private Label failImgText;
	private Label imgRate;
	private Label imgRateText;
	private Button hideButton;
	private Button startButton;
	private Button pauseButton;
	private Button quitButton;
	private Text logText;
	private Label emailAddress;
	private Text emailAddressText;
	private Combo velocity;
	private Label velocityLable;
	private Combo isNowDomain;
	private Label isNowDomainLable;
	
	
	@Override
	public Label getPing() {
		return ping;
	}
	@Override
	public Text getEmailAddressText() {
		return emailAddressText;
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
	public Text getUrlsText() {
		return urlsText;
	}

	@Override
	public Button getFilePathButton() {
		return filePathButton;
	}
	@Override
	public Text getFilePathText() {
		return filePathText;
	}
	@Override
	public Text getFileSizeText() {
		return fileSizeText;
	}
	@Override
	public Button getHideButton() {
		return hideButton;
	}
	@Override
	public Button getStartButton() {
		return startButton;
	}

	@Override
	public Button getPauseButton() {
		return pauseButton;
	}

	@Override
	public Button getQuitButton() {
		return quitButton;
	}
	@Override
	public Text getLogText() {
		return logText;
	}
	@Override
	public Combo getCombo() {
		return combo;
	}
	@Override
	public Label getSumUrlText() {
		return sumUrlText;
	}
	@Override
	public Label getDoneUrlText() {
		return doneUrlText;
	}
	@Override
	public Label getFailUrlText() {
		return failUrlText;
	}
	@Override
	public Label getUrlRateText() {
		return urlRateText;
	}
	@Override
	public Label getSumImgText() {
		return sumImgText;
	}
	@Override
	public Label getDoneImgText() {
		return doneImgText;
	}
	@Override
	public Label getFailImgText() {
		return failImgText;
	}
	@Override
	public Label getImgRateText() {
		return imgRateText;
	}
	@Override
	public Combo getVelocity() {
		return velocity;
	}
	@Override
	public Combo getIsNowDomain() {
		return isNowDomain;
	}
	@Override
	public Text getKeywordText() {
		return keywordText;
	}

	public ReptileView() {
		
		//设置窗体样式
		display = new Display();
		shell = new Shell(display, SWT.MIN);
		shell.setText("图片爬虫");
		shell.setSize(650, 375);
		
		/**
		 * ping值
		 */
		ping = new Label(shell, SWT.PUSH);
		ping.setText("ping: 999ms");
		ping.setLocation(550, 4);
		ping.setSize(80, 20);
        
		/**
		 * 网址
		 */
		urls = new Label(shell, SWT.PUSH);
		urls.setText("网址（多个用英文,隔开）");
		urls.setLocation(20, 30);
		urls.setSize(200, 20);
		urlsText = new Text(shell, SWT.BORDER);
		urlsText.setLocation(220, 30);
		urlsText.setSize(400, 20);
		
		/**
		 * 搜索关键字
		 */
		keyword = new Label(shell, SWT.PUSH);
		keyword.setText("搜索关键字（英文,隔开）");
		keyword.setLocation(20, 64);
		keyword.setSize(200, 20);
		keywordText = new Text(shell, SWT.BORDER);
		keywordText.setLocation(220, 64);
		keywordText.setSize(400, 20);
		
		/**
		 * 图片保存路径
		 */
		filePath = new Label(shell, SWT.PUSH);
		filePath.setText("图片保存路径");
		filePath.setLocation(20, 98);
		filePath.setSize(170, 20);
		
		filePathButton = new Button(shell, SWT.PUSH);
		filePathButton.setLocation(570, 98);
		filePathButton.setSize(50, 22);
		filePathButton.setText("请选择");
		
		filePathText = new Text(shell, SWT.BORDER);
		filePathText.setLocation(220, 99);
		filePathText.setSize(400, 20);
		
		/**
		 * 最小文件限制
		 */
		fileSize = new Label(shell, SWT.PUSH);
		fileSize.setText("最小文件限制（kb）");
		fileSize.setLocation(20, 132);
		fileSize.setSize(200, 20);

		fileSizeText = new Text(shell, SWT.BORDER);
		fileSizeText.setLocation(220, 132);
		fileSizeText.setSize(400, 20);
		
		/**
		 * 通知邮件地址
		 */
        emailAddress = new Label(shell, SWT.PUSH);
        emailAddress.setText("通知邮件地址:");
        emailAddress.setLocation(20, 166);
        emailAddress.setSize(200, 20);
        
        emailAddressText = new Text(shell, SWT.BORDER);
        emailAddressText.setLocation(220, 166);
        emailAddressText.setSize(400, 20);

        /**
         * 爬虫速度
         */
        velocity = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        velocity.add("1");
        velocity.add("2");
        velocity.add("3");
        velocity.add("4");
        velocity.add("5");
        velocity.setData("0", 1);
        velocity.setData("1", 2);
        velocity.setData("2", 3);
        velocity.setData("3", 4);
        velocity.setData("4", 5);
        velocity.select(0);
        velocity.setLocation(80,200);
        velocity.setSize(100, 20);
        
        velocityLable = new Label(shell, SWT.PUSH);
        velocityLable.setText("爬虫速度:");
        velocityLable.setLocation(20, 203);
        velocityLable.setSize(60, 20);
        
        /**
         * 是否指定域名
         */
        isNowDomain = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        isNowDomain.add("是");
        isNowDomain.add("否");
        isNowDomain.setData("0", true);
        isNowDomain.setData("1", false);
        isNowDomain.select(0);
        isNowDomain.setLocation(300,200);
        isNowDomain.setSize(100, 20);
        
        isNowDomainLable = new Label(shell, SWT.PUSH);
        isNowDomainLable.setText("是否指定域名:");
        isNowDomainLable.setLocation(220, 203);
        isNowDomainLable.setSize(80, 20);
        
        /**
         * 监控时间段
         */
		combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.add("全部");
        combo.add("1小时内");
        combo.add("30分钟内");
        combo.add("10分钟内");
        combo.setData("0", 0);
        combo.setData("1", 60);
        combo.setData("2", 30);
        combo.setData("3", 10);
        combo.select(0);
        combo.setLocation(520,200);
        combo.setSize(100, 20);
        
        comboLable = new Label(shell, SWT.PUSH);
        comboLable.setText("监控时间段:");
        comboLable.setLocation(450, 203);
        comboLable.setSize(70, 20);
        

        /**
         * 监控详情
         */
        sumUrl = new Label(shell, SWT.PUSH);
        sumUrl.setText("探测到链接数:");
        sumUrl.setLocation(20, 236);
        sumUrl.setSize(80, 20);
        
        sumUrlText = new Label(shell, SWT.PUSH);
        sumUrlText.setText("0");
        sumUrlText.setLocation(100, 236);
        sumUrlText.setSize(80, 20);
        
        doneUrl = new Label(shell, SWT.PUSH);
        doneUrl.setText("抓取链接数:");
        doneUrl.setLocation(180, 236);
        doneUrl.setSize(80, 20);
        
        doneUrlText = new Label(shell, SWT.PUSH);
        doneUrlText.setText("0");
        doneUrlText.setLocation(260, 236);
        doneUrlText.setSize(80, 20);
        
        failUrl = new Label(shell, SWT.PUSH);
        failUrl.setText("失败链接数:");
        failUrl.setLocation(340, 236);
        failUrl.setSize(80, 20);
        
        failUrlText = new Label(shell, SWT.PUSH);
        failUrlText.setText("0");
        failUrlText.setLocation(420, 236);
        failUrlText.setSize(80, 20);
        
        urlRate = new Label(shell, SWT.PUSH);
        urlRate.setText("链接失败率:");
        urlRate.setLocation(500, 236);
        urlRate.setSize(80, 20);
        
        urlRateText = new Label(shell, SWT.PUSH);
        urlRateText.setText("");
        urlRateText.setLocation(580, 236);
        urlRateText.setSize(80, 20);
        
        sumImg = new Label(shell, SWT.PUSH);
        sumImg.setText("探测到图片数:");
        sumImg.setLocation(20, 266);
        sumImg.setSize(80, 20);
        
        sumImgText = new Label(shell, SWT.PUSH);
        sumImgText.setText("0");
        sumImgText.setLocation(100, 266);
        sumImgText.setSize(80, 20);
        
        doneImg = new Label(shell, SWT.PUSH);
        doneImg.setText("下载图片数:");
        doneImg.setLocation(180, 266);
        doneImg.setSize(80, 20);
        
        doneImgText = new Label(shell, SWT.PUSH);
        doneImgText.setText("0");
        doneImgText.setLocation(260, 266);
        doneImgText.setSize(80, 20);
        
        failImg = new Label(shell, SWT.PUSH);
        failImg.setText("失败图片数:");
        failImg.setLocation(340, 266);
        failImg.setSize(80, 20);
        
        failImgText = new Label(shell, SWT.PUSH);
        failImgText.setText("0");
        failImgText.setLocation(420, 266);
        failImgText.setSize(80, 20);
        
        imgRate = new Label(shell, SWT.PUSH);
        imgRate.setText("下载失败率:");
        imgRate.setLocation(500, 266);
        imgRate.setSize(80, 20);
        
        imgRateText = new Label(shell, SWT.PUSH);
        imgRateText.setText("");
        imgRateText.setLocation(580, 266);
        imgRateText.setSize(80, 20);
      
		
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
		
		pauseButton = new Button(shell, SWT.PUSH);
		pauseButton.setLocation(430, 300);
		pauseButton.setSize(80, 30);
		pauseButton.setText("暂停");
		pauseButton.setVisible(false);
		
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
		pauseButton.addSelectionListener(getPauseFunction());
		hideButton.addSelectionListener(getHideFunction());
		quitButton.addSelectionListener(getQuitFunction());
		filePathButton.addSelectionListener(getFilePathFunction());
		velocity.addSelectionListener(getVelocityFunction());
		shell.addShellListener(getCloseListener());
		
		/**
		 * 打开视图
		 */
		openView();
	}
		
}
