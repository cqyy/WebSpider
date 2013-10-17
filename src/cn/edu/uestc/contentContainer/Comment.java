package cn.edu.uestc.contentContainer;

/*this class is used to store comment 
 * id  */
public class Comment {
private String writer;
private String time;
private String content;

public Comment(){}

/* @param writer:贴主
 * @param time:回帖时间
 * @param content:帖子内容
 * */
public Comment(String writer,String time,String content){
	this.writer=writer;
	this.time=time;
	this.content=content;
}

public String text(){
	return (writer+","+time+","+content);
}

public String getWriter() {
	return writer;
}
public void setWriter(String writer) {
	this.writer = writer;
}


public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}

public String getTime() {
	return time;
}

public void setTime(String time) {
	this.time = time;
}

}
