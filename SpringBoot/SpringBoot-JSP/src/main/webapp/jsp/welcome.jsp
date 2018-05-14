<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- 链接bootstrap -->
		<!-- 
			href中的内容写为：webjars/bootstrap/3.3.7/css/bootstrap.css
			加载地址为：http://localhost:8080/SpringBoot-JSP/webjars/bootstrap/3.3.7/css/bootstrap.css
			href中的内容写为：/webjars/bootstrap/3.3.7/css/bootstrap.css
			加载地址为：http://localhost:8080/webjars/bootstrap/3.3.7/css/bootstrap.css
			使用中建议写为webjars/bootstrap/3.3.7/css/bootstrap.css
		-->
		<link rel="stylesheet" type="text/css" href="webjars/bootstrap/3.3.7/css/bootstrap.css"/>
		<%-- <spring:url value="/css/main.css" var="springCss"/>
		<link href="${springCss}" rel="stylesheet"/> --%>
		<script type="text/javascript" src="webjars/jquery/1.11.1/jquery.min.js"></script>
		<c:url value="/css/main.css" var="jstlCss"></c:url>
	    <link href="${jstlCss }" rel="stylesheet"/>
		<title>SpringBootTest</title>
	</head>
	<body> 
		<nav class="navbar navbar-inverse">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand" href="#">Spring Boot</a>
				</div>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav nav-bar-nav">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#about">About</a></li>
				</ul>
			</div>
		</nav>
		<div class="container">
			<div class="starter-template">
				<h1>Spring Boot Web JSP Example</h1>
				<h2>Message: ${message }</h2>
			</div>
		</div>
		<script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	</body>
</html>
