<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<style type="text/css">
.response{
	color:red;
	margin-left:0px;
}
</style>

<script type="text/javascript">
<!--
// Form validation code will come here.
function validate()
{
   
	var hostName = new RegExp("^([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])(\.([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]{0,61}[a-zA-Z0-9]))*$");
   if( document.addInstanceForm.port.value == "" || isNaN(document.addInstanceForm.port.value) || document.addInstanceForm.port.value>99999)
   {
     alert( "Please provide valid Port Number!");
     document.addInstanceForm.port.focus() ;
     return false;
   }
   if( document.addInstanceForm.host.value == "" || !document.addInstanceForm.host.value.match(hostName))
   {
     alert( "Please provide valid address of host machine!" );
     document.addInstanceForm.host.focus() ;
     return false;
   }
   if( document.addInstanceForm.description.value == "" )
   {
     alert( "Please provide description!" );
     document.addInstanceForm.description.focus() ;
     return false;
   }
   return( true );
}
//-->
</script>

<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/form.css" />
<form name="addInstanceForm" class="form" action="updateInstance" method="POST" onsubmit="return(validate());">
<div id="form">
	<table>
	<c:if test="${failure}">
			<tr><td colspan="2" align="center">
			<h5 class="response">**Failure</h5></td></tr>
	</c:if>
	<tr> <td colspan="2" align="left"><h1>Edit a Neo4j Instance</h1></td></tr>
	<tr><td colspan="2" align="left"><c:if test="${flag}">
	<h5 class="response">**Port ${instance.port} is already in used on host machine ${instance.host}</h5>
	</c:if></td></tr>
	<tr>
			<td><label>ID:</label></td>
			<td><input class ="text" type="text" name="id" value="${instance.id}" readonly /></td>
			
	</tr>	
	<tr>
			<td><label>Port Number:</label></td>
			<td><input class ="text" type="text" name="port" value="${instance.port}" /></td>
			
	</tr>		
	<tr>		
			<td><label>Host:</label></td>
			<td><input class ="text" type="text" name="host" value="${instance.host}"/></td>		
	</tr>	
	<tr>
			<td><label>Description:</label></td>
			<td><textarea class ="description" name="description"><c:out value="${instance.description}"></c:out></textarea></td>
	</tr>
	<tr>		
			<td><label>Activate Now</label></td>
			<c:choose>
				<c:when test="${instance.active}">
					<td><input class ="checkbox" type="checkbox" name="active" checked/></td>
				</c:when>
				<c:otherwise>
					<td><input class ="checkbox" type="checkbox" name="active" /></td>
				</c:otherwise>
			</c:choose>		
	</tr>
	<tr>	
			<td class="submit" colspan="2" align="left"><input type="submit" value="Update" /></td>
	</tr>
	</table>
	</div>
</form>