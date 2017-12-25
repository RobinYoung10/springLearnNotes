<%--
  Created by IntelliJ IDEA.
  User: robin
  Date: 17-12-25
  Time: 下午12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
</head>
<body>
    <h2>用户注册</h2>
    <form action="register" enctype="multipart/form-data" method="post">
        用户名:<input type="text" name="username"><br>
        请上传头像:<input type="file" name="image"><br>
        <input type="submit" value="注册">
    </form>
</body>
</html>
