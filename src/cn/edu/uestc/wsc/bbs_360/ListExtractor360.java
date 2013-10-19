package cn.edu.uestc.wsc.bbs_360;

import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBS360Cache;
import cn.edu.uestc.wsc.bbs.PostlistExtractor;

/*
 * 复杂提取帖子列表*/
public class ListExtractor360 extends PostlistExtractor {
	
	
	public ListExtractor360(PostList list) {
		super(list);
		_cache=new BBS360Cache();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isNew(Post post) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isLastPage(Document doc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void extract() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int extractList(Document doc) {
		// TODO Auto-generated method stub
		return 0;
	}

}
