<%@page import="edu.asu.lerna.iolaus.domain.implementation.Role"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

<style type="text/css">
 
 .submit {
 
	background-color: #808080;
	color: #FFFFFF;
	width: 130px;
	height: 30px;
	weight: bold;
	border-radius: 10px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	margin-left:60px;
	margin-top:8px;
}


#form h1 {
	margin-left: 140px;
	color: #474E69;
	font-size:25px;
	font-weight:bold;
}

.cancel{
 
	background-color: #808080;
	color: #FFFFFF;
	width: 80px;
	height: 30px;
	weight: bold;
	border-radius: 10px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	margin-top:8px;
	margin-left: 10px;
}

#form table{
	width:40%;
	margin-left:150px;
	margin-bottom:200px;
}

input:hover, textarea:hover,
input:focus, textarea:focus {
	border-color: 1px solid #C9C9C9;
	box-shadow: rgba(0, 0, 0, 0.2) 0px 0px 8px;
	-moz-box-shadow: rgba(0, 0, 0, 0.2) 0px 0px 8px;
	-webkit-box-shadow: rgba(0, 0, 0, 0.2) 0px 0px 8px;	
}

input {
	padding: 2px;
	border: 1px solid #E5E5E5;
	box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 8px;
	-moz-box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 8px;
	-webkit-box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 8px;		
}

.text input{
	width: 200px;
}
.checkbox input{
	width:10 px;	
}


#form label {
	margin-left: 10px;
	color: #000000;
	font-size: 15px;
	font-weight:bold;
	font-style: Verdana, Tahoma, sans-serif;
}

</style>

<br />
<br />

<form:form name="modifyUserForm" autocomplete="off" class="form"
	method="POST" modelAttribute="changePasswdBackingBean"
	action="${pageContext.servletContext.contextPath}/auth/user/changepasswd/${username}">
	<div id="form">
	<h1>Change password of User :
						${username}</h1>
		<table>
			<tr>
				<td><label>New password:</label></td>
				<td><form:input type='password' class="text"
						name='somepassword1' path='newpassword' />
			<font color="red"><form:errors path="newpassword"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<td><label>Repeat password:</label></td>
				<td><form:input type='password' class="text"
						name='somepassword' path='repeatpassword' />
				<font color="red">${errorMsg}<form:errors path="repeatpassword"
							cssClass="errors" /></font></td>
			</tr>
			<tr>
				<br/>
				<td colspan='2' align="left"><input class="submit"
					name="submit" type="submit" value="Change Password" /><input class="cancel" type=button
					onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/modifyuser/${username}'"
					value='Cancel'/></td>
			</tr>
		</table>
	</div>
</form:form>
