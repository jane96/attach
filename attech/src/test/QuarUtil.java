import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
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
    
    public static void main(String[] agrs) throws ParseException{
    	
    	SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = dFormat.parse("2015-07-06");
    	System.out.println(date.before(new Date()));
    }
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
       double radLat1 = rad(lat1);
       double radLat2 = rad(lat2);
       double a = radLat1 - radLat2;
       double b = rad(lng1) - rad(lng2);
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s = s * EARTH_RADIUS;
       s = Math.round(s * 10000) / 10000;
       return s;
    }
    private static double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
       return d * Math.PI / 180.0;
    }
	
}
