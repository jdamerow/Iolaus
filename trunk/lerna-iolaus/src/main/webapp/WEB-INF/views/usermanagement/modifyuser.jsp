<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

<style type="text/css">
.response{
	color:blue;
	margin-left:-170px;
	margin-bottom:10px;
	text-decoration:underline;
}

</style>

<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/form.css" />
	<br/>
	<br/>

<form:form name="modifyUserForm"  autocomplete="off" class="form" method="POST"
	modelAttribute="modifyUserBackingBean"
	action="${pageContext.servletContext.contextPath}/auth/user/modifyuser/${username}">
	<div id="form">
		<table>
			<tr>
				<td colspan="3" align="left"><h1>Modify a User</h1></td>
			</tr>
			<tr>
				<td width="20%"><label>Name:</label></td>
				<td><form:input type='text' class="text" path='name'
						name='name' /></td>
				<td><font color="red"><form:errors path="name"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td><label>Username:</label></td>
				<td><form:input type='text' class="text" path='username'
						name='username' /></td>
				<td><font color="red"><form:errors path="username"
							cssClass="errors" />${errorMsg}</font></td>
			</tr>
			<tr>
				<td><label>Password:</label></td>
				<td><input type='password' disabled="disabled" class="text"  value="*********************" /></td>
				<td align="left"><a class="response" href="'${pageContext.servletContext.contextPath}/auth/user/changepasswd/${username}'">Change Password</a></td>
			</tr>
			<tr>
				<td><label>Email:</label></td>
				<td><form:input type='email' class="text" path='email'
						name='email' /></td>
				<td><font color="red"><form:errors path="email"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td><label>Role:</label></td>
				<td><form:checkboxes class="checkbox" element="li"
						items="${availableRoles}" itemLabel="name" itemValue="id"
						path="roles" /></td>
				<td><font color="red"><form:errors path="roles"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td></td>
				<td colspan='3' align="left"><input class="submituser"
					name="submit" type="submit" value="Modify user" />
				<input type=button class="canceluser"
					onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/listuser'"
					value='Cancel'/>
			</tr>
		</table>
	</div>
</form:form>
