package cn.edu.uestc.wsc.bbs;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

public abstract class PostExtractor {
	protected PostList _list=null;
	protected HtmlConnect _con;
	protected Cache _cache=null;
	protected int _repliesNumOfPage; // 除了第一页之外，每页显示的评论数量
	protected Post _post = null;
	
	public PostExtractor(PostList list){
		_list=list;
		_con=new HtmlConnect();
	}
	
	/*
	 * @function 主控方法，控制提取帖子内容以及回复
	 * */
	public abstract void extrac();
	

	/*
	 * @function 提取帖子回复
	 * @param page:提取的页码
	 * @param floor:提取的开始楼数,1楼开始
	 * @return 提取的回复数量
	 * */
	protected abstract int extractReplies(int page,int floor);
	
	
	/* 
	 * @function 提取帖子内容
	 * */
	protected abstract void extractContent () ;
	
	/*
	 * @function 从list中获取一个post
	 * */
	public void getPost(){
		_post=_list.pop();
	}
	
	/*
	 * @function 获取总页数
	 * @param repliesNum:回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 * */
	protected  int getPages(int repliesNum,int repliesNumOfFirstPage){
		return ((int) Math.ceil((double) (repliesNum - repliesNumOfFirstPage) 
				/ _repliesNumOfPage) + 1);
	};
	
	/*
	 * @function 获取第一个爬取的页面，根据以前的爬取历史进行计算
	 * @param reoliesDealed: 历史爬取记录，本次以前已经爬取的回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 *  */
	protected int getStartPage(int repliesDealed,int repliesNumOfFirstPage){
		return ((int) Math
				.ceil((double) (repliesDealed - repliesNumOfFirstPage+1)
						/ _repliesNumOfPage) + 1);
	}
	
	/*
	 * @function 獲取开始楼层
	 * @param repliesNum： 回复总数量
	 * @param repliesNumOfFirstPage:第一页的回复数量*/
	protected int getStartFloor(int repliesNum,int repliesNumOfFirstPage){
		return (repliesNum-repliesNumOfFirstPage)%_repliesNumOfPage+1;
	}

	
	
}
