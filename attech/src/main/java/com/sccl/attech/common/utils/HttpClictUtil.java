package com.sccl.attech.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClictUtil {
	
	public static String executeGet(String url) throws Exception {  
        BufferedReader in = null;  
        String content = null;  
        try {
			// 定义HttpClient  
			HttpClient client = new DefaultHttpClient();  
			// 实例化HTTP方法  
			HttpGet request = new HttpGet(url);  
			HttpResponse response = client.execute(request); 

			in = new BufferedReader(new InputStreamReader(response.getEntity() 
			        .getContent(),"UTF-8"));  
			StringBuffer sb = new StringBuffer(); 
			sb.append("[");
			String line = "";  
      // String NL = System.getProperty("line.separator");  
			while ((line = in.readLine()) != null) {  
			    sb.append(line);  
			}  
			in.close();  
			sb.append("]");
			content = sb.toString();
			in.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return content;
    }  
	public static String httpPost(String url,Map<String, String> param){
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        HttpPost post = postForm(url, param);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body; 
	}
    private static String invoke(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
          
        return body;  
    } 
    private static String paseResponse(HttpResponse response) {  
        HttpEntity entity = response.getEntity();  
          
        String charset = EntityUtils.getContentCharSet(entity); 
        System.out.println(charset);
          
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(httpost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
    private static HttpPost postForm(String url, Map<String, String> params){  
        
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    }  
	public static String getInfo(){
	 
	 try {
		 
		 String s = null;//104.096669,30.681931
		// s = executeGet("http://api.map.baidu.com/geocoder/v2/?ak=RTPRmbT7hbkFk9Rear6gsgWH&callback=renderReverse&address=上海市黄浦区六合路&output=json&pois=1");
		 s = executeGet("http://api.map.baidu.com/geocoder/v2/?ak=RTPRmbT7hbkFk9Rear6gsgWH&callback=renderReverse&location=30.681931,104.096669&output=json&pois=1");
		 //JSONObject obj = JSONObject.fromObject(s);
		 System.out.println(s);
		 
		s = s.substring(30, s.length() - 2);
		return getAdrress(s);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return null;
	 
 }
 /**
  * 根据经纬度获取具体信息
  * @param x
  * @param y
  * @return
  */
 public static String getInfo(Double x,Double y){
	 
	 try {
		 
		 String s = null;
		 s = executeGet("http://api.map.baidu.com/geocoder/v2/?ak=RTPRmbT7hbkFk9Rear6gsgWH&callback=renderReverse&location="+x+","+y+"&output=json&pois=1");
		 //JSONObject obj = JSONObject.fromObject(s);
		 //System.out.println(s);
		 s = s.substring(30, s.length() - 2);
		return getAdrress(s);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return null;
	 
 }
    /**
     * 获得地址
     * 
     */
	public static String getAdrress(String jsonString) throws JSONException {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
        
        JSONObject json = (JSONObject) jsonObject.get("result");
        
        String s= json.get("formatted_address").toString();
        
        return s;
       

    }
	/**
	 * 获取code值
	 * @param jsonString
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getResult(String jsonString,String key) throws JSONException {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
        
        JSONArray json = jsonObject.getJSONArray("results");
        JSONObject obj = json.getJSONObject(0);
        
        
        return obj.get("code").toString();
       

    }
          
	public static String genRandomNum(int pwd_len){
	    //35是因为数组是从0开始的，26个字母+10个数字
	    final int maxNum = 36;
	    int i; //生成的随机数
	    int count = 0; //生成的密码的长度
	    char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	    StringBuffer pwd = new StringBuffer("");
	    Random r = new Random();
	    while(count < pwd_len){
	     //生成随机数，取绝对值，防止生成负数，
	   
	     i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
	   
	     if (i >= 0 && i < str.length) {
	      pwd.append(str[i]);
	      count ++;
	     }
	    }
	    return pwd.toString();
	 }
	
	public static void main(String []agrs ){
		
		System.out.println(getInfo());
	}
	

}
