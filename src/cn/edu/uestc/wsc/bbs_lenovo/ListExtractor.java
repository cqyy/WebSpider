package cn.edu.uestc.wsc.bbs_lenovo;

import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSlenCache;
import cn.edu.uestc.wsc.bbs.PostlistExtractor;

public class ListExtractor extends PostlistExtractor{
	private int _block;
	private int _pages=0;
	
	public ListExtractor(PostList list){
		super(list);
		_cache=new BBSlenCache();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int extractList(Document doc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void extract(int block, int page) {
		// TODO Auto-generated method stub
		
	};

	private int getPages(int block){
		// TODO Auto-generated method stub
		return 0;
	};
}
