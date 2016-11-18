package com.sccl.attech.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

public class JsonToBean {
	
	 //对JSON中的date类型进行格式转化
	 public static void setDataFormat2JAVA(){     
        //设定日期转换格式     
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));     
	 } 
	 
	 /**   
	     * 从json数组中得到相应java数组   
	     * json形如：["123", "456"]   
	     * @param jsonString   
	     * @return   
	     */    
	 public static Object[] getObjectArrayFromJson(String jsonString) {     
        JSONArray jsonArray = JSONArray.fromObject(jsonString);     
        return jsonArray.toArray();     
	 }  
	 
	 /**
	  * 从一个JSON 对象字符格式中得到一个java对象
	  * @param jsonString
	  * @param T
	  * @return
	  * @throws Exception
	  */
	 public static Object getJavaDTO(String jsonString, Class T) throws Exception{     
        JSONObject jsonObject = null;     
        try{     
            setDataFormat2JAVA();      
            jsonObject = JSONObject.fromObject(jsonString);     
        }catch(Exception e){     
            throw new Exception("json串转java对象异常!",e);    
        }     
        return JSONObject.toBean(jsonObject,T);     
	 } 
	 
	 /**   
	     * 从一个JSON数组得到一个java对象数组，形如：   
	     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...]   
	     * @param object   
	     * @param clazz   
	     * @return   
	     */    
	 public static Object[] getJavaDTOArray(String jsonString, Class T){     
		 
        setDataFormat2JAVA();   
        
        JSONArray array = JSONArray.fromObject(jsonString);     
        Object[] obj = new Object[array.size()];     
        for(int i = 0; i < array.size(); i++){     
            JSONObject jsonObject = array.getJSONObject(i);     
            obj[i] = JSONObject.toBean(jsonObject, T);     
        }     
        return obj;     
	 }
	 
	 /**   
	     * 从一个JSON数组得到一个java对象集合   
	     * @param object   
	     * @param clazz   
	     * @return   
	     */    
	 public static List getDTOList(String jsonString, Class T){     
        
		setDataFormat2JAVA();
        
        JSONArray array = JSONArray.fromObject(jsonString);     
        List list = new ArrayList();     
        for(Iterator iter = array.iterator(); iter.hasNext();){     
            JSONObject jsonObject = (JSONObject)iter.next();     
            list.add(JSONObject.toBean(jsonObject, T));     
        }     
        return list;     
	 }
	 
	 /**   
	     * 从json HASH表达式中获取一个map，该map支持嵌套功能   
	     * 形如：{"id" : "johncon", "name" : "小强"}   
	     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap   
	     * @param object   
	     * @return   
	     */    
	 public static Map getMapFromJson(String jsonString) {     
        
		setDataFormat2JAVA();
        
        JSONObject jsonObject = JSONObject.fromObject(jsonString);     
        Map map = new HashMap();     
        for(Iterator iter = jsonObject.keys(); iter.hasNext();){     
            String key = (String)iter.next();     
            map.put(key, jsonObject.get(key));     
        }     
        return map;     
	 }  
}
