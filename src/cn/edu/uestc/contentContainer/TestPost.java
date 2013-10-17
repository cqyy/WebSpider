package cn.edu.uestc.contentContainer;

import junit.framework.TestCase;

public class TestPost extends TestCase {
	public void testAddComent(){
		int[] num=new int[]{100,1000,10000,100000};
		Post post=new Post();
		post.setId("12345678");
		post.setContent("content");
		post.setTime("2013-9-8 22:22:22");
		post.setWriter("author");
		long st=System.currentTimeMillis();
		
		for(int i=0;i<num.length;i++){
			for(int j=0;j<num[i];j++){
				post.addComment(new Comment("author","2013-9-8 22:22:22","contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent"));
			}
			long et=System.currentTimeMillis();
			System.out.println("time of addComent(): "+num[i]+":"+(float)(et-st)/num[i]);
			post.text();
			long et2=System.currentTimeMillis();
			System.out.println("time of text(): "+num[i]+":"+(float)(et2-et)/num[i]);
			
		}
	};
}
