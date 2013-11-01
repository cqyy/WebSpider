package cn.edu.uestc.wsc.bbs_360;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.uestc.contentContainer.Comment;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.BBS360Cache;
import cn.edu.uestc.wsc.bbs.AbstractPostExtractor;

public class PostExtractor extends AbstractPostExtractor {

	public PostExtractor(PostList list) {
		super(list);
	}
	@Override
	protected int extractReplies(int page, int floor) throws IOException {
		int count = 0;
		Document doc = Jsoup.parse(_con.getHtmlByUrl(_urlMaker.createPostURL(
				_post.getId(), page)));
		Element rp_div = doc.select("#postlist").first();
		if (page == 1 && floor == 1) {
			floor = 2;
		}// 跳过内容

		Element ele = null;
		for (int i = floor + 1; i < rp_div.children().size() - 1; i++) {
			ele = rp_div.children().get(i);

			String author = ele.select(".xw1").first().text();

			String content =""; 
			if(ele.select(".t_fsz").first()!=null){
				content=ele.select(".t_fsz").first().text();
			}
			String time = "";
			if (ele.select(".pti div em span").first() != null) {
				time = ele.select(".pti div em span").first().attr("title");
			} else {
				time = ele.select(".pti div em").first().text()
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
		if(ele.select(".t_f").first()==null){
			_post.setContent("作者已被禁言");
		}else{
		_post.setContent(ele.select(".t_f").first().text());
		}
		ele = ele.select(".authi em").first();
		if (ele.select("span").first() != null) {
			_post.setTime(ele.select("span").first().attr("title"));
		} else {
			_post.setTime(ele.text().replace("发表于", "").trim());
		}

	}

	/*
	 * @function 从list中获取一个post
	 */
	public void getPost() {
		_post = _list.pop();
	}

	@Override
	protected void initCache() {
		_cache = new BBS360Cache();

	}

	@Override
	protected void initURLMaker() {
		_urlMaker = new URLMaker360();

	}

	@Override
	protected int repliesNumOfPage() {
		return 10;
	}

	@Override
	protected int repliesNumOfFirstPage() {
		return 9;
	}

}
