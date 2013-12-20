<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="currentPage" type="java.lang.String" scope="request" />
	<!-- Nav -->
	<nav id="nav">
		<ul>
			<li><a href="#">Home</a></li>
			<li><span>Neo4J Instance Management</span>
				<ul>
					<li><a href="#"><g>List Neo4J Instances</g></a></li>
					<li><a href="#"><g>Add a Neo4J Instance</g></a></li>
					<li><a href="#"><g>Edit a Neo4J Instance</g></a></li>
					<li><a href="#"><g>Delete a Neo4J Instance</g></a></li>
				</ul></li>
			<li><a href="#">User Management</a></li>
		</ul>
	</nav>
