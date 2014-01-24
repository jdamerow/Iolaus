<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE HTML>

<html>
<head>
<title><tiles:insertAttribute name="title" /></title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<link
	href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600"
	rel="stylesheet" type="text/css" />
<script src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/html5shiv.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery.dropotron.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/skel.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/config.js"></script>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/skel-noscript.css" />
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style.css" />
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style-1000px.css" />
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style-desktop.css" />
<link rel="stylesheet"	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style-mobile.css" />


</head>
<body class="no-sidebar">
	<tiles:importAttribute name="currentPage" scope="request" />
	<!-- Header -->
	<div id="header">
		<!-- Nav -->
		<tiles:insertAttribute name="navigation" />
		<!-- /Nav -->
		<!-- Inner -->
		<div class="inner">
			<header>
				<h1>
					<a href="#" id="logo">Herculus</a>
				</h1>
				<span class="byline">Neo4J Management</span>
			</header>
		</div>

	</div>
	<!-- Header -->

	<!-- Main -->


	<!-- Main -->
	<div id="main-wrapper">
		<div id="main" class="container">
			<sec:authorize access="isAuthenticated()">
				<div>
					<div class="loggedInMsg">
						Welcome <span class="user" style="margin-left: 5px;"><sec:authentication
								property="principal.username" /></span>!
					</div>
					<div class="loggedOutLink">
						<a href="<c:url value='/j_spring_security_logout' />">Logout</a>
					</div>
					<hr class="clearLoggedIn">
				</div>
			</sec:authorize>
			<div class="row">
				<div class="9u skel-cell-mainContent">
					<div class="content content-left">
						<!-- Content -->

						<article class="is-page-content">

							<tiles:insertAttribute name="content" />

						</article>

						<!-- /Content -->

					</div>
				</div>
				<div class="3u">
					<div class="sidebar">

						<!-- Sidebar -->

						<!-- Recent Posts -->
						<tiles:insertAttribute name="sub-navigation" />
						<!-- /Recent Posts -->
						<!-- /Sidebar -->

					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Footer -->
	<div id="footer">
		<div class="container">
			<div class="row">
				<div class="12u">
					<!-- Copyright -->
					<div class="copyright">
						<ul class="menu">
							<li>&copy;2013 Digital Innovation Group</li>
							<li>Design: <a href="http://html5up.net/">HTML5 UP</a></li>
							<li>Demo Images: <a
								href="http://mdomaradzki.deviantart.com/">Michael Domaradzki</a></li>
						</ul>
					</div>
				</div>

			</div>
		</div>
	</div>

</body>
</html>