<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数据库表结构导出</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/Servlet/ExcelServlet" enctype="multipart/form-data" method="get">
    连接数据库地址：<input type="text" name="address"><br/>
    数据库端口号：  <input type="text" name="point"><br/>
    数据库名：      <input type="text" name="dataName"><br/>
    连接用户名：    <input type="text" name="username"><br/>
    连接密码：      <input type="password" name="password"><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
