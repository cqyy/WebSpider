package cn.edu.uestc.wsc.FileOut;

import java.util.Calendar;

public class TimeFilenameCreater implements FilenameCreater{
	@Override
	public String getFileName() {
		Calendar cal=Calendar.getInstance();
		return String.valueOf(cal.getTimeInMillis());
	}

}
