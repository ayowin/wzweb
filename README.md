# wzweb
轻量级web框架：wzweb


使用指南：

  1、新建一个服务类，实现WzAPI接口，或自行添加并实现函数：public String exec(HttpServletRequest request)
  2、在wzweb.xml中，添加配置。（如有疑惑，参考WzAPITest示例。）
  3、如果您需要的是restful规范的api，本框架提供了默认的restful框架 "WzRestfulAPI" ，WzRestfulAPI使用fastjson作为JSON库，需要您稍微学习一下           fastjson。
  您只需要继承WzRestfulAPI并重写函数：public Object exec(Object jsonRequest)
  （说明：一般地，参数和返回值均为JSONObject或JSONArray，如有疑惑，参考WzRestfulAPITest示例。）
  4、本框架提供了默认的文件上传框架 "WzFileUploadAPI" ，继承WzFileUploadAPI，实现JSONObject exec(String fieldName,String fileName,InputStream   inputStream)函数即可，
  参数分别为：表单域的name属性值，文件名，表单域的输入流。（如有疑惑，参考WzFileUploadAPITest示例。）
  5、引入FreeMarker技术，以html文件做为模板，以servlet为模板加载真实的数据，并应答给客户端。（如有疑惑，参考WzFreeMarkerTestServlet示例。）
