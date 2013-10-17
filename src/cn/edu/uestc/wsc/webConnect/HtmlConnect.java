package cn.edu.uestc.wsc.webConnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
/*
 * @function get Html by given url
 * @author yy
 * @version 1.0
 * @date 2013-10-2*/
public class HtmlConnect {

	public  String getHtmlByUrl(String url) throws ParseException, IOException{
		
		HttpClient client=HttpClientBuilder.create().build();	// 创建httpClient对象	
		HttpGet get=new HttpGet(url);		        //以Get方式获取
		String html=null;	
	    try {
			HttpResponse response = client.execute(get);  //获取response
			int status=response.getStatusLine().getStatusCode();  //获取状态码
			if(status==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();       //获取实体
                if (entity!=null) {  
                    html = EntityUtils.toString(entity);//获得html源代码  
                } 
			}
		} finally{
			get.releaseConnection();
		}
		return html;
	}

}
