<%--
  Created by IntelliJ IDEA.
  User: William
  Date: 2015/1/19
  Time: 18:48
  To change this template use File | Settings | File Templates.

  在启用CSRF安全策略之后，SpringSecurity要求必须以POST方式请求/logout,因此需要添加本signout
  页面,构造一个form表单,action指向/logout，该url将被Spring Security拦截并处理.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>SevenDev-alpha</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <meta name="description" content="William&Nicole family clothes management system."/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <sec:csrfMetaTags />

    <link href="content/bootstrap.css" rel="stylesheet"/>
    <link href="content/font-awesome.css" rel="stylesheet"/>
</head>
<body>
    <form action="/logout" method="post">
        <div class="form-group">
            <button type="submit" class="btn btn-default btn-lg">Confirm to Logout</button>
        </div>
        <sec:csrfInput />
    </form>
</body>
</html>
