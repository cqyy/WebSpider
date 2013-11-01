package cn.edu.uestc.wsc.Cache;

public class BBS360Cache implements Cache{

Redis redis=new Redis();
	
	@Override
	public  synchronized int getValue(String key) {
		String value=redis.get("360_"+key);
		
		if(value==null){ return -1; }
		
		return Integer.parseInt(value);
	}
	
	@Override
	public synchronized void setValue(String key, int num) {
		redis.set("360_"+key, String.valueOf(num));
		
	}

	@Override
	public void del(String key) {
		redis.del(key);
		
	}

}
