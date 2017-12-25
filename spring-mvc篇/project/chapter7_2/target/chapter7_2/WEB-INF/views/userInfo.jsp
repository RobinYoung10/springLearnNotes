<%--
  Created by IntelliJ IDEA.
  User: robin
  Date: 17-12-25
  Time: 下午1:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户</title>
</head>
<body>
    <h3>${requestScope.user.username}</h3>
    <a href="download?filename=${requestScope.user.image.originalFilename}">${requestScope.user.image.originalFilename}</a>
</body>
</html>
