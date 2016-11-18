package com.sccl.attech.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 图片加水印工具类
 * @author luoyang
 *
 */
public class WaterMarkUtils {
	
	private static Logger logger = LoggerFactory.getLogger(WaterMarkUtils.class);
	
    /** 
     * 图片添加水印  （时间不能为空）
     * @param srcImgPath 需要添加水印的图片的路径 
     * @param outImgPath 添加水印后图片输出路径 
     * @param markContentColor
     * @param time
     * @param xy
     * @param address
     */
    public static void mark(String srcImgPath, String outImgPath, Color markContentColor, String time,String xy,String address) {  
        try {  
            // 读取原图片信息  
            File srcImgFile = new File(srcImgPath);  
            Image srcImg = ImageIO.read(srcImgFile);  
            int srcImgWidth = srcImg.getWidth(null);  
            int srcImgHeight = srcImg.getHeight(null);  
            // 加水印  
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = bufImg.createGraphics();  
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);  
            Font font = new Font("宋体", Font.PLAIN, 20);    
            g.setColor(markContentColor); //根据图片的背景设置水印颜色  
              
            g.setFont(font);  
            int x = 0;
            Map<String, String> map = new HashMap<String, String>();
            if(StringUtils.isNotBlank(xy)){
        		map.put("xy", xy);
        	}else{
        		xy = "";
        	}
            if(StringUtils.isNotBlank(address)){
        		map.put("address", address);
        	}else{
        		address = "";
        	}
            if(StringUtils.isNotBlank(time)){
        		map.put("time", time);
        	}
            boolean falg = true;
            while(falg){
            	if(StringUtils.isNotBlank(xy)){
            		map.put("xy", xy);
            		x = srcImgWidth - getWatermarkLength(xy, g) - 3; 
            		falg = false;
            		break;
            	}
            	if(StringUtils.isNotBlank(address)){
            		map.put("address", address);
            		if(isTime(address)){
            			x = srcImgWidth - getWatermarkLength(address, g) - 19; 
            		}else{
            			x = srcImgWidth - getWatermarkLength(address, g) - 3; 
            		}
            		
            		falg = false;
            		break;
            	}
            	if(StringUtils.isNotBlank(time)){
            		map.put("time", time);
            		x = srcImgWidth - getWatermarkLength(time, g) - 3; 
            		falg = false;
            		break;
            	}
            	//防止都为""的时候 死循环
            	falg = false;
            }
            
            int y = srcImgHeight - 3;  
            int size = map.size();
			if(size == 3){
				logger.info("水印地址:3..............................."+address);
				g.drawString(address, x, y);  
	            g.drawString(xy, x, y-30); 
	            g.drawString(time, x, y-60); 
			}else if(size == 2){
				String zuobiao = map.get("xy");
				if(StringUtils.isNotBlank(zuobiao)){
					 g.drawString(xy, x, y); 
					 logger.info("水印地址:2xy..............................."+address);
				}else{
					logger.info("水印地址:2address..............................."+address);
					 g.drawString(address, x, y); 
				}
	           
	            g.drawString(time, x, y-30);
			}else if(size == 1){
				logger.info("水印地址:1..............................."+address);
				g.drawString(address, x, y);
			}
            
            g.dispose();  
            // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);  
            ImageIO.write(bufImg, "jpg", outImgStream);  
            outImgStream.flush();  
            outImgStream.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * 获取水印文字总长度 
     * @param waterMarkContent 水印的文字 
     * @param g 
     * @return 水印文字总长度 
     */  
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {  
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());  
    }  
    /**
     * 判断是否为时间格式
     * @param value
     * @return true：是时间格式，false:不是时间格式
     */
    public static boolean isTime(String value){
    	//String input = "2015-01-27 10:11:12";
        String regex = "^[1-9]\\d{3}\\-(0?[1-9]|1[0-2])\\-(0?[1-9]|[12]\\d|3[01])\\s*(0?[1-9]|1\\d|2[0-3])(\\:(0?[1-9]|[1-5]\\d)){2}$";
        
        return value.matches(regex);
    }
      
    public static void main(String[] args) {  
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字  
    	WaterMarkUtils.mark("d:/1-140409160234.jpg", "d:/1-140409160234.jpg", Color.red,"2015-08-12 15:12:30","经度:104.056038 纬度:30.628336", "xxxxxxxxxxxxxxxx");  
    	
    	
   }  

}
