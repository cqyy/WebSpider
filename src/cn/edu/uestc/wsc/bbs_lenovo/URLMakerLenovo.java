package cn.edu.uestc.wsc.bbs_lenovo;

import cn.edu.uestc.wsc.bbs.URLMaker;

/*
 * @function 网页URL生成
 * */
public class URLMakerLenovo implements URLMaker {

	@Override
	public String createPostURL(String id, int page) {
		return "http://lenovobbs.lenovo.com.cn/forum.php?mod=viewthread&tid="+id+"&extra=page%3D1&page="+page+"&mobile=no";
	}


	@Override
	public String createPostlistURL(int blockId, int page) {
		return "http://lenovobbs.lenovo.com.cn/forum.php?mod=forumdisplay&fid="+blockId+"&page="+page+"&mobile=no";
	}
}
