package com.abner.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件操作工具类
 * @author wei.li
 * @time 2017年11月28日上午10:26:53
 */
public class FileUtil {
	
	private static  Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 删除文件
	 * 
	 */
	public static void deleteFile(String filePath) {
		if(filePath==null){
			return;
		}
		File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists()&&file.isFile()) {
            file.delete();
        }
		
	}
	/**
	 * 文件是否存在
	 */
	public static boolean isFile(String filePath) {
		if(filePath==null){
			return false;
		}
		File file = new File(filePath);
        return file.exists()&&file.isFile();
		
	}
	
	 /**
     * 读取文件
     * @param filePath 文件路径
     * @return
     */
    public static String readFileToString(String filePath){
    	StringBuilder fileContent = new StringBuilder();
    	try {
    		InputStream fileInputStream = new FileInputStream(filePath);
    		BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
    		String oneLineContent = null;
            while ( (oneLineContent = br.readLine()) != null) {
                fileContent.append(oneLineContent);
            }
            br.close();
            fileInputStream.close();
        } catch (IOException e) {
            logger.error("读取文件内容错误",e);
        }
        return fileContent.toString();
    }
    
    /**
     * 
     * 写入文件
     */
    public static boolean writeToFile(String data,String fileName){
    	boolean result = false;
		OutputStream output = null;
		OutputStreamWriter outputWriter = null;
		BufferedWriter writer = null;
		File file = new File(fileName);

		try {
			if(!file.exists()){
				file.createNewFile();
			}	
			output = new FileOutputStream(file);
			outputWriter = new OutputStreamWriter(output);
			writer = new BufferedWriter(outputWriter);
			writer.write(data);
			writer.flush();
			writer.close();
			outputWriter.close();
			output.close();
			result = true;
		} catch (Exception e) {
			logger.error("文件写入错误",e);
		}
		return result;
    }
    
    /**
     * 
     * 复制文件
     */
	public static boolean copyFile(String fromPath, String toPath) {    
	        try {
	        	BufferedInputStream input= new BufferedInputStream(new FileInputStream(fromPath));
	        	BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(toPath));
	        	byte[] data=new byte[1024];
	        	while(input.read(data)!=-1){
	        		output.write(data);
	        	}
	        	output.flush();
	        	input.close();
	        	output.close();
	        	return true;
	        } catch (IOException e) {
	            logger.error("文件复制出错",e);
	            return false;
	        }
		
	}

}
