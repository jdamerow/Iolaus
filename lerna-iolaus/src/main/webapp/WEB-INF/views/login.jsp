<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/login.css" />

<style>
p.ex1 {
	padding: 0cm;
}

p.ex2 {
	padding: 0cm;
}
</style>
<br/>
<span onload='document.f.j_username.focus();'>
	<heading>Login</heading>
	<p class="ex2" /> <c:if test="${not empty error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
<div id="login">
	<form name='f' action="<c:url value='j_spring_security_check' />"
		method='POST'>
		
			<table>
				<tr>
					<td><h1>User:</h1></td>
					<td><input type='text' name='j_username' value=''></td>
				</tr>
				<tr>
					<td><h1>Password:</h1></td>
					<td><input type='password' name='j_password' /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td colspan='2'><input name="submit" type="submit"
						value="submit" /></td>
				</tr>
			</table>
		
	</form>
</div>
</span>
<br/>
<br/>