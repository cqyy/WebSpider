package cn.edu.uestc.wsc.bbs;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.uestc.contentContainer.Post;
import cn.edu.uestc.contentContainer.PostList;
import cn.edu.uestc.wsc.Cache.Cache;
import cn.edu.uestc.wsc.webConnect.HtmlConnect;

/*
 * 帖子列表提取器抽象类*/
public abstract class AbstractPostlistExtractor implements Runnable{
	protected PostList _list=null;
	protected HtmlConnect _connect=null;
	protected int _startPage=1;
	protected Cache _cache=null;
	protected int _block;
	protected int _pages;
	protected BlockManager _blockManager=null;
	protected URLMaker _urlMaker=null;
	
	public AbstractPostlistExtractor(PostList list){
		_list=list;
		_connect=new HtmlConnect();
		initCache();
		initBlockManager();
		initURLMaker();
	};
	/**/
	public void run() {
		while(true){
			try {
				_block=_blockManager.next();
				_pages=this.getPages(_block);
				_startPage=1;
				while(_startPage<=_pages){
					System.out.println("extract block: "+_block+"  pages: "+_startPage);
					this.extract(_block,_startPage);
					_startPage++;
					/*控制列表长度，使其不至于太长*/
					while(_list.size()>60){
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * @function 获取帖子列表的页数
	 * @param blockId 论坛版块的id
	 * @throws IOExceptin
	 * */
	protected abstract int getPages(int blockId) throws IOException;

	/*
	 * @function 提取一页的帖子
	 * @param doc:帖子列表页面的DOM对象
	 * @return 提取到的帖子数量
	 * */
	protected abstract int extractList(Document doc);
	 
	/*
	 * @function 根据Redis记录，判断该post是否有新的回复或者是新的
	 * 如果Redis中没有记录或者记录中的回复数与当前回复数不一致，则热土让你 true
	 * */
	protected  boolean isNew(Post post){
		int value=_cache.getValue(post.getId());
		if(value!=post.getCommentNum()){
			return true;
		}
		return false;
	};

	
	/*
	 * @function 提取主控方法，控制页码*/
	public  void extract(int block,int page){
		try {
			Document doc=Jsoup.parse(
					_connect.getHtmlByUrl(
							_urlMaker.createPostlistURL(block, page)));
			extractList(doc);
		} catch (IOException e) {

			e.printStackTrace();
		}catch(Exception e){
			//System.out.println("block"+block+"page"+page+"url:"+_urlMaker.createPostlistURL(block, page));
			e.printStackTrace();
		}
	};
	
	/*
	 * @function 初始化Cache为特定论坛的Cache操作
	 * */
	protected abstract void initCache();
	
	/*
	 * @function 初始化BlockManager为特定的版块管理器
	 * */
	protected abstract void initBlockManager();
	
	/*
	 * @function 初始化URL控制器*/
	protected abstract void initURLMaker();
}
