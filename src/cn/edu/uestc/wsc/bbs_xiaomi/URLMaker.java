package cn.edu.uestc.wsc.bbs_xiaomi;

/*
 * 根据参数生成URL
 **/
public class URLMaker {
	
	/* 
	 * @function 生成指定页码的帖子列表的URL
	 * @param
	 * pageNum   页码*/
	public static String getURLofPostList(int pageNum){
		return "http://bbs.xiaomi.cn/index-"+pageNum+".html";
	}
	
	/*
	 * @function 根据帖子id号以及页码，生成帖子的URL
	 * @param
	 * postId  帖子的id
	 * pageNum 帖子页码*/
	public static String getURLofPost(String postId,int pageNum){
		return "http://bbs.xiaomi.cn/forum.php?mod=viewthread&tid="+postId+"&page="+pageNum+"&mobile=no";
	}
}