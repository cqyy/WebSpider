package cn.edu.uestc.wsc.bbs_anzhi;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.uestc.RegexTool.RegexTool;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSanzhiCache;
import cn.edu.uestc.wsc.bbs.AbstractPostlistExtractor;

public class PostListExtractor extends AbstractPostlistExtractor {

	public PostListExtractor(PostList list) {
		super(list);
	}

	@Override
	protected int getPages(int blockId) throws IOException {
		Document doc=Jsoup.parse(
				_connect.getHtmlByUrl(
						_urlMaker.createPostlistURL(blockId, 1)));
		Element ele=doc.select(".pg").first();
		//只有一页，没有页码导航
		if(ele==null)
			return 1;
		ele=ele.children().get(ele.children().size()-2);
		return Integer.parseInt(ele.text().replace("...", "").trim());
	}

	@Override
	protected int extractList(Document doc) {
		int count=0;
		String regex_id="\\d{4,8}"; //提取id的正则表达式
		Elements eles=doc.select("#moderate table").first().children();
		Element ele=null;
		String id=null;
		String title=null;
		String author=null;
		String time=null;
		int replies=0;
		Post post=null;
		/*******提取帖子列表*********/
		for(int i=0;i<eles.size();i++){
			ele=eles.get(i);
			id=ele.attr("id");

			if(id==null){continue;}			//无效贴子，可能是公告
			
			id=RegexTool.subString(id, regex_id);

			if(id==null){continue;}			//无效帖子 ，可能是分隔栏
			
			 title=ele.select(".xst").first().text();
			 author=ele.select(".by cite").first().text();
			 time=ele.select(".by em span").first().attr("title"); 
			 replies=Integer.parseInt(ele.select(".num a").first().text());
			 
			 post=new Post(id,title,author,time,replies);
			
			 if(isNew(post)){
				_list.push(post);
				count++;
			 }
		}
		
		return count;
	}

	@Override
	protected void initCache() {
		super._cache=new BBSanzhiCache();
	}

	@Override
	protected void initBlockManager() {
		super._blockManager=new BlockManageranzhi();
	}

	@Override
	protected void initURLMaker() {
		super._urlMaker=new URLMakeranzhi();
	}



}
