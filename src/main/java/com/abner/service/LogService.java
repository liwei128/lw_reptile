package com.abner.service;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;
import java.util.Scanner;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

import com.abner.annotation.Async;
import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.db.LogStorage;
/**
 * 日志服务
 * @author wei.li
 * @time 2017年11月23日下午1:26:22
 */
@Service
public class LogService{
	
	@SuppressWarnings("resource")
	@Async
	@Singleton
	public void readLogs() {
		PipedReader reader = interceptLog();
		while(true){
			Scanner scanner = new Scanner(reader);
	        while (scanner.hasNextLine()) { 
	        	String text=scanner.nextLine();
	        	LogStorage.addLog(text);
	        }  
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private PipedReader interceptLog() {
		Appender appender = Logger.getRootLogger().getAppender("stdout");  
		try {
			PipedReader reader = new PipedReader();
			Writer writer= new PipedWriter(reader);
			((WriterAppender) appender).setWriter(writer);
			return reader;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	
	
}
