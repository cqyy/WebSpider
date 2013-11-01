package cn.edu.uestc.wsc.bbs_360;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.uestc.wsc.bbs.URLMaker;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;
import junit.framework.Assert;
import junit.framework.TestCase;

public class URLMakerTest extends TestCase {
	HtmlConnect con=new HtmlConnect();
	URLMaker urlMaker=new URLMaker360();
	
	public void testCreatePostlistUrl() {
		int blockId=92; 
		int page=1;

		try {
			String url=urlMaker.createPostlistURL(blockId, page);
			Document doc=Jsoup.parse(
					con.getHtmlByUrl(url));
			Element ele=doc.select("#threadlist").first();
			Assert.assertNotNull(ele);
		} catch (IOException e) {
			fail();
		}
	}
	
	public void testCreatePostUrl(){
		String postId="2860178";
		int page=1;
		String url=urlMaker.createPostURL(postId, page);
		Document doc;
		try {
			doc = Jsoup.parse(
					con.getHtmlByUrl(url));
			Element ele=doc.select("#postlist").first();
			Assert.assertNotNull(ele);
		} catch (IOException e) {
			fail();
		}

	}
}
