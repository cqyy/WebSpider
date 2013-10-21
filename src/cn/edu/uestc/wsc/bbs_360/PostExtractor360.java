package cn.edu.uestc.wsc.bbs_360;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.uestc.contentContainer.Comment;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBS360Cache;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.FileOut.FileOut;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

public class PostExtractor360 implements Runnable{
	protected PostList _list=null;
	protected HtmlConnect _con;
	protected Cache _cache=null;
	protected final int _repliesNumOfPage=10; // 除了第一页之外，每页显示的评论数量
	protected final int _repliesNumOfFirstPage = 9; //第一页回复数量
	protected Post _post = null;
	
	
	public PostExtractor360(PostList list) {
		_list=list;
		_con=new HtmlConnect();
		_cache=new BBS360Cache();
	}


	public void extrac() {
		int repliesExtracted=0;//本次提取了的回复数量
		int startPage = 1; // 本次提取的开始页码
		int startFloor = 1; // 本次提取的开始楼层

		int repliesNum=_post.getCommentNum();              //回复总数量
		int repliesDealed=_cache.getValue(_post.getId());;  //上次已经提取的回复数量 -1表示这是一个新帖子
		//帖子总页数
		int pages =this.getPages(repliesNum);
		
		try{				
			//新帖子
			if(repliesDealed==-1){
				extractContent();
			}else{
				_post.setNotNew();
				//初始开始提取的页码和开始的楼层
				if (repliesDealed > _repliesNumOfFirstPage) {
					startPage = this.getStartPage(repliesDealed);
					startFloor = this.getStartFloor(repliesNum);
				}
			}
			
			//String log="thread: "+this.hashCode()+"  id:"+_post.getId()+"  pages:"+pages+"  startPage:"+startPage+"  startFloor:"+startFloor+"  repliesNum:"+repliesNum;
			//Logger.writeLog(log);
			//System.out.println(log);
			
			//提取回复
			for(int i=startPage;i<=pages;i++){
				repliesExtracted+=extractReplies(i,startFloor);
				startFloor=1; //开始楼层只对第一个提取页面有效
			}

		//	endTime=System.currentTimeMillis();			
		}catch(IOException e){
		}catch(Exception e){
		}finally{
			if(repliesExtracted>0){
				FileOut.writeLine(_post.text());
				//更新缓存
				_cache.setValue(_post.getId(), repliesExtracted+repliesDealed);
				_post=null;
			}
		}
	}

	/*
	 * @function 获取总页数
	 * @param repliesNum:回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 * */
	private int getPages(int repliesNum){
		if(repliesNum<_repliesNumOfFirstPage){
			return 1;
			}else{
				return ((int) Math.ceil((double) (repliesNum - _repliesNumOfFirstPage) / _repliesNumOfPage) + 1);
				}
	}
	
	/*
	 * @function 获取第一个爬取的页面，根据以前的爬取历史进行计算
	 * @param reoliesDealed: 历史爬取记录，本次以前已经爬取的回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 *  */
	private int getStartPage(int repliesDealed){
		return ((int) Math
				.ceil((double) (repliesDealed - _repliesNumOfFirstPage+1)
						/ _repliesNumOfPage) + 1);
	}
	
	/*
	 * @function 獲取开始楼层
	 * @param repliesNum： 回复总数量
	 * @param repliesNumOfFirstPage:第一页的回复数量*/
	private int getStartFloor(int repliesNum){
		return (repliesNum-_repliesNumOfFirstPage)%_repliesNumOfPage+1;
	}
	
	protected int extractReplies(int page, int floor) throws IOException {
		int count =0;
		Document doc=Jsoup.parse(
				_con.getHtmlByUrl(
						URLMaker.createPostUrl(_post.getId(), page)));
		Element rp_div=doc.select("#postlist").first();
		if(page==1&&floor==1){floor=2;}//跳过内容
		
		Element ele=null;
		for(int i=floor+1;i<rp_div.children().size()-1;i++){
			ele=rp_div.children().get(i);
			
			String author=ele.select(".xw1").first().text();
			String content=ele.select(".t_f").first().text();
			String time="";
			if(ele.select(".pti div em span").first()!=null){
				time=ele.select(".pti div em span").first().attr("title");
			}else{
				time=ele.select(".pti div em").first().text().replace("发表于", "").trim();
			}
			
			_post.addComment(new Comment(author,time ,content));
			count++;
		}
		
		return count;
	}


	protected void extractContent() throws IOException {
		Document doc=Jsoup.parse(
				_con.getHtmlByUrl(
						URLMaker.createPostUrl(_post.getId(), 1)));
		Element ele=doc.select("#postlist").first().children().get(2).select(".t_f").first();
		_post.setContent(ele.text());
		
	}

	/*
	 * @function 从list中获取一个post
	 * */
	public void getPost(){
		_post=_list.pop();
	}
	
	
	@Override
	public void run() {
		while (true) {
			this.getPost();
		    this.extrac();
		}
	}

}
