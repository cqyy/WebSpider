/*
 * */
package cn.edu.uestc.wsc.FileOut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOut {
	private static String basePath="../xiaomiBBS/";;
	private static final int size=100;//每个文件写入数据条数
	private static FilenameCreater filename=new TimeFilenameCreater();;
	private static FileWriter out=null;
	private static int count=0;

	
	public static synchronized void writeLine(String content) {
		if(count==size){
			count=0;
			close();
		}
		if(out==null){
			try {
				File file=new File(basePath);
				if(!file.exists()){	file.mkdirs();}
				
				file=new File(basePath+filename.getFileName()+".txt");
				
				if(!file.exists()) { file.createNewFile();}
				
				out=new FileWriter(file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		try {
			out.write(content+"\n");
			count++;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void close(){
		if(out==null){
			return;
		}
		try{
			out.close();
			}catch(Exception e){	
				e.printStackTrace();
				}finally{
					out=null;
					}
		}

}
