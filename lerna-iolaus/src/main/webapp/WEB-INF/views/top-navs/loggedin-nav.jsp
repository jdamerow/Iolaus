<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="currentPage" type="java.lang.String" scope="request" />
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Nav -->
<nav id="nav" class="skel-ui-fixed">
	<ul>
		<sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/home">Home</a></li>
		</sec:authorize>
		<sec:authorize access="hasAnyRole('ROLE_USER','ROLE_ADMIN')">
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/listInstances">Neo4J
					Instance Management</a>
				<ul>
				
					<li><a
						href="${pageContext.servletContext.contextPath}/auth/listInstances"><g>List
							Neo4J Instances</g></a></li>
					<sec:authorize access="hasAnyRole('ROLE_ADMIN')"><li><a
						href="${pageContext.servletContext.contextPath}/auth/addInstance"><g>Add
							a Neo4J Instance</g></a></li></sec:authorize>
				</ul></li>
		</sec:authorize>
		<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/user/listuser">User
					Management</a></li>
		</sec:authorize>
	</ul>
</nav>
