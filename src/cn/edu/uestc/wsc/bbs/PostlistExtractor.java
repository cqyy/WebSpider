package cn.edu.uestc.wsc.bbs;

import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

/*
 * 帖子列表提取器抽象类*/
public abstract class PostlistExtractor implements Runnable{
	protected PostList _list=null;
	protected HtmlConnect _connect=null;
	protected int _startPage=1;
	protected Cache _cache=null;
	
	public PostlistExtractor(PostList list){
		_list=list;
		_connect=new HtmlConnect();
	};
	

	/*
	 * @function 提取一页的帖子
	 * @param doc:帖子列表页面的DOM对象
	 * @return 提取到的帖子数量
	 * */
	protected abstract int extractList(Document doc);
	 
	/*
	 * @function 根据redis记录，判断该post是否有新的回复或者是新的
	 * 如果redis中没有记录或者记录中的回复数与当前回复数不一致，则热土让你 true
	 * */
	protected  boolean isNew(Post post){
		int value=_cache.getValue(post.getId());
		
		if(value!=post.getCommentNum()){
			return true;
		}
		return false;
	};

	/*
	 * @function 判断是否为列表页面的最后一页
	 * @param doc:页面DOM对象
	 * return:true 该页面是最后一页，false 不是最后一页
	 * */
	protected abstract boolean isLastPage(Document doc);
	
	/*
	 * @function 提取主控方法，控制页码*/
	public abstract void extract();
	
}
