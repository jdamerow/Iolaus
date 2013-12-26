<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

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

<link rel="stylesheet" href="css/form.css" />
<div id="form">
	<table>
	<tr> <td colspan="2" align="left"><h1>Add a Neo4j Instance</h1></td></tr>
	<form name="addInstanceForm" class="form" action="addInstance" method="POST" onsubmit="return(validate());">
	<tr>
			<td><label>Port Number:</label></td>
			<td><input class ="text" type="text" name="port" /></td>
			
	</tr>		
	<tr>		
			<td><label>Host:</label></td>
			<td><input class ="text" type="text" name="host" /></td>		
	</tr>	
	<tr>
			<td><label>Description:</label></td>
			<td><textarea class ="description" name="description"/></textarea></td>
	</tr>
	<tr>		
			<td><label>Activate Now</label></td>
			<td><input class ="checkbox" type="checkbox" name="active" /></td>		
	</tr>
	<tr>	
			<td class="submit" colspan="2" align="left"><input type="submit" value="Add" /></td>
	</tr>
	</form>
	</table>
	</div>