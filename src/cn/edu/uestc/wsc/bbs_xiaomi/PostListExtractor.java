package cn.edu.uestc.wsc.bbs_xiaomi;

import java.io.IOException;

import org.apache.http.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.uestc.RegexTool.RegexTool;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSxmCache;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;


//Extract the list of the post
class PostListExtractor implements Runnable{
	private Cache cache=null;
	private PostList list=null;
	private HtmlConnect connect=null;
	private int startPage=1;
	
	public PostListExtractor(PostList list){
		this.cache=new BBSxmCache();
		this.list=list;
		connect=new HtmlConnect();
	}
	
	public PostListExtractor(PostList list,int startPage){
		this.cache=new BBSxmCache();
		this.list=list;
		connect=new HtmlConnect();
		this.startPage=startPage;
	}

	/*
	 * @function 提取一页的帖子
	 * @param doc:帖子列表页面的DOM对象
	 * @return 提取到的帖子数量
	 * */
	 private int extractPage(Document doc) throws ParseException, IOException{
		int count=0;
		/*doc=Jsoup.parse(
				connect.getHtmlByUrl(
						URLMaker.getURLofPostList(startPage)));
		*/
		Element ele_listDiv=doc.select(".listDiv").first();

		Elements eles_info=ele_listDiv.select(".listInfo");
		Elements eles_replies=ele_listDiv.select(".ui-li-count");
		Elements eles_title=ele_listDiv.select(".listInfo+h4");
		Elements eles_id=ele_listDiv.select("li a"); 	
		
		for(int i=0;i<eles_info.size();i++){
			String[] authorAndTime=eles_info.get(i).text().split("/");
			String id=RegexTool.subString(eles_id.get(i).attr("href"),"\\d{5,8}");
			
			Post post=new Post(
					id,
					eles_title.get(i).ownText(),
					authorAndTime[0].trim(),
					authorAndTime[1].trim(),
					Integer.parseInt(eles_replies.get(i).text().split(" ")[1]));
			
			if(this.isNew(post)){
				this.list.push(post);
				count++;
			}
		}
		return count;
	}
	
	/*
	 * @function 根据redis记录，判断该post是否有新的回复或者是新的
	 * 如果redis中没有记录或者记录中的回复数与当前回复数不一致，则热土让你 true*/
	protected boolean isNew(Post post){
		int value=cache.getValue(post.getId());
		
		if(value!=post.getCommentNum()){
			return true;
		}
		return false;
	}

	/*
	 * @function 判断是否为列表页面的最后一页
	 * @param doc:页面DOM对象
	 * return:true 该页面是最后一页，false 不是最后一页
	 * */
	private boolean isLastPage(Document doc){
		Element listDiv=doc.select(".listDiv").first();
		if(listDiv==null){
			throw new NullPointerException();
		}
		if(listDiv.children().size()<1){
			return true;
		}
		return false;
	}
	
	/*
	 * @function 提取主控方法，控制页码*/
	public void extract(){
		//System.out.println("pages:"+startPage);
		
		try {
			Document doc=Jsoup.parse(
					connect.getHtmlByUrl(
							URLMaker.getURLofPostList(startPage)));
			if(this.isLastPage(doc)){
				//最后一页
				startPage=1;
				return;
			}
			this.extractPage(doc);
			startPage++;
		} catch (ParseException | IOException e) {
			//String log="exception int PostListExtractor----page:"+startPage+"  exception: "+e.toString();
			//Logger.writeLog(log);
		}
	};
	@Override
	public void run() {
		while(true){
			while(list.size()<60){
				this.extract();
			}
			try {			
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
	}
}
