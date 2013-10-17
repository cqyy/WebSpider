package cn.edu.uestc.wsc.Cache;

import redis.clients.jedis.Jedis;

class Redis{
	
	private  Jedis jedis=null;
	
    public Redis(){
    	this.connect();
    }
	
	private  void connect(){
		jedis=new Jedis("127.0.0.1", 6379, 1000);
	}
	
	public  void set(String key,String value){
		if(jedis==null||!jedis.isConnected()){
			connect();
		}
	    jedis.set(key, value);
	}
	
	public  String get(String key){
		if(jedis==null||!jedis.isConnected()){
			connect();
		}
		
		return jedis.get(key);
	}
	
	public  long del(String key){
		if(jedis==null||!jedis.isConnected()){
			connect();
		}
		return jedis.del(key);
	}
}
