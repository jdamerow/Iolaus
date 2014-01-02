<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>

<style type="text/css">
.submit {
	background-color: #808080;
	color: #FFFFFF;
	width: 120px;
	height: 30px;
	border-radius: 3px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	margin-bottom: 5px;
}
</style>

<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/form.css" />

<form:form name="addUserForm" class="form" method="POST" modelAttribute="userBackingBean" action="${pageContext.servletContext.contextPath}/auth/user/adduser">
<div id="form">
	<table>
		<tr> <td colspan="3" align="left"><h1>Add a User</h1></td></tr>
		<tr>
			<td width="20%"><label>Name:</label></td>
			<td><form:input type='text' class="text" path='name' name ='name' value=''/></td>
			<td><font color="red"><form:errors path="name" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td><label>Username:</label></td>
			<td><form:input type='text' class="text" path='username' name ='someuser' value='' /></td>
			<td><font color="red"><form:errors path="username" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td><label>Password:</label></td>
			<td><form:input type='password' class="text" name='somepassword' path='password' /></td>
			<td><font color="red"><form:errors path="password" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td><label>Email:</label></td>
			<td><form:input type='email' class="text" path='email' name='email' value='' /></td>
			<td><font color="red"><form:errors path="email" cssClass="errors" /></font></td>
		</tr>
		<tr>
			<td><label>Role:</label></td>
			<td><form:checkboxes class ="checkbox" element="li" items="${availableRoles}" itemLabel="name" itemValue="id" path="roles" /></td>
			<td><font color="red"><form:errors path="roles" cssClass="errors" /></font></td>
		</tr>
		<tr>
		<td></td>
			<td colspan='2' align="left"><input class="submit" name="submit" type="submit"
				value="Add user" />
				<input class="submit" type=button
					onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/listuser'"
					value='Cancel'/></td>
		</tr>
	</table>
</div>
</form:form>
