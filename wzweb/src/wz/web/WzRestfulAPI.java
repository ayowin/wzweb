package wz.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class WzRestfulAPI implements WzAPI
{
	// 实现WzAPI接口
	@Override
	public String exec(HttpServletRequest request)
	{
		try
		{
			InputStream inputStream = request.getInputStream();			
			byte[] bytes = new byte[2048];
			int length = inputStream.read(bytes);
			String requestContent = "";
			// 如果length>0，即：读取到数据
			if(length > 0)
			{
				// 如果length大于等于bytes数组的长度，说明内容过长，需要分段读取
				if(length >= bytes.length)
				{
					//先创建一个Byte链表arrayListByte
					ArrayList<Byte> arrayListByte = new ArrayList<Byte>();
					// 先将读到的字节保存到arrayListByte中
					for(byte b : bytes)
					{
						arrayListByte.add(b);
					}
					// 继续读取，直到没数据可读
					while( (length = inputStream.read(bytes)) > 0 )
					{
						// 再将读到的字节保存到arrayListByte中
						for(byte b : bytes)
						{
							arrayListByte.add(b);
						}
					}
					// 将arrayListByte转为数组
					bytes = new byte[arrayListByte.size()];
					int index = 0;
					for( Byte b : arrayListByte )
					{
						bytes[index] = b.byteValue();
						index++;
					}
					requestContent = new String(bytes,0,--index,"UTF-8");
				}
				else // 无需分段读取
				{				
					requestContent = new String(bytes,0,length,"UTF-8");
				}
			}
			//关闭流
			inputStream.close();
			
			Object object = JSON.parse(requestContent);
			//打印输出
//			if(object instanceof JSONObject)
//			{
//				System.out.println("JSONObject: " + object); // 不格式化字符串
//				System.out.println("JSONObject: " + JSON.toJSONString(object, true) ); // 格式化
//			}
//			if(object instanceof JSONArray)
//			{
//				System.out.println("JSONObject: " + object ); // 不格式化字符串
//				System.out.println("JSONArray: " + JSON.toJSONString(object, true) ); // 格式化
//			}
			
			//调用public Object exec(Object jsonRequest)，并将其返回值返回
			return exec(object).toString();
		} catch (IOException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	// 子类需实现此函数，参数和返回值均为JSON对象，即：JSONObject或JSONArray
	public abstract Object exec(Object jsonRequest);
}
