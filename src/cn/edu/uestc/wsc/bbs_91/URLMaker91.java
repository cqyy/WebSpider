package cn.edu.uestc.wsc.bbs_91;

import cn.edu.uestc.wsc.bbs.URLMaker;

public class URLMaker91 implements URLMaker {

	@Override
	public String createPostURL(String id, int page) {
		return "http://ibbs.91.com/forum.php?mod=viewthread&tid="+id+"&page="+page+"&mobile=no";
	}

	@Override
	public String createPostlistURL(int blockId, int page) {
		return "http://ibbs.91.com/forum.php?mod=forumdisplay&fid="+blockId+"&page="+page+"&mobile=no";
	}

}
