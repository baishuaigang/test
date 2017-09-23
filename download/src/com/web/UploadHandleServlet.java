package com.web;
import java.io.File;
   import java.io.FileOutputStream;
   import java.io.IOException;
 import java.io.InputStream;
 import java.util.List;
 import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.commons.fileupload.FileItem;
 import org.apache.commons.fileupload.FileUploadBase;
 import org.apache.commons.fileupload.ProgressListener;
 import org.apache.commons.fileupload.disk.DiskFileItemFactory;
 import org.apache.commons.fileupload.servlet.ServletFileUpload;
 

 public class UploadHandleServlet extends HttpServlet {
     public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
//                 String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
    	           String savePath = "C:/Users/Administrator/Desktop/WEB-INF/upload";
                 String tempPath = "C:/Users/Administrator/Desktop/WEB-INF/temp";
//                 String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
                 File tmpFile = new File(tempPath);
                 if (!tmpFile.exists()) {
                     tmpFile.mkdir();
                 } 
                 String message = "";
                 try{
                     DiskFileItemFactory factory = new DiskFileItemFactory();
                     factory.setSizeThreshold(1024*100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
                     factory.setRepository(tmpFile);
                     ServletFileUpload upload = new ServletFileUpload(factory);
                     upload.setProgressListener(new ProgressListener(){
                         public void update(long pBytesRead, long pContentLength, int arg2) {
                         }
                     });
                     upload.setHeaderEncoding("UTF-8"); 
                     if(!ServletFileUpload.isMultipartContent(request)){
                         return;
                     }
                     upload.setFileSizeMax(1024*1024);
                     upload.setSizeMax(1024*1024*10);
                     List<FileItem> list = upload.parseRequest(request);
                     for(FileItem item : list){
                         if(item.isFormField()){
                             String name = item.getFieldName();
                             String value = item.getString("UTF-8");
                         }else{
                             String filename = item.getName();
                             if(filename==null || filename.trim().equals("")){
                                 continue;
                             }
                             filename = filename.substring(filename.lastIndexOf("\\")+1);
                             String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
                             System.out.println("上传的文件的扩展名是："+fileExtName);
                             InputStream in = item.getInputStream();
                             String saveFilename = makeFileName(filename);
                             String realSavePath = makePath(saveFilename, savePath);
                             FileOutputStream out = new FileOutputStream(realSavePath + "\\" + saveFilename);
                             byte buffer[] = new byte[1024];
                             int len = 0;
                             while((len=in.read(buffer))>0){
                                 out.write(buffer, 0, len);
                             }
                             in.close();
                             out.close();
                             message = "文件上传成功！";
                         }
                     }
                 }catch (FileUploadBase.FileSizeLimitExceededException e) {
                     e.printStackTrace();
                     request.setAttribute("message", "单个文件超出最大值！！！");
                     request.getRequestDispatcher("/message.jsp").forward(request, response);
                     return;
                 }catch (FileUploadBase.SizeLimitExceededException e) {
                     e.printStackTrace();
                     request.setAttribute("message", "上传文件的总的大小超出限制的最大值！！！");
                     request.getRequestDispatcher("/message.jsp").forward(request, response);
                     return;
                 }catch (Exception e) {
                     message= "文件上传失败！";
                     e.printStackTrace();
                 }
                 request.setAttribute("message",message);
//                 request.getRequestDispatcher("/message.jsp").forward(request, response);
                 response.sendRedirect(request.getContextPath()+"/servlet/ListFileServlet");
     }
     
     private String makeFileName(String filename){  //2.jpg
         //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
         return UUID.randomUUID().toString() + "_" + filename;
     }
    
     private String makePath(String filename,String savePath){
         int hashcode = filename.hashCode();
         int dir1 = hashcode&0xf;  //0--15
         int dir2 = (hashcode&0xf0)>>4;  //0-15
//         String dir = savePath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
         String dir = savePath ;
         File file = new File(dir);
         if(!file.exists()){
             file.mkdirs();
         }
         return dir;
     }
     public void doPost(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
         doGet(request, response);
     }
 }
