package cn.edu.uestc.wsc.bbs;

/*
 * interface for making URL*/
public interface URLMaker {
	String createPostURL(String id,int page);
	String createPostlistURL(int blockId,int page);
}
