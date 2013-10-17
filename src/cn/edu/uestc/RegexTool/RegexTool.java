package cn.edu.uestc.RegexTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class RegexTool {
	private static Pattern   pattern;  
    private static Matcher   ma;  
	public static String subString(String str,String regex){
		pattern=Pattern.compile(regex);
		ma=pattern.matcher(str);
		if(ma.find()){
			return ma.group();
		}
		return null;
		
	}

}
