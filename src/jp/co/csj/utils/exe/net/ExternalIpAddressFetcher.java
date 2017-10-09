package jp.co.csj.utils.exe.net;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mydbsee.common.CmnStrUtils;
public class ExternalIpAddressFetcher {
    //外网IP提供者的网址
    private String externalIpProviderUrl;

    //本机外网IP地址
    private String myExternalIpAddress = null;

    public ExternalIpAddressFetcher(String externalIpProviderUrl) throws Throwable {
       this.externalIpProviderUrl = externalIpProviderUrl;

       String returnedhtml =fetchExternalIpProviderHTML(externalIpProviderUrl);
       parse(returnedhtml);
    }
    private String fetchExternalIpProviderHTML(String externalIpProviderUrl){
       // 输入流
       InputStream in = null;
       // 到外网提供者的Http连接
       HttpURLConnection httpConn = null;

       try {
           // 打开连接
           URL url = new URL(externalIpProviderUrl);
           httpConn = (HttpURLConnection)url.openConnection();         
           // 连接设置
           HttpURLConnection.setFollowRedirects(true);
           httpConn.setRequestMethod("GET");
           httpConn.setRequestProperty("User-Agent",
                   "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

           // 获取连接的输入流
           in = httpConn.getInputStream();
           byte[] bytes=new byte[1024];// 此大小可根据实际情况调整 
           // 读取到数组中
           int offset = 0;
           int numRead = 0;
           while (offset < bytes.length
                  && (numRead=in.read(bytes, offset,bytes.length-offset)) >= 0) {
               offset += numRead;
           } 
           //将字节转化为为UTF-8的字符串       
           String receivedString=new String(bytes,"UTF-8");    
           // 返回
           return receivedString;
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (Throwable e) {
           e.printStackTrace();
       } finally {
           try {
               in.close();
               httpConn.disconnect();
           } catch (Throwable ex) {
               ex.printStackTrace();
           }
       }

       // 出现异常则返回空
       return null;
    }

	private void parse(String html) throws Throwable {
		if (CmnStrUtils.isNotEmpty(html)) {
			Pattern pattern = Pattern.compile("(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})",Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(html);
			while (matcher.find()) {
				myExternalIpAddress = matcher.group(0);
			}
		}
	}   
    public String getMyExternalIpAddress() {
    
       return "ip:["+myExternalIpAddress+"]" +AddressUtils.getContentIp(myExternalIpAddress);
    }
    public static String getOutIp() {
    	try {
            ExternalIpAddressFetcher fetcher=new ExternalIpAddressFetcher("http://checkip.dyndns.org/");
            return fetcher.getMyExternalIpAddress();
		} catch (Throwable e) {
			return "ip:[unknow]";
		}
    }
    public static void main(String[] args){
    	  try {
			System.out.println(getOutIp());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}