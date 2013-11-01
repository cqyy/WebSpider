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

	private HttpClient client = null;
	private HttpGet get = null;
	private HttpResponse response = null;
	private HttpEntity entity = null;
	private String charset="utf-8";     //默认字码
	private final int retryTime=3;

	@SuppressWarnings("deprecation")
	public HtmlConnect() {
	//	client=HttpClientBuilder.create().build(); // 创建httpClient对象
		client = MyHttpClient.getInstance();
		client.getParams().setParameter("http.protocol.content-charset", charset);
	}
	
	@SuppressWarnings("deprecation")
	public void setCharset(String charset){
		this.charset=charset;
		client.getParams().setParameter("http.protocol.content-charset", this.charset);
	}

	public String getHtmlByUrl(String url) throws IOException {
		get = new HttpGet(url); // 以Get方式获取
		String html = null;
		for(int i=0;i<this.retryTime;i++){
			try {
				response = client.execute(get);// 获取response
				int status = response.getStatusLine().getStatusCode(); // 获取状态码
				if (status == HttpStatus.SC_OK) {
					entity = response.getEntity(); // 获取实体
					if (entity != null) {
						html = EntityUtils.toString(entity, this.charset); // 获得html源代码
						break;
					}
				} else {
					System.out.println("failed: " + url);
					Thread.sleep(1000*(i+1));
				}
			} catch (IOException e) {
				// do nothing
			} catch (InterruptedException e) {
			}
		}
		get.releaseConnection();

		return html;
	}

}
