package com.sccl.attech.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sccl.attech.common.config.Global;

/**
 * 手机端上传文件，存入服务器工具类
 * @author luoyang
 *
 */
public class HttpClientMultiPartFileUtil {
	
	@SuppressWarnings("rawtypes")
	/**
	 * 文件上传，同时把名字相同的数据用map键值对进行封装
	 * @param request
	 * @return
	 */
	public static Map<String, List<String>> uploadMultipartFile(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	     String fileName = "";  
	     //String originalFileName = "";  //图片名字
	     Map<String, List<String>> map = new HashMap<String, List<String>>();
		 for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			 	String key1 = (String) it.next();
			 	
	            MultipartFile file = multipartRequest.getFile(key1);  
	            if (file == null || file.isEmpty())  
	                continue;  
	            String[] keyArray = key1.split("_");
	         //   originalFileName = file.getOriginalFilename();  
	            String realPath = SpringContextHolder.getRootRealPath() + Global.getCkBaseDir();
				fileName = "phone"+File.separator+DateUtils.getDate("yyyyMM") + File.separator + DateUtils.getDay() + File.separator + RandomUtils.nextLong() + "."
						+FilenameUtils.getExtension(file.getOriginalFilename());
				// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
					fileName=Global.getCkBaseDir() + fileName.replace("\\", "/");
					if(map.get(keyArray[0]) != null && !"".equals(map.get(keyArray[0]))){
						List<String> strings2 = map.get(keyArray[0]);
						strings2.add(fileName);
						map.put(keyArray[0], strings2);
					}else{
						List<String> strings = new ArrayList<String>();
						strings.add(fileName);
						map.put(keyArray[0], strings);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }
		 return map;
	}

}
