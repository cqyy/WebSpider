package cn.edu.uestc.wsc.Cache;

/*
 * @funciton 小米BBS缓存控制类
 **/
public class BBSxmCache implements Cache{

	Redis redis=new Redis();
	
	/*
	 * @funciton 获取key对应的值。key为小米bbs每个帖子的id，值为该帖子的回复数量
	 * 对id进行获取和存储时候，需要加上‘xm_’前缀，使得与其他bbs的id不会有冲突
	 * @param key:帖子id*/
	@Override
	public  synchronized int getValue(String key) {
		String value=redis.get("xm_"+key);
		
		if(value==null){ return -1; }
		
		return Integer.parseInt(value);
	}

	/*
	 * @function 设置key对应的值
	 * @param key：帖子id
	 * @param num:相应帖子对应的回复数量*/
	@Override
	public synchronized void setValue(String key, int num) {
		redis.set("xm_"+key, String.valueOf(num));
		
	}

	@Override
	public void del(String key) {
		redis.del(key);
		
	}

}
