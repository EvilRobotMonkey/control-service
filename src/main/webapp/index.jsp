<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>App 后台控制</title>
</head>
<script type="text/javascript" src="<%=basePath %>/js/swfobject.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/ParsedQueryString.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/public.js"></script>
<body>
<div id="GrindPlayer"></div>
<br>
<div id="dv" />
<input type="button" value="连接" onclick="hello()" />
<input type="text" id ="msg" /><input type="button" onclick="subsend()" value="发送" />
</body>
</html>