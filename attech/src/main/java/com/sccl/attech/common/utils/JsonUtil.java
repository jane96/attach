package com.sccl.attech.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json工具类
 * @author luoyang
 *
 */
public class JsonUtil {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		TestObject testObject = new TestObject();
//		testObject.setName("测试一");
//		testObject.setSort("1");
//		TestObject testObject2 = new TestObject();
//		testObject2.setName("测试二");
//		testObject2.setSort("2");
//		List<TestObject> list = new ArrayList<TestObject>();
//		list.add(testObject);
//		list.add(testObject2);
//		
//		JSONArray jsonArray = JSONArray.fromObject(list);
//		String json = jsonArray.toString();
//		System.out.println(json);
//		
//		JSONArray jsonObject =JSONArray.fromObject(json);
//		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();  
//        Iterator<JSONObject> it = jsonObject.iterator();  
//        while(it.hasNext()){  
//            JSONObject json2 = it.next();  
//            maplist.add(parseJSON2Map(json2.toString()));  
//        } 
//        for(int i =0 ; i < maplist.size(); i++){
//        	System.out.println(maplist.get(i).get("name"));
//        }
//		System.out.println(jsonObject);
		

	}
	/**
	 * 解析模板元素,获取文件上传个数
	 * @param options json数据
	 * @return
	 */
	public static String getFileSize(String options){
		JSONObject obj = JSONObject.fromObject(options);
		Iterator<?> it = obj.keys();
		String zhi = null;
		while(it.hasNext()){  
			String key = it.next()+"";  
            String value = obj.getString(key);  
            JSONObject obj2 = JSONObject.fromObject(value);
            zhi = obj2.get("value")+"";
	        } 
		
		return zhi;
		
	}
	/**
	 * 把json的字符串数组转化为Map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getMapResult(String json){
		JSONArray jsonObject =JSONArray.fromObject(json);
		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();  
        Iterator<JSONObject> it = jsonObject.iterator();  
        while(it.hasNext()){  
            JSONObject json2 = it.next();  
            maplist.add(parseJSON2Map(json2.toString()));  
        } 
        return maplist;
	}
    public static Map<String, Object> parseJSON2Map(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = JSONObject.fromObject(jsonStr);  
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){  
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                Iterator<JSONObject> it = ((JSONArray)v).iterator();  
                while(it.hasNext()){  
                    JSONObject json2 = it.next();  
                    list.add(parseJSON2Map(json2.toString()));  
                }  
                map.put(k.toString(), list);  
            } else {  
                map.put(k.toString(), v);  
            }  
        }  
        return map;  
    } 

}
