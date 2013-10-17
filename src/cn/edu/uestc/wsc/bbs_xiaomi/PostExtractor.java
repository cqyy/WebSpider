package cn.edu.uestc.wsc.bbs_xiaomi;

import java.io.IOException;
import org.apache.http.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.uestc.Log.Logger;
import cn.edu.uestc.RegexTool.RegexTool;
import cn.edu.uestc.contentContainer.Comment;
import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBSxmCache;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.FileOut.FileOut;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

/*@function 提取一个帖子尚未提取的内容
 * 若帖子是新帖子，则需要提取帖子内容以及所有回复
 * 若帖子不是新帖子，则只需提取从上次提取以来新的回复
 * 根据redis是否有该帖子的记录判断是否是新帖子，若无记录则是新帖子*/
public class PostExtractor implements Runnable {
	private PostList list=null;
	private HtmlConnect htmlConnect;
	private Cache cache=null;
	private final int repliesNumOfPage = 25; // 除了第一页之外，每页显示的评论数量
	private Post post = null;	
	/* 
	 * @function构造函数 初始化容器，网络了解以及缓存工具 
	 * */
	public PostExtractor(PostList list) {
		this.list = list;
		htmlConnect = new HtmlConnect();
		cache = new BBSxmCache();
	}

	/*
	 * @function 主控方法，控制提取帖子内容以及回复
	 * */
	public void extrac(){
		int repliesExtracted=0;//本次提取了的回复数量
		int startPage = 1; // 本次提取的开始页码
		int startFloor = 1; // 本次提取的开始楼层
		int repliesNumOfFirstPage = 0; //第一页回复数量
		int repliesNum=post.getCommentNum();              //回复总数量
		int repliesDealed=cache.getValue(post.getId());;  //上次已经提取的回复数量 -1表示这是一个新帖子
		//帖子总页数
		int pages =1; 
		long startTime=System.currentTimeMillis();
		long endTime=0;
		long endTime2=0;
		
		try{		
			repliesNumOfFirstPage=this.getRepliesNumOfFirstPage();
			pages=this.getPages(repliesNum, repliesNumOfFirstPage);
				
			//新帖子
			if(repliesDealed==-1){
				extractContent();
			}else{
				post.setNotNew();
				//初始开始提取的页码和开始的楼层
				if (repliesDealed > repliesNumOfFirstPage) {
					startPage = this.getStartPage(repliesDealed, repliesNumOfFirstPage);
					startFloor = this.getStartFloor(repliesNum, repliesNumOfFirstPage);
				}
			}
			
			String log="id:"+post.getId()+"  pages:"+pages+"  repliesNum:"+repliesNum+"  startPage:"+startPage+"  startFloor:"+startFloor;
			System.out.println(log);
			
			//提取回复
			for(int i=startPage;i<=pages;i++){
				repliesExtracted+=extractReplies(i,startFloor);
				startFloor=1; //开始楼层只对第一个提取页面有效
			}

			endTime=System.currentTimeMillis();			
			//将提取时间大于2分钟的帖子信息进行记录
			//if((endTime-startTime)>2*60*1000){

			//	}
		}catch(IOException e){
			//String log="exception in PostExtractor---postID："+post.getId()+" exception inf:"+e.toString();
			//Logger.writeLog(log);
		}catch(Exception e){
			//String log="exception in PostExtractor---postID："+post.getId()+" exception inf:"+e.toString();
			//Logger.writeLog(log);
		}finally{
			if(repliesExtracted>0){
				FileOut.writeLine(post.text());
				//更新缓存
				cache.setValue(post.getId(), repliesExtracted+repliesDealed);
				endTime2=System.currentTimeMillis();
				String log="ID: "+post.getId()
						+"   rnum: "+String.format("%1$-8d", repliesExtracted)
						+"   time: "+String.format("%1$-7d",(endTime-startTime))
						+"   Wtime: "+String.format("%1$-6d",(endTime2-endTime))
						+"   AVGTime:  "+String.format("%1$-6.3f",(float)(endTime-startTime)/repliesExtracted);
				Logger.writeLog(log);
				post=null;
			}
		}
	}
	

	
	/*
	 * @function 获取总页数
	 * @param repliesNum:回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 * */
	private int getPages(int repliesNum,int repliesNumOfFirstPage){
		return ((int) Math.ceil((double) (repliesNum - repliesNumOfFirstPage) 
				/ repliesNumOfPage) + 1);
	}
	
