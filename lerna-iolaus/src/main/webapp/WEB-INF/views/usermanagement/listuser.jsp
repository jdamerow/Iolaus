
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<header>
	<h2>User Management</h2>
	<span class="byline">Users you could edit, delete and add.</span>
</header>




<!--  
	Author Lohith Dwaraka  
	Used to list the users
-->


<script type="text/javascript" charset="utf8">
	$(document).ready(function() {
		$('#selectall').click(function() {
			$('.selected').prop('checked', isChecked('selectall'));
		});
	});
	function isChecked(checkboxId) {
		var id = '#' + checkboxId;
		return $(id).is(":checked");
	}
	function resetSelectAll() {
		// if all checkbox are selected, check the selectall checkbox
		// and viceversa
		if ($(".selected").length == $(".selected:checked").length) {
			$("#selectall").attr("checked", "checked");
		} else {
			$("#selectall").removeAttr("checked");
		}

		if ($(".selected:checked").length > 0) {
			$('#edit').attr("disabled", false);
		} else {
			$('#edit').attr("disabled", true);
		}
	}
	$(document).ready(function() {
		$("input[type=submit]").button().click(function(event) {

		});
	});
	$(document).ready(function() {
		$("input[type=a]").button().click(function(event) {
			event.preventDefault();
		});
	});
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("ul.pagination1").quickPagination({
			pageSize : "10"
		});
		$("ul.pagination2").quickPagination({
			pageSize : "10"
		});

	});
</script>
<script type="text/javascript" charset="utf8">
	$(document).ready(function() {
		activeTable = $('.dataTable').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});
	});
	$(document).ready(function() {
		$("input[type=button]").button().click(function(event) {
			event.preventDefault();
		});
	});
</script>



<br />
<div class="container">
	<c:choose>
		<c:when test="${not empty userList}">

			<form method="POST">
				<input type=button
					onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/adduser'"
					value='Add User'> <input type="submit" value="Delete User"
					onclick="this.form.action='${pageContext.servletContext.contextPath}/auth/user/deleteUser'" />

				<span class="inline">List of Users.</span>
				<hr />
				<table style="width: 100%" cellpadding="0" cellspacing="0"
					border="0" class="display dataTable" id="listuser">
					<thead>
						<tr>
							<th align="left"><input type="checkbox" id="selectall">
								All</th>
							<th align="left">Name</th>
							<th>User Name</th>
							<th>Email ID</th>
							<th>Role</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="user" items="${userList}">
							<tr>
								<td><input type="checkbox" class="selected" name="selected"
									value='<c:out value="${user.username}"></c:out>' /></td>
								<td width="25%" align="left"><c:out value="${user.name}"></c:out></td>
								<td width="25%" align="center"><input name="usernames"
									type="hidden" value="<c:out value="${user.username}"></c:out>" />
									<c:out value="${user.username}"></c:out></td>
								<td width="25%" align="center"><c:out value="${user.email}"></c:out></td>
								<td width="25%" align="center"><c:forEach var="authorities"
										items="${user.authorities}">
										<c:out value="${authorities.authority}"></c:out> :
										</c:forEach></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</c:when>
		<c:otherwise>
			<input type=button
				onClick="location.href='${pageContext.servletContext.contextPath}/auth/user/adduser'"
				value='Add User'>
			<hr />
			<br />
			No Users
		</c:otherwise>
	</c:choose>
</div>
