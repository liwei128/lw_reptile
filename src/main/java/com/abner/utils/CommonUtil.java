package com.abner.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.abner.manage.Config;
import com.abner.manage.FilePathManage;
import com.abner.pojo.MyUrl;
/**
 * 公共工具类
 * @author wei.li
 * @time 2017年11月28日上午10:26:53
 */
public class CommonUtil {
	
	private static  Logger logger=Logger.getLogger(CommonUtil.class);
	
	/**
	 * 通过url来设置文件目录名
	 * 
	 */
	public static  String getFilePath(MyUrl imgUrl) {
		String url = imgUrl.getUrl();
		String title = imgUrl.getTitle();
		//文件名设置
		String suffix = url.substring(url.lastIndexOf(".")); 
		String fileName = UUID.randomUUID().toString()+suffix;
		//检查目录是否存在
		File file = new File(Config.filePath+title);
	    if(!file.exists()){
	    	file.mkdirs();
	    }
        //完整文件名
		String path=Config.filePath+title+"/"+fileName;
        return path;
	}
	
	/**
	 * 
	 * 资源关闭
	 */
	public static void closeStream(Closeable... streams) {
		for(Closeable stream:streams){
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("资源关闭异常",e);
				}
			}
			
		}
	}
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
     * 读取在本工程存放文件
     * @param filePath 本工程存放的文件路径
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
            e.printStackTrace();
        }
        return fileContent.toString();
    }
    
    /**
     * 
     * 写入文件
     */
    public static  boolean writeToFile(String data,String fileName){
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
	
	/**
	 * 检测目录是否存在，如果不存在则创建
	 * 
	 */
	public static void checkPath(String configPath) {
		File file = new File(FilePathManage.configPath);
		if(!file.exists()){
			file.mkdirs();
		}	
	}
	
	/**
	 * 计算失败率
	 */
	public static double calculateRate(long doneNum, long failNum) {
		long sum = doneNum + failNum;
		if(sum==0){
			return 0;
		}
		BigDecimal b1 = new BigDecimal(failNum);
		BigDecimal b2 = new BigDecimal(sum);
		BigDecimal b3 = new BigDecimal(100);
		BigDecimal rate = b1.divide(b2,4,BigDecimal.ROUND_HALF_UP).multiply(b3);
		return rate.doubleValue();
	}

}
