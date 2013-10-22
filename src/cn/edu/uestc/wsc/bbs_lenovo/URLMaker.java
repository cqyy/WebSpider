package cn.edu.uestc.wsc.bbs_lenovo;

/*
 * @function 网页URL生成
 * */
public class URLMaker {
	/*
	 * @funciton 生成相应页面的帖子列表url
	 * @param blockId 论坛版块id
	 * @param page 帖子列表页码*/
	public final static String createPostlistUrl(int blockId, int page){
		return "http://lenovobbs.lenovo.com.cn/forum.php?mod=forumdisplay&fid="+blockId+"&page="+page+"&mobile=no";
	}
	
	/*
	 * @function 生成帖子url
	 * @param postId  帖子id
	 * @param page 帖子页码*/
	public final static String createPostUrl(String postId,int page){
		return "http://lenovobbs.lenovo.com.cn/forum.php?mod=viewthread&tid="+postId+"&extra=page%3D1&page="+page+"&mobile=no";
	}
}
