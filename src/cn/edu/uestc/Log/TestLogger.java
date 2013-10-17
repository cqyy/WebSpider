package cn.edu.uestc.Log;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestLogger extends TestCase {
	
	public void testWriteLog() throws ClassNotFoundException{
		String path="../xiaomiBBS/log/";
		File file=new File(path);
		int sLen=0;
		if(file.exists()){
			sLen=file.listFiles().length;
			}
		for(int i=0;i<230;i++){
			Logger.writeLog(i+"*********log*******");
		}
		int eLen=file.listFiles().length;
		Assert.assertEquals(eLen-sLen, 3);
	}
}
