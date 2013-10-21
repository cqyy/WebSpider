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
import cn.edu.uestc.wsc.bbs.PostlistExtractor;

/*
 * 复杂提取帖子列表*/
public class ListExtractor360 extends PostlistExtractor {
	private int _block;
	private int _pages=0;
	
	public ListExtractor360(PostList list){
		super(list);
		_cache=new BBS360Cache();
	}

	@Override
	public void run() {
		while(true){
			try {
				_block=BlockManger.next();
				_pages=this.getPage(_block);
				_startPage=1;
				while(_startPage<=_pages){
					this.extract(_block,_startPage);
					_startPage++;
					/*控制列表长度，使其不至于太长*/
					while(_list.size()>60){
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void extract(int block,int page) {
		try {
			Document doc=Jsoup.parse(
					_connect.getHtmlByUrl(
							URLMaker.createPostlistUrl(block, page)));
			extractList(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * @function 提取该版块的列表总页面*/
	private int getPage(int block) throws IOException{
		Document doc=Jsoup.parse(
				_connect.getHtmlByUrl(
						URLMaker.createPostlistUrl(block, 1)));
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

}
