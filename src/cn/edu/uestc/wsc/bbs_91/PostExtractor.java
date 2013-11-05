package cn.edu.uestc.wsc.bbs_91;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.uestc.contentContainer.Comment;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBS91Cache;
import cn.edu.uestc.wsc.bbs.AbstractPostExtractor;

public class PostExtractor extends AbstractPostExtractor {

	public PostExtractor(PostList list) {
		super(list);
	}

	@Override
	protected void initCache() {
		super._cache = new BBS91Cache();

	}

	@Override
	protected void initURLMaker() {
		super._urlMaker=new URLMaker91();
	}

	@Override
	protected int repliesNumOfPage() {
		return 10;
	}

	@Override
	protected int repliesNumOfFirstPage() {
		return 9;
	}

	@Override
	protected int extractReplies(int page, int floor) throws IOException {
		int count = 0;
		Document doc = Jsoup.parse(_con.getHtmlByUrl(_urlMaker.createPostURL(
				_post.getId(), page)));
		Element rp_div = doc.select("#postlist").first();
		if (rp_div == null) {
			return count;
		}
		/* 跳过帖子内容 */
		if (page == 1 && floor == 1) {
			floor = 2;
		}

		Element ele = null;
		for (int i = floor + 1; i < rp_div.children().size() - 1; i++) {
			ele = rp_div.children().get(i);
			String content = "";
			String time = "";
            /*作者*/
			String author = ele.select("#vwuser>h3>a").first().text();
			
			/*回复内容*/
			if (ele.select(".t_fsz").first() != null) {
				content = ele.select(".t_fsz").first().text();
			}
			
			/*回帖时间*/
			if (ele.select(".authi em span").first() != null) {
				time = ele.select(".authi em span").first().attr("title");
			} else {
				time = ele.select(".authi em").first().text()
						.replace("发表于", "").trim();
			}

			_post.addComment(new Comment(author, time, content));
			count++;
		}

		return count;
	}

	@Override
	protected void extractContent() throws IOException {
		Document doc = Jsoup.parse(_con.getHtmlByUrl(_urlMaker.createPostURL(
				_post.getId(), 1)));
		Element ele = doc.select("#postlist").first().children().get(2);

		/* 提取帖子内容 */
		if (ele.select(".t_fsz").first() == null) {
			_post.setContent("作者已被禁言");
		} else {
			_post.setContent(ele.select(".t_fsz").first().text());
		}
		/* 提取发帖时间 */
		ele = ele.select(".authi> em").first();
		if (ele.select("span").first() != null) {
			_post.setTime(ele.select("span").first().attr("title"));
		} else {
			_post.setTime(ele.text().replace("发表于", "").trim());
		}

	}

}
