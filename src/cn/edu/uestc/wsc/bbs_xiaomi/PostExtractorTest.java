package cn.edu.uestc.wsc.bbs_xiaomi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PostExtractorTest extends TestCase {
	
	public PostExtractorTest(String method){
		super(method);
	}
	
	protected void setUp(){}
	
	protected void tearDown(){}
	
	public void testGetRepliesNumOfFirstPage(){ 
		PostList list=new PostList();
		String[] id=new String[]{"361525","420883","360230","479987","470110","315477"};
		int[] num=new int[]{24,25,24,24,24,27};
	
		for(int i=0;i<id.length;i++){
			Post post=new Post();
			post.setId(id[i]);
			list.push(post);
		}
		
		try{
			//获取目标类的对象
			Class<PostExtractor> ex=PostExtractor.class;
	
			//获取构造函数
			Constructor<PostExtractor> ctr=ex.getConstructor(PostList.class);	
	
			//获取对象
			Object obj=ctr.newInstance(list);
	
			//获取待测试的方法
			Method mtR=ex.getDeclaredMethod("getRepliesNumOfFirstPage");
			Method mtP=ex.getDeclaredMethod("getPost");
	
			//设置方法访问权限
			mtP.setAccessible(true);
			mtR.setAccessible(true);
	
			for(int i=0;i<id.length;i++){
				mtP.invoke(obj);
				Object result=mtR.invoke(obj);
			//	System.out.println(result);
				Assert.assertEquals(num[i], result);
			}
	
		}catch(Exception e){
			e.printStackTrace();
			}
	}

	public void testExtractReplies(){
		PostList list=new PostList();
		String[] id=new String[]{"361525","420883","360230","479987","470110","315477"};
		int[] pages=new int[]{1,2,153,1,1,1};
		int[] floors=new int[]{1,2,1,1,24,1};
		int[] num=new int[]{24,24,18,24,1,27};
	
		for(int i=0;i<id.length;i++){
			Post post=new Post();
			post.setId(id[i]);
			list.push(post);
		}
		
		try {
			//获取目标类对象
			Class<?> pe = Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor");
			//获取构造函数
			Constructor<?> cst=pe.getConstructor(PostList.class);
			//获取目标对象实例
			Object obj_pe=cst.newInstance(list);
			//获取待测试方法
			Class<?>[] arg=new Class[]{int.class ,int.class};
			Method meE=pe.getDeclaredMethod("extractReplies",arg);
			Method meP=pe.getDeclaredMethod("getPost");
			Field fd_post=pe.getDeclaredField("post");
			
			//设置访问权限
			meE.setAccessible(true);
			meP.setAccessible(true);
			fd_post.setAccessible(true);
			
			
			for(int i=0;i<num.length;i++){
				meP.invoke(obj_pe);
				Object result=meE.invoke(obj_pe,pages[i],floors[i]);
				//测试提取数量是否正确
				Assert.assertEquals(num[i], result);
			}
		} catch (ClassNotFoundException e) {
			fail("class not found");
		} catch (InstantiationException e) {
			fail("InstantiationException");
		} catch (IllegalAccessException e) {
			fail("IllegalAccessException");
		} catch (NoSuchMethodException e) {
			fail("NoSuchMethodException");
		} catch (SecurityException e) {
			fail("SecurityException");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException");
		} catch (InvocationTargetException e) {
			fail("InvocationTargetException");
		} catch (NoSuchFieldException e) {
			fail("NoSuchFieldException");
		}		
	}

	public void testExtractContent(){
		PostList list=new PostList();
		String[] id=new String[]{"361525","420883","360230","479987","470110","315477"};	
		for(int i=0;i<id.length;i++){
			Post post=new Post();
			post.setId(id[i]);
			list.push(post);
		}
		
		try{
			Class<?> pe=Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor");
			Constructor<?> cst=pe.getConstructor(PostList.class);
			Object obj=cst.newInstance(list);
			Method meR=pe.getDeclaredMethod("extractContent");
			Method meG=pe.getDeclaredMethod("getPost");
			
			Field pd=pe.getDeclaredField("post");
			pd.setAccessible(true);
			
			meR.setAccessible(true);
			meG.setAccessible(true);
			
			for(int i=0;i<id.length;i++){
				meG.invoke(obj);
				meR.invoke(obj);
				Post post=(Post)pd.get(obj);
				Assert.assertNotNull(post.getContent());
			}
		} catch (ClassNotFoundException e) {
			fail("class not found");
		} catch (InstantiationException e) {
			fail("InstantiationException");
		} catch (IllegalAccessException e) {
			fail("IllegalAccessException");
		} catch (NoSuchMethodException e) {
			fail("NoSuchMethodException");
		} catch (SecurityException e) {
			fail("SecurityException");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException");
		} catch (InvocationTargetException e) {
			fail("InvocationTargetException");
		} catch (NoSuchFieldException e) {
			fail("NoSuchFieldException");
		}

	}
	
	public void testGetPages(){
		int[] num=new int[]{24,26,50,50,80,1000,1747,673,77,794};
		int[] fnum=new int[]{24,26,25,24,24,26,24,24,24,24};
		int[] expected=new int[]{1,1,2,3,4,40,70,27,4,32};
		
		try {
			Class<?> pe=Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor");
			Constructor<?> cst=pe.getConstructor(PostList.class);
			PostList list=new PostList();
			PostExtractor pe_obj=(PostExtractor)cst.newInstance(list);
			Method mt=pe.getDeclaredMethod("getPages",new Class<?>[]{int.class,int.class});
			mt.setAccessible(true);
			
			for(int i=0;i<num.length;i++){
				int result=(int)mt.invoke(pe_obj, num[i],fnum[i]);
				System.out.println(result+"---"+expected[i]);
				Assert.assertEquals(expected[i], result);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testGetStartPage(){
		int[] dnum=new int[]{12,26,20,100,48,1000};
		int[] fnum=new int[]{24,26,25,24,24,26};
		int[] expected=new int[]{1,2,1,5,2,40};
		try{
			Class<?> pe=Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor");
			Constructor<?> cst=pe.getConstructor(PostList.class);
			PostList list=new PostList();
			PostExtractor pe_obj=(PostExtractor)cst.newInstance(list);
			Method mt=pe.getDeclaredMethod("getStartPage",new Class<?>[]{int.class,int.class});
			mt.setAccessible(true);
			
			for(int i=0;i<dnum.length;i++){
				int result=(int)mt.invoke(pe_obj, dnum[i],fnum[i]);
				System.out.println(expected[i]+":"+result);
				Assert.assertEquals(expected[i], result);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetStartFloor(){
		int[] num=new int[]{24,26,50,50,80,1000};
		int[] fnum=new int[]{23,25,25,24,24,26};
		int[] expected=new int[]{2,2,1,2,7,25};
		try{
			Class<?> pe=Class.forName("cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor");
			Constructor<?> cst=pe.getConstructor(PostList.class);
			PostList list=new PostList();
			PostExtractor pe_obj=(PostExtractor)cst.newInstance(list);
			Method mt=pe.getDeclaredMethod("getStartFloor",new Class<?>[]{int.class,int.class});
			mt.setAccessible(true);
			
			for(int i=0;i<num.length;i++){
				int result=(int)mt.invoke(pe_obj, num[i],fnum[i]);
				Assert.assertEquals(expected[i], result);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
