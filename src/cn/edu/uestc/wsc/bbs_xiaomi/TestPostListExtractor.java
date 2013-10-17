package cn.edu.uestc.wsc.bbs_xiaomi;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSxmCache;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;
import junit.framework.*;

/*
 * @function
 * jUnit测试，测试PostistExtractor*/
public class TestPostListExtractor extends TestCase{
	
	public void testIsNew(){
		PostList list=new PostList();
		PostListExtractor ex=new PostListExtractor(list);
		try {
			ex.extract();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BBSxmCache cache=new BBSxmCache();
		Post post=null;
		while(!list.isEmpty()){
			post=list.pop();
			int replies=cache.getValue(post.getId());
			//System.out.println("postId:"+post.getId()+" replies:"+post.getCommentNum()+"  value:"+replies);
			if(replies==post.getCommentNum())
				fail("failed:"+post.getId());
		}
	}

	public void testIsLastPage(){
		int[] pages=new int[]{1,2,584,585,1000};
		boolean[] isLast=new boolean[]{false,false,false,true,true};
		Document doc=null;
		HtmlConnect con=new HtmlConnect();
		for(int i=0;i<pages.length;i++){
			try {
				Class<?> ple=Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostListExtractor");
				Constructor<?> ctr=ple.getConstructor(PostList.class);
				PostListExtractor ople=(PostListExtractor)ctr.newInstance(new PostList());
				Method mt_lp=ple.getDeclaredMethod("isLastPage",new Class<?>[]{Document.class});
				mt_lp.setAccessible(true);
				
				doc=Jsoup.parse(
						con.getHtmlByUrl(
								URLMaker.getURLofPostList(
										pages[i])));
				//System.out.println(i);
				Assert.assertEquals(isLast[i], mt_lp.invoke(ople, doc));
			} catch (ParseException | IOException e) {
				fail();
			} catch (ClassNotFoundException e) {
				fail();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				fail();
			} catch (SecurityException e) {
				fail();
			} catch (InstantiationException e) {
				fail();
			} catch (IllegalAccessException e) {
				fail();
			} catch (IllegalArgumentException e) {
				fail();
			} catch (InvocationTargetException e) {
				fail();
			}
		}
	}

	
}
