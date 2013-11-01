import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import cn.edu.uestc.wsc.webConnect.HtmlConnect;


public class tst {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String url="http://bbs.hiapk.com/forum.php?mobile=no";
		HtmlConnect con=new HtmlConnect();
		String html=con.getHtmlByUrl(url);
		Set set=new HashSet();
		String regex="http://bbs.hiapk.com/forum-\\d{2,4}-1.html";
		Pattern pattern=Pattern.compile(regex);
		Pattern pattern2=Pattern.compile("\\d{2,4}");
		Matcher matcher=pattern.matcher(html);
		while(matcher.find()){
			Matcher machter2=pattern2.matcher(matcher.group());
			if(machter2.find()){
				set.add(machter2.group());
			}
		}
		System.out.print(set);

	}

}
