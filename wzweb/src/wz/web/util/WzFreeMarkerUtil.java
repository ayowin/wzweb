package wz.web.util;

import java.io.File;
import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class WzFreeMarkerUtil
{
	private static Configuration config = null;
	private static String appPath;
	
	// 取得FreeMarker模板，templatePath为相对于web根目录的相对路径
	public static Template getTemplate(String templatePath) throws IOException
	{
		// FreeMarker框架要求，Configuration对象应为单例，否则会严重影响性能
		// 如果config为空，则先初始化config
		if(config == null)
		{
			config = new Configuration(Configuration.VERSION_2_3_28);
			config.setDirectoryForTemplateLoading(new File(appPath));
			config.setDefaultEncoding("UTF-8");
			config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			config.setLogTemplateExceptions(false);
			config.setWrapUncheckedExceptions(true);
		}
		return config.getTemplate(templatePath);
	}

	public static String getAppPath()
	{
		return appPath;
	}
	
	public static void setAppPath(String appPath)
	{
		WzFreeMarkerUtil.appPath = appPath;
	}
}
