package wz.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class WzFileUploadAPI implements WzAPI
{
	// 返回值为JSONArray字符串，元素为每个表单域的服务应答
	// 即：JSONObject exec(String fieldName,String fileName,InputStream inputStream);的返回值
	@Override
	public String exec(HttpServletRequest request)
	{
		// 检查该请求是否为文件上传的请求
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// 创建一个JSON数组对象，将作为服务器的应答
		JSONArray jsonArray = new JSONArray();
		
		if(isMultipart)
		{ 
			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload();
			
			// 解析请求
			FileItemIterator fileItemIteratorer;
			try
			{
				// 从请求中获得文件项迭代器
				fileItemIteratorer = upload.getItemIterator(request);
				
				// 当有检测到有文件项时
				while (fileItemIteratorer.hasNext())
				{
					// 取得文件项的流
					FileItemStream fileItemStream = fileItemIteratorer.next();
					// 取得表单的字段名
					String fieldName = fileItemStream.getFieldName();
					// 取得文件名
					String fileName = fileItemStream.getName();
					// 取得表单的数据流
					InputStream inputStream = fileItemStream.openStream();
						
					// 检查是否为普通的表单域
//					if(fileItemStream.isFormField())
//					{
//						System.out.println("fieldname: " + fieldName);
//						System.out.println("filename: " + fileName);
//						System.out.println("data: " + Streams.asString(inputStream));
//					}
//					else 
//					{
//						System.out.println("fieldname: " + fieldName);
//						System.out.println("filename: " + fileItemStream.getName());
//						System.out.println("inputstream: " + inputStream);
//					}
					
					// 无论是普通的表单域，还是文件表单域，都传过去，由用户来处理
					jsonArray.add(exec(fieldName,fileName,inputStream));
				}
			} catch (FileUploadException | IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return jsonArray.toJSONString();
	}
	
	// 返回值为JSONObject，这样能够较好地处理多文件上传问题
	// 参数说明：
	// fieldName为前端表单域中的name属性的值
	// fileName为上传的文件名，如果当前表单域不是文件表单域，则fileName为null
	// inputStream为当前表单域的输入流
	public abstract JSONObject exec(String fieldName,String fileName,InputStream inputStream);
}
