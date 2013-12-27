<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>

<div class="title">
	<h2>Add new user</h2>
	<span class="byline">Enter user information</span>
</div>


<form:form name="addUserForm" method="POST" modelAttribute="userBackingBean" action="${pageContext.servletContext.contextPath}/auth/user/adduser">

	<table class="form">
		<tr>
			<td width="100">Name:</td>
			<td><form:input type='text' path='name' name ='name' value='${userBackingBean.name}'/></td>
			<td><font color="red"><form:errors path="name" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td>Username:</td>
			<td><form:input type='text' path='username' name ='username' value='${userBackingBean.username}' /></td>
			<td><font color="red"><form:errors path="username" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><form:input type='password' name='password' path='password' /></td>
			<td><font color="red"><form:errors path="password" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><form:input type='text' path='email' name='email' value='${userBackingBean.email}' /></td>
			<td><font color="red"><form:errors path="email" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td valign="top">Roles:</td><td><form:checkboxes element="li" items="${availableRoles}" itemLabel="name" itemValue="id" path="roles" /></td>
			<td><font color="red"><form:errors path="roles" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td colspan='4'><input name="submit" type="submit"
				value="Add user" /></td>
		</tr>
	</table>

</form:form>