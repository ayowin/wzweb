package wz.web.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.util.Streams;

import com.alibaba.fastjson.JSONObject;

import wz.web.WzFileUploadAPI;

public class WzFileUploadAPITest extends WzFileUploadAPI
{
	@Override
	public JSONObject exec(String fieldName,String fileName,InputStream inputStream)
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fieldName", fieldName);

		try
		{
			// 普通表单域
			if(fieldName.equals("textEdit"))
			{
				jsonObject.put("isFile", false);
				jsonObject.put("filename", fileName);
				jsonObject.put("data", Streams.asString(inputStream,"UTF-8"));
			}
			// 文件表单域
			else
			{
				jsonObject.put("isFile", true);
				jsonObject.put("filename", fileName);
				jsonObject.put("dataLength", inputStream.available());
				FileOutputStream fileOutputStream = null;
				if(fieldName.equals("firstFile"))
					fileOutputStream = new FileOutputStream("D:/firstFile");
				if(fieldName.equals("secondFile"))
					fileOutputStream = new FileOutputStream("D:/secondFile");
				
				int data;
				while( (data = inputStream.read()) != -1)
				{
					fileOutputStream.write(data);
				}
				fileOutputStream.close();
			}
			inputStream.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

}
