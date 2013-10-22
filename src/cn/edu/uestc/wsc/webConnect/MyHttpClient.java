package cn.edu.uestc.wsc.webConnect;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.HttpContext;

@SuppressWarnings({ "deprecation" })
public class MyHttpClient {

	private HttpClient instance;
 
	private void initHttpClient(){
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		
		schemeRegistry.register(
		         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 200
		cm.setMaxTotal(50);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20+1);

		 
		instance = new DefaultHttpClient(cm);
		instance.getParams().setParameter("http.socket.timeout", new Integer(1000*20));
        instance.getParams().setParameter("http.connection.timeout", new Integer(1000*20));

        
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount,HttpContext contex
                   ) {
              
                if(executionCount >= 5){
                	if(exception instanceof SocketTimeoutException){
                		return true;
                	}
                    return false;
                }
                if(exception instanceof NoHttpResponseException){
                	ThreadSleep(10);
                    return true;
                } else if (exception instanceof ClientProtocolException){
                	ThreadSleep(10);
                    return true;
                }else if(exception instanceof SocketTimeoutException){
                	ThreadSleep(60,30);
                	return true;
                }
                
                return false;
            }				
        };
        
        ((AbstractHttpClient) instance).setHttpRequestRetryHandler(retryHandler);
	}
	
	public static HttpClient getInstance(){
				
			MyHttpClient client = new MyHttpClient();
			client.initHttpClient();
		
		return client.instance;
	}
	
	public static void ThreadSleep(int sec){
		try {
			Thread.sleep(1000*(int)(Math.random()*sec+5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void ThreadSleep(int sec,int basetime){
		try {
			Thread.sleep(1000*(int)(Math.random()*sec+basetime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
