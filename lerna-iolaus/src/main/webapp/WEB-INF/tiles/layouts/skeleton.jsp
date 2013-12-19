<!DOCTYPE HTML>
<!--
	Helios 1.5 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
<head>
<title>Helios by HTML5 UP</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<link
	href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600"
	rel="stylesheet" type="text/css" />
<script
	src="js/jquery.min.js"></script>
<script
	src="js/jquery.dropotron.min.js"></script>
<script
	src="js/skel.min.js"></script>
<!-- <script src="js/skel-panels.min.js"></script>-->
<script
	src="js/init.js"></script>
<link rel="stylesheet"
	href="css/skel-noscript.css" />
<link rel="stylesheet"
	href="css/style.css" />
<link rel="stylesheet"
	href="css/style-desktop.css" />
<link rel="stylesheet"
	href="css/style-noscript.css" />
</head>
<body class="no-sidebar">

	<!-- Header -->
	<div id="header">

		<!-- Nav -->
		<nav id="nav">
			<ul>
				<li><a href="index.html">Home</a></li>
				<li>
					<span>Neo4J Instance Management</span>
					<ul>
						<li><a href="#"><g>List Neo4J Instance</g></a></li>
						<li><a href="#"><g>Add Neo4J Instance</g></a></li>
						<li><a href="#"><g>Edit Neo4J Instance</g></a></li>
						<li><a href="#"><g>Delete Neo4J Instance</g></a></li>
					</ul>
				</li>
				<li><a href="right-sidebar.html">User Management</a></li>
			</ul>
		</nav>
		


	</div>

	<tiles:importAttribute name="currentPage" scope="request" />

	<!-- Header -->
	<header id="header">
		<div class="logo">
			<div>
				<h1>
					<a href="#" id="logo">Lerna</a>
				</h1>
				<span class="byline">- Neo4J Management</span>
			</div>
		</div>
	</header>
	<!-- /Header -->

	<!-- Nav -->
	<tiles:insertAttribute name="navigation" />
	<!-- /Nav -->

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
							<li>&copy; Untitled. All rights reserved.</li>
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