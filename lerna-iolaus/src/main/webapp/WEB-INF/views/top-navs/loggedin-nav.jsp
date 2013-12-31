<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="currentPage" type="java.lang.String" scope="request" />
	<!-- Nav -->
	<nav id="nav">
		<ul>
			<li><a href="${pageContext.servletContext.contextPath}/auth/home">Home</a></li>
			<li><a href="${pageContext.servletContext.contextPath}/auth/listInstances">Neo4J Instance Management</a>
				<ul>
					<li><a href="${pageContext.servletContext.contextPath}/auth/listInstances"><g>List Neo4J Instances</g></a></li>
					<li><a href="${pageContext.servletContext.contextPath}/auth/addInstance"><g>Add a Neo4J Instance</g></a></li>
				</ul></li>
			<li><a href="${pageContext.servletContext.contextPath}/auth/user/listuser">User Management</a></li>
		</ul>
	</nav>
