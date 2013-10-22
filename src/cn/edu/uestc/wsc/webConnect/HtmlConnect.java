package cn.edu.uestc.wsc.webConnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
/*
 * @function get Html by given url
 * @author yy
 * @version 1.0
 * @date 2013-10-2*/
public class HtmlConnect {

	private HttpClient client=null;
	private HttpGet get=null;
	private HttpResponse response=null;
	private HttpEntity entity=null;
	private final int retryTimes=3; //连接失败，重试次数
	private final int sleepTime=1000; //连接失败睡眠基本时间
	
	public HtmlConnect(){
		 //client=HttpClientBuilder.create().build();	// 创建httpClient对象			 
		client=MyHttpClient.getInstance();
	}
	
	public  String getHtmlByUrl(String url) throws IOException{
		get=new HttpGet(url);		        //以Get方式获取
		String html=null;	
		int i=1;
		for(;i<=retryTimes;i++){
			try {
				response = client.execute(get);//获取response
				int status=response.getStatusLine().getStatusCode();    //获取状态码
				if(status==HttpStatus.SC_OK){					
					entity=response.getEntity();            //获取实体					
					if (entity!=null) {  
						html = EntityUtils.toString(entity);           //获得html源代码  
						break;     //获取成功，跳出循环。
					} 
				}
				//获取失败，睡眠并重试
				Thread.sleep(sleepTime*i);
				} catch (IOException e) {
					//do nothing
				} catch (InterruptedException e) {
					//do nothing
			}
		}
		get.releaseConnection();
		
		//三次重试失败
		if(i>retryTimes){
			String log="error:connection fail!  url:"+url;
			//Logger.writeLog(log);
			System.out.println(log);
			throw new IOException();
		}
		return html;
	}

}
