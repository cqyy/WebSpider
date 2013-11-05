package cn.edu.uestc.wsc.Cache;

public class BBS91Cache implements Cache {

	Redis redis=new Redis();
	@Override
	public int getValue(String key) {
		String value=redis.get("91_"+key);
		
		if(value==null){ return -1; }
		
		return Integer.parseInt(value);
	}

	@Override
	public void setValue(String key, int num) {
		redis.set("91_"+key, String.valueOf(num));

	}

	@Override
	public void del(String key) {
		redis.del(key);

	}

}
