package cn.edu.uestc.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/*
 * 日志记录类
 * 静态访问方式，多线程安全*/
public class Logger {
	private static final String logPath="../xiaomiBBS/log/";  //日志文件
	private static final int logSize=100;  //每个日志文件写入的日志数量
	private static String fileName=null;     //日志文件名
	private static int count=0;
	/*
	 * @function 写一条日志
	 * @param log:日志*/
	public synchronized static void writeLog(String log){
		
		if(fileName==null||count>=logSize){
			fileName=getNameByTime();
			//System.out.println(fileName);
			count=0;
		}
		
		File file=new File(logPath);		
		if(!file.exists()){file.mkdirs();}
		
		file=new File(logPath+fileName);
		FileWriter out=null;
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}
		try {
			out=new FileWriter(file,true);
			out.write(log+"\n");
			out.flush();
			out.close();
			count++;
		} catch (IOException e) {e.printStackTrace();}


	}
	
	/*
	 * @function 根据时间生成一个唯一的日志文件名
	 * @return String:文件名,包括文件格式
	 * */
	private static String getNameByTime(){
		String fileName="";
		Calendar ca=Calendar.getInstance();
		fileName=String.valueOf(ca.getTimeInMillis());
		return 	fileName+".txt";
	}
}
