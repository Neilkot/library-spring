<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<!DOCTYPE html>
<head>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="js/md5.js"></script>
<script src="js/globalVars.js"></script>
</head>

<c:choose>
	<c:when test="${cookie.get('locale').value == 'en'}">
		<fmt:setLocale value='en' scope="session" />
	</c:when>
	<c:otherwise>
		<fmt:setLocale value='ua' scope="session" />
	</c:otherwise>
</c:choose>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="locale" />

<ul class="nav">
	<c:choose>
		<c:when test="${empty userSession.roleType}">
			<li class="nav-item">
				<button type="button" class="btn btn-primary" data-toggle="modal"
					onclick="location.href='/reader-book'">
					<fmt:message key="header.home" />
				</button>
			</li>
			<li class="nav-item">
				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#login-modal"><fmt:message key="header.login" /></button>
			</li>
			<li class="nav-item">
				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#register-modal">Register</button>
			</li>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${userSession.roleType == 'ADMIN'}">
					<li class="nav-item">
						<button type="button" class="btn btn-primary" data-toggle="modal"
							onclick="location.href='/admin-book'">
							<fmt:message key="header.home" />
						</button>
					</li>
					<li class="nav-item">
						<div class="dropdown">
							<button class="btn btn-secondary dropdown-toggle" type="button"
								id="dropdownMenuButton" data-toggle="dropdown"
								aria-haspopup="true" aria-expanded="false">Menu</button>
							<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
								<a class="dropdown-item" href="#"
									onclick="location.href='/admin-book'"> Books</a> <a
									class="dropdown-item" href="#"
									onclick="location.href='/admin-librarian'"> Librarians</a> <a
									class="dropdown-item" href="#"
									onclick="location.href='/admin-reader'"> Readers</a>
							</div>
						</div>
				</c:when>
				<c:when test="${userSession.roleType == 'LIBRARIAN'}">
					<li class="nav-item">
						<button type="button" class="btn btn-primary" data-toggle="modal"
							onclick="location.href='/pending-request'">
							<fmt:message key="header.home" />
						</button>
					</li>
					<li class="nav-item">
						<div class="dropdown">
							<button class="btn btn-secondary dropdown-toggle" type="button"
								id="dropdownMenuButton" data-toggle="dropdown"
								aria-haspopup="true" aria-expanded="false">Menu</button>
							<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
								<a class="dropdown-item" href="#"
									onclick="location.href='/pending-request'">Pending</a> <a
									class="dropdown-item" href="#"
									onclick="location.href='/approved-request'">Approved</a>
							</div>
						</div>
					</li>
				</c:when>
				<c:when test="${userSession.roleType == 'READER'}">
					<li class="nav-item">
						<button type="button" class="btn btn-primary" data-toggle="modal"
							onclick="location.href='/reader-book'">
							<fmt:message key="header.home" />
						</button>
					</li>
					<li class="nav-item">
						<div class="dropdown">
							<button class="btn btn-secondary dropdown-toggle" type="button"
								id="dropdownMenuButton" data-toggle="dropdown"
								aria-haspopup="true" aria-expanded="false">Menu</button>
							<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
								<a class="dropdown-item" href="#"
									onclick="location.href='/reader-book'">Books</a> <a
									class="dropdown-item" href="#"
									onclick="location.href='reader-request'">Requests</a>
							</div>
						</div>
					</li>
				</c:when>
			</c:choose>
			<li class="nav-item">
				<button type="button" class="btn btn-primary" data-toggle="modal"
					onclick="location.href='/logout'">Logout</button>
			</li>
			<li class="nav-item">
				<button type="button" class="btn btn-primary" data-toggle="modal">
					Hello
					<c:out value="${userSession.username}" />
				</button>
		</c:otherwise>
	</c:choose>
	<li class="nav-item">
		<ul id='dropdown_lang' class='dropdown-content'>
			<li id="en">En</li>
			<li id="ua">Ua</li>
		</ul>
	</li>
</ul>

<div id="login-modal" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form:form method="POST" action="/login" id="login-form"
					modelAttribute="login">
					<table class="table">
						<tr>
							<td><form:label path="username">Login</form:label></td>
							<td><form:input path="username" required="required" /></td>
						</tr>
						<tr>
							<td><form:label path="password">Password</form:label></td>
							<td><form:password path="password" id="login-password"
									required="required" /></td>
						</tr>
					</table>

					<div class="modal-footer">
						<input class="btn btn-primary" type="submit" value="Submit" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<div id="register-modal" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form:form method="POST" action="/register"
					modelAttribute="register" id="registration-form">
					<table class="table">
						<tr>
							<td><form:label path="username">Login</form:label></td>
							<td><form:input path="username" required="required" /></td>
						</tr>
						<tr>
							<td><form:label path="password">Password</form:label></td>
							<td><form:password path="password" id="password"
									required="required" /></td>
						</tr>
						<tr>
							<td><form:label path="firstName">
                      First Name</form:label></td>
							<td><form:input path="firstName" required="required" /></td>
						</tr>
						<tr>
							<td><form:label path="lastName">
                      Last Name</form:label></td>
							<td><form:input path="lastName" required="required" /></td>
						</tr>
					</table>

					<div class="modal-footer">
						<input class="btn btn-primary" type="submit" value="Submit" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<div id="error-modal" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Error</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<p>${errorDesc.message}</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<c:if test="${not empty errorDesc and not empty errorDesc.message}">
	<script>
		$("#error-modal").modal("show")
	</script>
</c:if>

<script>
	$(document).ready(function() {

		$('#content-table').DataTable({
			"lengthMenu" : [ [ 5, 10, 25, 50, -1 ], [ 5, 10, 25, 50, "All" ] ],
			"sDom" : 'ltrip',
			"bLengthChange" : false,
			"paging" : false,
			"info" : false
		});

		$('.modal').on('hidden.bs.modal', function() {
			$(this).find('form')[0].reset();
		});

		$("#en").click(function() {
			document.cookie = "locale=en";
			location.reload();
		});
		$("#ua").click(function() {
			document.cookie = "locale=ua";
			location.reload();
		});
		$("#registration-form").on("submit", function() {
			var password = $("#password");
			if (password) {
				var checksum = md5(password);
				$("#password").val(checksum);
			}
		});
		$("#login-form").on("submit", function() {
			var password = $("#login-password");
			if (password) {
				var checksum = md5(password);
				$("#login-password").val(checksum);
			}
		});

		formatDates();
	});
</script>