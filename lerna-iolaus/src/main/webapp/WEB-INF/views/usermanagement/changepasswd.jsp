<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

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

<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/form.css" />
<br />
<br />
<input class="submit" type=button
	onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/changepasswd'"
	value='Change password' />

<form:form name="modifyUserForm" autocomplete="off" class="form"
	method="POST" modelAttribute="changePasswdBackingBean"
	action="${pageContext.servletContext.contextPath}/auth/user/changepasswd/${username}">
	<div id="form">
		<table>
			<tr>
				<td colspan="2" align="left"><h1>Change password of User :
						${username}</h1></td>
			</tr>
			<tr>
				<td><label>New password:</label></td>
				<td><form:input type='password' class="text"
						name='somepassword1' path='newpassword' /></td>
				<td><font color="red"><form:errors path="newpassword"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td><label>Repeat password:</label></td>
				<td><form:input type='password' class="text"
						name='somepassword' path='repeatpassword' /></td>
				<td><font color="red"><form:errors path="repeatpassword"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td colspan='2' align="left"><input class="submit"
					name="submit" type="submit" value="Change passwd user" /><input class="submit" type=button
					onClick="location.href='${pageContext.servletContext.contextPath}/auth/editInstance/${instance.id}'"
					value='Cancel'/></td>
			</tr>
		</table>
	</div>
</form:form>
