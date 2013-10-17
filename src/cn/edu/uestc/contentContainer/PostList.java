package cn.edu.uestc.contentContainer;

import java.util.LinkedList;
import java.util.Queue;


public class PostList{
	private  Queue<Post> list=new LinkedList<Post>();
		
	public  boolean isEmpty(){
		return list.isEmpty();
	}
	
	
	/*互斥操作*/
	public synchronized void push(Post post){
		if(post==null)
			return ;
		
		list.offer(post);
		this.notify();      //通知等待该资源的一个线程
	}
	
	public synchronized Post pop(){
		if(this.list.isEmpty()){
			try {
				wait();  //队列为空，阻塞等待获取队列资源的线程
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return list.poll();
	}

	public  int size(){
		return list.size();
	}
	
}
