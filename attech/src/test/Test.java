import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sccl.attech.modules.sys.dao.UserDao;
import com.sccl.attech.modules.sys.dao.VoteDao;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.entity.Vote;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDao ud = new UserDao();
		List<User> list2 = ud.findAllList();
		System.out.println(list2.get(0).getLoginName());
		VoteDao vd = new VoteDao();
		List<Vote> list = vd.findAllList();
		System.out.println(list.get(0).getVotingContent());
		

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
