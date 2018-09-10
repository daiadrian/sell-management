<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>在线点餐后台管理系统</title>
    <link href="${pageContext.request.contextPath}/login_style/style_log.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/login_style/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/login_style/userpanel.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/login_style/jquery.ui.all.css">
</head>
<body class="login" mycollectionplug="bind">
<div class="login_m">
    <div class="login_logo">
        <h1>在线点餐后台管理系统</h1>
    </div>
    <div class="login_boder">
        <form action="${pageContext.request.contextPath}/login/login" method="post">
            <div class="login_padding" id="login_model">
                <h2>用户名</h2>
                <label>
                    <input type="text" name="username"  class="txt_input txt_input2">
                </label>
                <h2>密码</h2>
                <label>
                    <input type="password" name="password" class="txt_input">
                </label>
                <div class="rem_sub">
                    <label>
                        <input type="submit" class="sub_button" name="button" id="button" value="登录" style="opacity: 0.7;">
                    </label>
                </div>
            </div>
        </form>
    </div>
</div>
<br> <br>
<p align="center"> 仲恺出品 </p>
</body></html>