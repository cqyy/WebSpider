package cn.edu.uestc.wsc.bbs_xiaomi;

import cn.edu.uestc.contentContainer.PostList;

/*
 * @function 爬虫的主控类
 * 不断提取postlist，并接下来提取list中所有post内容，之后提取新的postist，循环知道最后一页
 * @date 2013年10月6日 20时11分03秒
 * */
public class Main {

	public static void main(String[] args) {
		final int ThreadNum = 6;

		PostList list = new PostList();
		PostListExtractor pe=new PostListExtractor(list);
		
		Thread[] th = new Thread[ThreadNum];
		th[0] = new Thread(pe);
		th[0].start();

		for (int i = 1; i <= ThreadNum - 1; i++) {
			th[i] = new Thread(new PostExtractor(list));
			th[i].start();
		}

		/* 循环检测系统状态 */
		while (true) {
			//列表线程检测
			//System.out.print("--" + th[0].getState().toString());
			if(!th[0].isAlive()){
				th[0]=new Thread(pe);
				th[0].start();
				}
			//帖子爬取线程检测
			for (int i = 1; i < ThreadNum; i++) {
			//	System.out.print("--" + th[i].getState().toString());
				if(!th[i].isAlive()){
					th[i]=new Thread(new PostExtractor(list));
					th[i].start();
				}
			}
			//System.out.println("--size:" + list.size());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}
}
