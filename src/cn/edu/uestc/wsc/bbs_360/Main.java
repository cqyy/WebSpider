package cn.edu.uestc.wsc.bbs_360;

import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.FileOut.FileOut;
import cn.edu.uestc.wsc.bbs_xiaomi.PostExtractor;

public class Main {
	public static void main(String[] args){
		final int ThreadNum = 6;
		FileOut.setSavePath("../360BBS/");
		PostList list = new PostList();
		ListExtractor360 pe=new ListExtractor360(list);
		
		Thread[] th = new Thread[ThreadNum];
		th[0] = new Thread(pe);
		th[0].start();

		for (int i = 1; i <= ThreadNum - 1; i++) {
			th[i] = new Thread(new PostExtractor360(list));
			th[i].start();
		}

		/* 循环检测系统状态 */
		while (true) {
			//列表线程检测
			System.out.print("--" + th[0].getState().toString());
			if(!th[0].isAlive()){
				th[0]=new Thread(pe);
				th[0].start();
				}
			//帖子爬取线程检测
			for (int i = 1; i < ThreadNum; i++) {
				System.out.print("--" + th[i].getState().toString());
				if(!th[i].isAlive()){
					th[i]=new Thread(new PostExtractor(list));
					th[i].start();
				}
			}
			System.out.println("--size:" + list.size());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

}
