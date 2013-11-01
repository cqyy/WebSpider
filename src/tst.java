import java.io.BufferedReader;
import java.io.FileReader;
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
		/*String url="http://bbs.anzhi.com/forum.php?mobile=no";
		HtmlConnect con=new HtmlConnect();
		String html=con.getHtmlByUrl(url);
		*/
		BufferedReader reader=new BufferedReader(new FileReader("../block.html"));
        String tempString = null;
        StringBuffer html=new StringBuffer();
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
        	html.append(tempString);
        }
		Set set=new HashSet();
		String regex="href=\"forum-\\d{2,4}-1.html";
		Pattern pattern=Pattern.compile(regex);
		Pattern pattern2=Pattern.compile("\\d{2,6}");
		Matcher matcher=pattern.matcher(html.toString());
		while(matcher.find()){
			Matcher machter2=pattern2.matcher(matcher.group());
			if(machter2.find()){
				set.add(machter2.group());
			}
		}
		System.out.print(set);

	}

}
