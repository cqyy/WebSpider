package cn.edu.uestc.wsc.bbs_360;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ListExtractor360Test extends TestCase {
	private String classpath="cn.edu.uestc.wsc.bbs_360.ListExtractor360";
	
	//测试基本数据会随论坛变化而变化
	public void testGetPage(){
		int[] block=new int[]{92,93,1485,84,80,166};
		int[] pages=new int[]{1000,16,31,90,1000,565};
		try{
			Class<?> pe=Class.forName(classpath);
			Constructor<?> cst=pe.getDeclaredConstructor(PostList.class);
			
			ListExtractor360 pe_obj=(ListExtractor360)cst.newInstance(new PostList());
			
			Method mt_getPage=pe.getDeclaredMethod("getPage",new Class<?>[]{int.class});
			mt_getPage.setAccessible(true);
			
			for(int i=0;i<block.length;i++){
				Object result=mt_getPage.invoke(pe_obj, block[i]);
				Assert.assertEquals(result, pages[i]);
				
			}			
		}catch(Exception e){
			fail();
		}
		
	}

	public void testExtractList(){
		int[] block=new int[]{92,93,84,80,166};
		PostList list=new PostList();
		HtmlConnect con=new HtmlConnect();
		try{
			Class<?> le=Class.forName(classpath);
			Constructor<?> sct=le.getDeclaredConstructor(PostList.class);
			Object le_obj=sct.newInstance(list);
			
			Method mt_et=le.getDeclaredMethod("extractList",new Class<?>[]{Document.class});
			mt_et.setAccessible(true);
			
			Document doc=null;
			for(int i=0;i<block.length;i++){
				doc=Jsoup.parse(
						con.getHtmlByUrl(
								URLMaker.createPostlistUrl(block[i],1)));
				mt_et.invoke(le_obj, doc);
				//System.out.println(list.size());
				Assert.assertEquals(30, list.size());
				Post post=null;
				while(!list.isEmpty()){
					post=list.pop();
					Assert.assertNotNull(post.getId());
					Assert.assertNotNull(post.getTitle());
					Assert.assertNotNull(post.getTime());
					Assert.assertNotNull(post.getWriter());
					Assert.assertNotNull(post.getCommentNum());
					//System.out.println(post.text());
					}
			}
			
			///check
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		
	}
	
}
