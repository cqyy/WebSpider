package cn.edu.uestc.wsc.bbs_hiapk;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.uestc.wsc.bbs.URLMaker;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;
import junit.framework.Assert;
import junit.framework.TestCase;

public class URLMakerhiapkTest extends TestCase {
	
	public void testCreatePostlistUrl(){
		int blockId=86; 
		int page=1;
		HtmlConnect con=new HtmlConnect();
		URLMaker um=new URLMakerhiapk();
		try {
			String url=um.createPostlistURL(blockId, page);
			Document doc=Jsoup.parse(
					con.getHtmlByUrl(url));
			Element ele=doc.select("#moderate").first();
			Assert.assertNotNull(ele);
		} catch (IOException e) {
			fail();
		}
	}
	
	
	public void testCreatePostUrl(){
		String postId="4277807";
		int page=2;
		HtmlConnect con=new HtmlConnect();
		con.setCharset("gbk");
		URLMaker um=new URLMakerhiapk();
		String url=um.createPostURL(postId, page);
		Document doc;
		try {
			doc = Jsoup.parse(
					con.getHtmlByUrl(url));
			/*FileWriter writer=new FileWriter("../test.html");
			writer.write(doc.html());
			writer.close();
			*/
			Element ele=doc.select("#postlist").first();
			Assert.assertNotNull(ele);
		} catch (IOException e) {
			fail();
		}
	}

}
