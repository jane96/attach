

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestHttpGet {
	
	
	@SuppressWarnings("finally")
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
	                    .getContent()));  
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
	        } catch(Exception ee){
	        	ee.printStackTrace();
	        }finally {  
	            if (in != null) {  
	                try {  
	                    in.close();// 最后要关闭BufferedReader  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	            return content;  
	        }  
	    }  
	 public static void main(String[] agrs){
		 
		 try {
			 int  b = -3;
				int y = b%2;
			System.out.println("1111111"+y);
			 String s = null;
			 s = executeGet("http://api.map.baidu.com/geocoder/v2/?ak=RTPRmbT7hbkFk9Rear6gsgWH&callback=renderReverse&location=30.681931,104.096669&output=json&pois=1");
			 //JSONObject obj = JSONObject.fromObject(s);
			 System.out.println(s);
			 
//			s = s.substring(30, s.length() - 2);
//			StringBuilder sb = new StringBuilder();
//			sb.append("[");
//			sb.append(s);
//			sb.append("]");
//			 JSONArray json = JSONArray.fromObject(sb.toString());
//			 System.out.println(getAdrress(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
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

}
