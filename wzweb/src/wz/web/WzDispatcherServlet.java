package wz.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import wz.web.util.WzFreeMarkerUtil;

/*
 * 		wzweb框架
 * 		作者：欧阳伟
 * 		日期：2017-9-13
 * 		使用方法：
 * 				1、新建一个服务类，实现WzAPI接口，或自行添加并实现函数：public String exec(HttpServletRequest request)
 * 				2、在wzweb.xml中，添加配置。（如有疑惑，参考WzAPITest示例。）
 * 				3、如果您需要的是restful规范的api，本框架提供了默认的restful框架 "WzRestfulAPI" ，WzRestfulAPI使用fastjson作为JSON库，需要您稍微学习一下fastjson。
 * 				您只需要继承WzRestfulAPI并重写函数：public Object exec(Object jsonRequest)
 * 				（说明：一般地，参数和返回值均为JSONObject或JSONArray，如有疑惑，参考WzRestfulAPITest示例。）
 * 				4、本框架提供了默认的文件上传框架 "WzFileUploadAPI" ，继承WzFileUploadAPI，实现JSONObject exec(String fieldName,String fileName,InputStream inputStream)函数即可，
 * 				参数分别为：表单域的name属性值，文件名，表单域的输入流。（如有疑惑，参考WzFileUploadAPITest示例。）
 * 		注：本框架的编写，参考了Spring MVC的restful设计、邵发老师的restful框架
 */

@WebServlet(value = "*.api", loadOnStartup = 1)
public class WzDispatcherServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	// 存储wzweb.xml的HashMap，只读不写
	private HashMap<String, String> wzWebXmlHashMap = new HashMap<String, String>();

	// 初始化时，读取wzweb.xml，并将内容存入wzWebXml
	@Override
	public void init() throws ServletException
	{	
		// 取得web应用的根目录，传给WzFreeMarkerUtil工具类
		WzFreeMarkerUtil.setAppPath(getServletContext().getRealPath("/"));
		
		try
		{
			loadWzWebXML();
		} catch (DocumentException | IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			// 根据配置文件，选择相应的业务类
			String apiName = request.getServletPath();
			// 去掉第一个'/'字符，去掉最后一个".api"字符串，得到真实的api名称
			apiName = apiName.substring(apiName.indexOf("/") + 1, apiName.lastIndexOf(".api"));
			// 根据api名称，获取对应的类的信息
			Class<?> c = Class.forName(wzWebXmlHashMap.get(apiName));
			// 创建该类的一个对象
			Object object = c.newInstance();

			// 获取该类的函数
//			Method[] methods = c.getDeclaredMethods();
			Method[] methods = c.getMethods();
			// 遍历所有函数
			for (Method method : methods)
			{
				// 寻找想要的那个函数
				// 名字匹配，返回值匹配
				if (method.getName().equals("exec") && method.getReturnType() == String.class)
				{
					// 获取函数的参数
					Class<?>[] parameterTypes = method.getParameterTypes();
					// 参数的个数匹配，参数的类型匹配
					if (parameterTypes.length == 1 && parameterTypes[0] == HttpServletRequest.class)
					{
						// 找到想要的函数，调用该函数
						// 先设置为可访问
						method.setAccessible(true);
						// 调用函数，执行业务，取得业务的应答内容
						String responseContent = (String) method.invoke(object, request);
						// 发送应答给客户端
						response.setCharacterEncoding("UTF-8");
						response.setContentType("text/plain");
						// response.setHeader("Connection", "close");
						Writer writer = response.getWriter();
						writer.write(responseContent);
						writer.close();
					}
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	// 加载wzweb.xml
	private String loadWzWebXML() throws DocumentException, IOException
	{
		// 加载文件
		InputStream inputStream = this.getClass().getResourceAsStream("/wzweb.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputStream);
		inputStream.close();

		// 找api配置
		Element root = document.getRootElement();
		List<Element> elements = root.elements("api");
		for (Element element : elements)
		{
			// 如果wzWebXmlHashMap中还没有该api，则增加该api
			if( !wzWebXmlHashMap.containsKey(element.attributeValue("name")) )
				wzWebXmlHashMap.put(element.attributeValue("name"), element.attributeValue("class"));
		}
//		System.out.println(wzWebXmlHashMap);
		return "";
	}
	
}
