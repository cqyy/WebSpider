package cn.edu.uestc.wsc.bbs_360;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PostExtractorTest extends TestCase {
	private final String classpath="cn.edu.uestc.wsc.bbs_360.PostExtractor360";
	
	
	public void testExtractReplies(){
		Post post1=new Post("2898749"," 〖公开课第34讲〗周鸿祎：硅谷创业者的启示","游离者1"," 发表于 13 小时前",39);
		Post post2=new Post("909861","获取更多积分的方法 ","aixinsun","2013-4-4 12:23:26 ",38 );
		PostList list=new PostList();
		list.push(post1);
		list.push(post2);
		
		try{
			Class<?> pe=Class.forName(classpath);
			Constructor<?> cst=pe.getDeclaredConstructor(PostList.class);
			PostExtractor pe_obj=(PostExtractor)cst.newInstance(list);
			
			Method mt_er=pe.getDeclaredMethod("extractReplies",new Class<?>[]{int.class,int.class});
			mt_er.setAccessible(true);
			
			Field pd=pe.getDeclaredField("_post");
			pd.setAccessible(true);
			
			while(!list.isEmpty()){
				pe_obj.getPost();
				Object result=mt_er.invoke(pe_obj, 2,1);
				Post post=(Post)pd.get(pe_obj);
				System.out.println(post.text());
				Assert.assertEquals(result, 10);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			fail();
			}
		
		
	}

	public void testExtractContent(){
		Post post1=new Post("2898749"," 〖公开课第34讲〗周鸿祎：硅谷创业者的启示","游离者1"," 发表于 13 小时前",39);
		Post post2=new Post("909861","获取更多积分的方法 ","aixinsun","2013-4-4 12:23:26 ",38 );
		PostList list=new PostList();
		list.push(post1);
		list.push(post2);
		
		try{
			Class<?> pe=Class.forName(classpath);
			Constructor<?> cst=pe.getDeclaredConstructor(PostList.class);
			PostExtractor pe_obj=(PostExtractor)cst.newInstance(list);
			
			Method mt_er=pe.getDeclaredMethod("extractContent");
			mt_er.setAccessible(true);
			
			Field pd=pe.getDeclaredField("_post");
			pd.setAccessible(true);
			
			while(!list.isEmpty()){
				pe_obj.getPost();
				mt_er.invoke(pe_obj);
				Post post=(Post)pd.get(pe_obj);
				System.out.println(post.text());
				Assert.assertNotNull(post.getContent());
			}
			
		}catch(Exception e){
			e.printStackTrace();
			fail();
			}
		
	}

	
}