	/*
	 * @function 获取第一个爬取的页面，根据以前的爬取历史进行计算
	 * @param reoliesDealed: 历史爬取记录，本次以前已经爬取的回复数量
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 *  */
	private int getStartPage(int repliesDealed,int repliesNumOfFirstPage){
		return ((int) Math
				.ceil((double) (repliesDealed - repliesNumOfFirstPage+1)
						/ repliesNumOfPage) + 1);
	}
	
	/*
	 * @function 獲取开始楼层
	 * @param repliesNum： 回复总数量
	 * @param repliesNumOfFirstPage:第一页的回复数量*/
	private int getStartFloor(int repliesNum,int repliesNumOfFirstPage){
		return (repliesNum-repliesNumOfFirstPage)%repliesNumOfPage+1;
	
		//repliesDealed - ((startPage - 2) * repliesNumOfPage + repliesNumOfFirstPage);
	}
	
	/*
	 * @function 提取帖子回复
	 * @param page:提取的页码
	 * @param floor:提取的开始楼数,1楼开始
	 * @return 提取的回复数量
	 * */
	private int extractReplies(int page,int floor) throws ParseException, IOException{
		int count=0;
		String time_regex = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";// 匹配时间的正则表达式
		Document doc=Jsoup.parse(
				htmlConnect.getHtmlByUrl(
						URLMaker.getURLofPost(post.getId(), page)));
		this.clearDOM(doc);
		
		Element ele_listDiv=doc.select("#postlist").first();
	
		//提取回复内容, 忽略第一个内容块，其为内容，忽略最后一个内容块，其不是回复
		for(int i=floor;i<ele_listDiv.children().size()-1;i++){
			String time="";
			Element eTime=ele_listDiv.children().get(i).select(".authortitle+em>span").first();
			if(eTime!=null){
				time=eTime.attr("title");
			}else{
				time=RegexTool.subString(ele_listDiv.children().get(i).select(".authortitle+em").text(),time_regex);
			}
			post.addComment(new Comment(
					ele_listDiv.children().get(i).select(".author").text(),
					time,
					ele_listDiv.children().get(i).select(".t_fsz").text()));
			count++;
		}
		return count;
	}
	
	/* 
	 * @function 提取帖子内容
	 * */
	private void extractContent() throws ParseException, IOException{
		Document doc=Jsoup.parse(
				htmlConnect.getHtmlByUrl(
						URLMaker.getURLofPost(post.getId(), 1)));
		this.clearDOM(doc);
		
		Element ele=doc.select(".t_f").first();
		post.setContent(ele.text());
		//设置时间
		ele=doc.select(".xm_p_tp").first().children().get(3).children().get(1);
		if(ele.children().size()>0){
			post.setTime(ele.children().first().attr("title"));
		}else{
			post.setTime(ele.text().replace("于 ",""));
		}
	}
	
	/*
	 * @function 去除页面中故意加入的干扰乱码
	 * */
	private void clearDOM(Document doc){
		if(doc==null)
			return;
		// 去除干扰项
		Elements eles = doc.select("[style=display:none],.jammer");
		for (Element e : eles) {
			e.remove();
		}
	}

	/* 获取帖子第一页显示的回复数量 */
	private int getRepliesNumOfFirstPage() throws ParseException, IOException {
		String url = URLMaker.getURLofPost(post.getId(), 1);
		Document doc = Jsoup.parse(htmlConnect.getHtmlByUrl(url));
		Element ele = doc.select("#postlist").first();
		return (ele.children().size() - 2); //div postist 第一个孩子为帖子内容，最后一个不是回复
	}

	/*
	 * @function 从list中获取一个post
	 * */
	public void getPost(){
		this.post=list.pop();
	}
	
	@Override
	public void run() {
		while (true) {
			this.getPost();
		    this.extrac();
		}

	}
}
