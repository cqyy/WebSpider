package cn.edu.uestc.contentContainer;

import java.util.LinkedList;
import java.util.Queue;

/*this is a class for Post*/
public class Post {

	private String id;
	private String title;
	private String writer;
	private String time;
	private int commentNum; 
	private String content;
	private boolean isNew=true;//是否是新帖子，用以决定是否填充content以及到text()方法
	private Queue<Comment> commentQueue;
	
	public Post(){
		this.commentQueue=new LinkedList<Comment>();
	}
	/*@param
	 * @id id
	 * @title title
	 * @writer writer
	 * @time time
	 * @num num*/
	public Post(String id,String title,String writer,String time,int num){
		this.id=id;
		this.time=time;
		this.setCommentNum(num);
		this.title=title;
		this.writer=writer;
		this.commentQueue=new LinkedList<Comment>();
	}
	
	public boolean addComment(Comment comment){
		return this.commentQueue.offer(comment);
	}
	
	//clear all the comment
	public void clearComment(){
		this.commentQueue.clear();
	}
	
	public String text(){
		StringBuffer sb=new StringBuffer();
		sb.append(id);
		//System.out.print("post.isNew?"+this.isNew);
		if(isNew){
			sb.append(",").append(title).append(",").append(writer).
			append(",").append(time).append(",").append(content);
			}
		while(!this.commentQueue.isEmpty()){
			sb.append("|").append(this.commentQueue.poll().text());
		}
		return sb.toString();
	}
	
	public String getId(){
		return this.id;
	}	
	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}


	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isNew(){
		return this.isNew;
	}
	
	public void setNew(){
		this.isNew=true;
	}
	
	public void setNotNew(){
		this.isNew=false;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

}
