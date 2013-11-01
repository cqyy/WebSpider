package cn.edu.uestc.wsc.bbs_lenovo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.uestc.RegexTool.RegexTool;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSlenCache;
import cn.edu.uestc.wsc.bbs.AbstractPostlistExtractor;

public class PostListExtractor extends AbstractPostlistExtractor{
	
	public PostListExtractor(PostList list){
		super(list);

	}


	@Override
	protected int extractList(Document doc) {
		int count=0;
		String regex_id="\\d{2,8}"; //提取id的正则表达式
		Elements eles=doc.select("#threadlisttableid").first().children();     
		
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
			
			if(ele.select(".common a+a+a").first()!=null){
				title=ele.select(".common a+a+a").first().text();
			}else{
				title=ele.select(".common a+a").first().text();
			}
			
			 author=ele.select(".by cite a").first().text();
			 //time=ele.select(".by em span").first().attr("title"); 
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
	protected int getPages(int blockId) throws IOException {
		int pages=0;
		Document doc=Jsoup.parse(
				_connect.getHtmlByUrl(
						_urlMaker.createPostlistURL(blockId, 1)));
		Element ele=doc.select(".pg").first();
		if(ele==null){
			//System.out.println("pages=1");
			pages=1;
			}else{
				pages=Integer.valueOf(ele.children().get(ele.children().size()-2).text().replace("/", "").replace("页", "").trim());
			}
		return pages;
	}


	@Override
	protected void initCache() {
		_cache=new BBSlenCache();
		
	}


	@Override
	protected void initBlockManager() {
		super._blockManager=new BlockManagerLenovo();
		
	}


	@Override
	protected void initURLMaker() {
		super._urlMaker=new URLMakerLenovo();
		
	};


}
