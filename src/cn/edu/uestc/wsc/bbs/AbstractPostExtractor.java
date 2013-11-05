package cn.edu.uestc.wsc.bbs;

import java.io.IOException;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.FileOut.FileOut;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

public abstract class AbstractPostExtractor implements Runnable {
	protected PostList _list = null;
	protected HtmlConnect _con;
	protected Cache _cache = null;
	protected Post _post = null;
	protected URLMaker _urlMaker = null;
	protected int _repliesNumOfPage; // 除了第一页之外，每页显示的评论数量
	protected int _repliesNumOfFirstPage; // 第一页回复数量

	public AbstractPostExtractor(PostList list) {
		_list = list;
		_con = new HtmlConnect();
		initCache();
		initURLMaker();
		_repliesNumOfPage = this.repliesNumOfPage();
		_repliesNumOfFirstPage = this.repliesNumOfFirstPage();
	}

	protected abstract void initCache();

	protected abstract void initURLMaker();

	protected abstract int repliesNumOfPage();

	protected abstract int repliesNumOfFirstPage();

	/*
	 * @function 主控方法，控制提取帖子内容以及回复
	 */
	public void extrac() {
		int repliesExtracted = 0;// 本次提取了的回复数量
		int startPage = 1; // 本次提取的开始页码
		int startFloor = 1; // 本次提取的开始楼层
		int repliesNum = _post.getCommentNum(); // 回复总数量
		int repliesDealed = _cache.getValue(_post.getId());
		; // 上次已经提取的回复数量 -1表示这是一个新帖子
			// 帖子总页数
		int pages = getPages(repliesNum);

		try {
			// 新帖子
			if (repliesDealed == -1) {
				repliesDealed = 0;
				extractContent();
			} else {
				_post.setNotNew();
				// 初始开始提取的页码和开始的楼层
				if (repliesDealed > _repliesNumOfFirstPage) {
					startPage = getStartPage(repliesDealed);
					startFloor = getStartFloor(repliesNum);
				}
			}

			// 提取回复
			for (int i = startPage; i <= pages; i++) {
				repliesExtracted += extractReplies(i, startFloor);
				startFloor = 1; // 开始楼层只对第一个提取页面有效
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (repliesExtracted > 0 || _post.getContent() != null) {
				FileOut.writeLine(_post.text());
				// 更新缓存
				_cache.setValue(_post.getId(), repliesExtracted + repliesDealed);
				_post = null;
			}
		}
	};

	/*
	 * @function 提取帖子回复
	 * 
	 * @param page:提取的页码
	 * 
	 * @param floor:提取的开始楼数,1楼开始
	 * 
	 * @return 提取的回复数量
	 */
	protected abstract int extractReplies(int page, int floor)
			throws IOException;

	/*
	 * @function 提取帖子内容
	 */
	protected abstract void extractContent() throws IOException;

	/*
	 * @function 从list中获取一个post
	 */
	public void getPost() {
		_post = _list.pop();
	}

	/*
	 * @function 获取总页数
	 * 
	 * @param repliesNum:回复数量
	 * 
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 */
	protected int getPages(int repliesNum) {
		return ((int) Math.ceil((double) (repliesNum - _repliesNumOfFirstPage)
				/ _repliesNumOfPage) + 1);
	};

	/*
	 * @function 获取第一个爬取的页面，根据以前的爬取历史进行计算
	 * 
	 * @param reoliesDealed: 历史爬取记录，本次以前已经爬取的回复数量
	 * 
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 */
	protected int getStartPage(int repliesDealed) {
		return ((int) Math.ceil((double) (repliesDealed
				- _repliesNumOfFirstPage + 1)
				/ _repliesNumOfPage) + 1);
	}

	/*
	 * @function 獲取开始楼层
	 * 
	 * @param repliesNum： 回复总数量
	 * 
	 * @param repliesNumOfFirstPage:第一页的回复数量
	 */
	protected int getStartFloor(int repliesNum) {
		return (repliesNum - _repliesNumOfFirstPage) % _repliesNumOfPage + 1;
	}

	@Override
	public void run() {
		while (true) {
			this.getPost();
			// System.out.println(_post.getId()+"  "+_post.getCommentNum());
			this.extrac();
		}
	}

}
