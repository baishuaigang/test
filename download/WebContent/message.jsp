<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
  <!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消息提示</title>
</head>
<body>
 ${message}
 <a href="${pageContext.request.contextPath}/servlet/ListFileServlet">查看文件</a>
</body>
</html>