package wz.web.test;

import javax.servlet.http.HttpServletRequest;

import wz.web.WzAPI;

public class WzAPITest implements WzAPI
{
	@Override
	public String exec(HttpServletRequest request)
	{
		return "Welcome to the wzweb framework.";
	}
}
