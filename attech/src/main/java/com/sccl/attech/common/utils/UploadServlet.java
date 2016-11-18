package com.sccl.attech.common.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public UploadServlet() {
		super();
	}
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	response.setContentType("text/html;charset=UTF-8");
	FileItemFactory factory = new DiskFileItemFactory();
    // Create a new file upload handler
    String type = request.getParameter("type");
    ServletFileUpload upload = new ServletFileUpload(factory);
     //防止中文文件名乱码
     upload.setHeaderEncoding("UTF-8");
     //获取存放路径
     String path = "";
     if(type != null){
    	 if(type.equals("1")){
    		 path = request.getSession().getServletContext().getRealPath("/templates/");
    	 }
     }
     if("".equals(path)){
    	 path = request.getSession().getServletContext().getRealPath("/templates/");
     }
     String name = null;
   //  String nameShow = null;
     int b = 0;
    try {
		List<FileItem> items = upload.parseRequest(request);
        if (items != null) {
           Iterator<FileItem> itr = items.iterator();
           while (itr.hasNext()) {
               FileItem item = (FileItem) itr.next();
               if (item.isFormField()) {
            	   if(b == 4){
            		   name = item.getString();
            	   }
            	   b++;
               continue;
               } else {
            	 //File fullFile=new File(item.getName());
               	//	name = fullFile.getName()+DateUtils.getTime(0);
                   //文件都保存在文件夹下面, 使用原始的文件名
                  // nameShow = fullFile.getName();
               	// String tempName = fullFile.getName();
               	//Calendar calendar = Calendar.getInstance();
        		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
               	//name = tempName.substring(0, tempName.lastIndexOf("."))+dateFormat.format(calendar.getTime())+tempName.substring(tempName.lastIndexOf("."));
                   File savedFile=new File(path, name);
                   item.write(savedFile);
               }
           }
        }
    } catch (Exception e) {

        e.printStackTrace();

    }
    //结束upload. 如果没有这一行, 浏览器里面一直是uploading....
//    Map map =new HashMap();
//    map.put("name", name);
//    map.put("nameShow", nameShow);
    PrintWriter pw = response.getWriter(); 
   // JSONObject jsonObject = JSONObject.fromObject(map);
    pw.print(name);
    System.out.println(name);
   // response.getOutputStream().println(name);
	/*
	 String ajaxUpdateResult = "";
	 String path = request.getSession().getServletContext().getRealPath("/templates/");
     try {

         List items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);            

         Iterator<FileItem> itr = items.iterator();
         while (itr.hasNext()) {
        	 FileItem item = (FileItem) itr.next();
             if (item.isFormField()) {

                 ajaxUpdateResult += "Field " + item.getFieldName() + 

                 " with value: " + item.getString() + " is successfully read\n\r";

             } else {

                 String fileName = item.getName();

                 InputStream content = item.getInputStream();

                 response.setContentType("text/plain");

                 response.setCharacterEncoding("UTF-8");

                 // Do whatever with the content InputStream.

                 System.out.println(Streams.asString(content));

                 ajaxUpdateResult += "File " + fileName + " is successfully uploaded\n\r";
                 File savedFile=new File(path, fileName);
                 try {
					item.write(savedFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

             }

         }
         System.out.println(ajaxUpdateResult);
     } catch (FileUploadException e) {

         throw new ServletException("Parsing file upload failed.", e);

     }

     response.getWriter().print(ajaxUpdateResult);
     */
}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
