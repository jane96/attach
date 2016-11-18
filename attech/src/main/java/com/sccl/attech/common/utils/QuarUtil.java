package com.sccl.attech.common.utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sccl.attech.common.vo.Point;


/**
 * 计算点是否在矩形内
 * @author luoyang
 *
 */
public class QuarUtil {

	/**
	 * 
	 * @param mp1 第一个点
	 * @param mp2 第二个点
	 * @param mp3 第三个点
	 * @param mp4 第四个点
	 * @param mp 判断点
	 * @return boolean
	 */
    public static boolean isContain(Point mp1,Point mp2,Point mp3,Point mp4,Point mp)
    {
        if (multiply(mp, mp1, mp2) * multiply(mp,mp4, mp3) <= 0
            && multiply(mp, mp4, mp1) * multiply(mp, mp3, mp2) <= 0)
            return true;

        return false;
    }
    // 计算叉乘 |P0P1| × |P0P2| 
    private static double multiply(Point p1, Point p2, Point p0)
    {
        return ((p1.getX() - p0.getX()) * (p2.getY() - p0.getY()) - (p2.getX() - p0.getX()) * (p1.getY() - p0.getY()));
    }
    
    public static void main(String[] agrs){
    	//103.976511,30.819977|103.901772,30.866113|103.644785,30.803104|103.685029,30.807075|103.85003,30.770343|103.85003,30.770343|
    		Point[] pointArr = new Point[]{new Point(103.976511,30.819977),new Point(103.901772,30.866113),new Point(103.644785,30.803104)
    		,new Point(103.685029,30.807075),new Point(103.85003,30.770343),new Point(103.85003,30.770343)};
    		boolean ptInPolygon = QuarUtil.PtInPolygon (new Point(103.762774,30.819484), pointArr, 6);
    		System.out.println(ptInPolygon);

    }//103.705726,30.825931|103.829908,30.499883|103.635586,30.475982|103.683879,30.554633|
	
    /**
     * 根据当前坐标判断是否在多边形内
     * @param p 当前坐标
     * @param points 围栏坐标
     * @param nCount 计数器
     * @return
     */
    public static boolean PtInPolygon (Point p, Point[] points, int nCount){
    		
    		int nCross = 0; 
    		
    		for(int i = 0; i < nCount; i++){
    			Point p1 = points[i]; 
    			Point p2 = points[(i + 1) % nCount]; 
    			// 求解 y=p.y 与 p1p2 的交点 
    			if ( p1.getY() == p2.getY() ) // p1p2 与 y=p0.y平行 
    				continue; 
    			if ( p.getY() < Math.min(p1.getY(), p2.getY()) ) // 交点在p1p2延长线上 
    			 continue; 
    			if ( p.getY() >= Math.max(p1.getY(), p2.getY()) ) // 交点在p1p2延长线上 
    			    continue; 
    			// 求交点的 X 坐标 -------------------------------------------------------------- 
    		   double x = (double)(p.getY() - p1.getY()) * (double)(p2.getX() - p1.getX()) / (double)(p2.getY() - p1.getY()) + p1.getX(); 
    		   if ( x > p.getX()) 
    			    nCross++; // 只统计单边交点 
    		}
    		 // 单边交点为偶数，点在多边形之外 --- 
    		 return (nCross % 2 == 1); 

    }
    
    /**
     * 根据点集合字符串获取 点集合
     * @param ids (围栏)点集合字符串
     * @return
     */
    public static List<Point> getPointAtt(String ids){
    	List<Point> points = new ArrayList<Point>();
		if(StringUtils.isNotBlank(ids)){
			String[] xys = ids.split("\\|");
			for(String s : xys){
				String[] xy = s.split(",");
				points.add(new Point(Double.valueOf(xy[0]),Double.valueOf(xy[1])));
			}
		}
		return points;
    }
	//计算百分比
    public static String getPercent(int x,int total){  
		String result="";//接受百分比的值  
		double x_double=x*1.0; 
		double total_double = total*1.0;
		double tempresult=(x_double/total_double)*100; 
		BigDecimal   b   =   new   BigDecimal(tempresult);  
		result   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).intValue()+"";   
		return result;  
	} 
}
