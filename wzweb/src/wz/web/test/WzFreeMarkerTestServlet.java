package wz.web.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import wz.web.util.WzFreeMarkerUtil;

@WebServlet("/template.html")
public class WzFreeMarkerTestServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		Template template = WzFreeMarkerUtil.getTemplate("template.html");
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("framework", "wzweb");
		hashMap.put("author", "欧阳伟");
		hashMap.put("date", "2017-9-13");
		
		try
		{
//			template.process(hashMap,new OutputStreamWriter(System.out));
			resp.setCharacterEncoding("UTF-8");
			template.process(hashMap,resp.getWriter());
		} catch (TemplateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
	}
}
