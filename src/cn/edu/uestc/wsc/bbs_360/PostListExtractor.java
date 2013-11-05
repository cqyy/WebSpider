package cn.edu.uestc.wsc.bbs_360;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.uestc.RegexTool.RegexTool;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBS360Cache;
import cn.edu.uestc.wsc.bbs.AbstractPostlistExtractor;

/*
 * 复杂提取帖子列表*/
public class PostListExtractor extends AbstractPostlistExtractor {

	
	public PostListExtractor(PostList list){
		super(list);
	}


	
	@Override
	protected int getPages(int block) throws IOException{
		Document doc=Jsoup.parse(
				_connect.getHtmlByUrl(
						_urlMaker.createPostlistURL(block, 1)));
		if(doc.select(".pg label span").first()==null){
			return 1;
		}
		String page=doc.select(".pg label span").first().text();
		page =RegexTool.subString(page, "\\d{1,4}");
		return Integer.parseInt(page);
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

			if(id==null){
				continue;
				}			//无效贴子，可能是公告
			
			id=RegexTool.subString(id, regex_id);

			if(id==null){continue;}			//无效帖子 ，可能是分隔栏
			
			 title=ele.select(".xst").first().text();
			 author=ele.select(".by cite").first().text();
			 time=ele.select(".by em span").first().text(); 
			 replies=Integer.parseInt(ele.select(".num a").first().text());
			 
			 post=new Post(id,title,author,time,replies);
			 
			 if(isNew(post)){
				// System.out.println(_list.size());
				_list.push(post);
				count++;
			 }
		}
		
		return count;
	}



	@Override
	protected void initCache() {
		super._cache=new BBS360Cache();
	}

	@Override
	protected void initBlockManager() {
		super._blockManager=new BlockManager360();
		
	}

	@Override
	protected void initURLMaker() {
		super._urlMaker=new URLMaker360();
		
	}

}
