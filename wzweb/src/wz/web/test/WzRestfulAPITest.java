package wz.web.test;

import wz.web.WzRestfulAPI;

public class WzRestfulAPITest extends WzRestfulAPI
{
	@Override
	public Object exec(Object jsonRequest)
	{
		return jsonRequest.toString();
	}
}
