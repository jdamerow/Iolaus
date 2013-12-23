<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>

<div class="title">
	<h2>Add new user</h2>
	<span class="byline">Enter user information</span>
</div>

<form:form method="post" modelAttribute="userBackingBean" action="${pageContext.servletContext.contextPath}/auth/user/addUser">

	<table class="form">
		<tr>
			<td width="100">Name:</td>
			<td><form:input type='text' path='name' value=''/></td>
			<td><form:errors path="name" cssClass="errors" /></td>
		</tr>
		<tr>
			<td>Username:</td>
			<td><form:input type='text' path='username' value='' /></td>
			<td><form:errors path="username" cssClass="errors" /></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><form:input type='text' path='email' value='' /></td>
			<td><form:errors path="email" cssClass="errors" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><form:input type='password' path='password' /></td>
			<td><form:errors path="password" cssClass="errors" /></td>
		</tr>
		<tr>
			<td valign="top">Roles:</td><td><form:checkboxes element="li" items="${availableRoles}" itemLabel="name" itemValue="id" path="roles" /></td>
			<td><form:errors path="roles" cssClass="errors" /></td>
		</tr>
		<tr>
			<td colspan='4'><input name="submit" type="submit"
				value="Add user" /></td>
		</tr>
	</table>

</form:form>
