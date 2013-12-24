
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<header>
	<h2>User Management</h2>
	<span class="byline">Users you could edit, delete and add.</span>
</header>

<input type=button
	onClick="location.href='${pageContext.servletContext.contextPath}/auth/adduser'"
	value='Add User'>