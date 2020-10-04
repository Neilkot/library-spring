<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>Librarians</title>

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.11.2/css/all.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/mdb.min.css">
<!-- Plugin file -->
<link rel="stylesheet" href="./css/addons/datatables.min.css">
<link rel="stylesheet" href="css/style.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/popper.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/mdb.min.js"></script>
<!-- Plugin file -->
<script src="./js/addons/datatables.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<h1>Librarians List</h1>

	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#create-modal">New</button>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td>ID</td>
				<td>Login</td>
				<td>First Name</td>
				<td>Last Name</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="librarian" items="${librarians}">
				<tr>
					<td>${librarian.id}</td>
					<td>${librarian.login}</td>
					<td>${librarian.firstName}</td>
					<td>${librarian.lastName}</td>
					<td>
						<button type="button" class="btn btn-primary" data-toggle="modal"
							data-target="#delete-modal"
							onclick="fillDeleteForm(${librarian.id})">Delete</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>


	<form:form id="request-page" method="POST" action="/admin-librarian"
		modelAttribute="page">
		<form:hidden id="page" path="page" />
	</form:form>

	<nav aria-label="Page navigation example">
	<ul class="pagination justify-content-right">
		<c:if test="${page.page != 1}">
			<li class="page-item"><a class="page-link" href="#"
				onclick="requestPage(${page.page - 1});">Previous</a></li>
		</c:if>
		<c:forEach begin="1" end="${page.noOfPages}" var="i">
			<c:choose>
				<c:when test="${page.page eq i}">
					<li class="page-item active"><a class="page-link">${i}</a></li>
				</c:when>
				<c:otherwise>
					<li class="page-item"><a class="page-link" href="#"
						onclick="requestPage(${i});">${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${page.page lt page.noOfPages}">
			<li class="page-item"><a class="page-link" href="#"
				onclick="requestPage(${page.page + 1});">Next</a></li>
		</c:if>
	</ul>
	</nav>

	<div id="delete-modal" class="modal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Delete Librarian</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>Are you sure you wish to delete librarian?</p>
				</div>
				<div class="modal-footer">
					<form:form method="POST" action="/librarian-delete"
						modelAttribute="delete-librarian">
						<form:hidden id="delete-librarian-id" path="id" />
						<input class="btn btn-primary" type="submit" value="Yes" />
					</form:form>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">No</button>
				</div>
			</div>
		</div>
	</div>

	<div id="create-modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">New Librarian</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form:form id="create-form" method="POST"
						action="/librarian-create" modelAttribute="librarian">
						<table class="table">
							<tr>
								<td><form:label path="username"> Login</form:label></td>
								<td><form:input path="username" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="password"> Password</form:label></td>
								<td><form:password id="create-password" path="password"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="firstName"> First Name</form:label></td>
								<td><form:input path="firstName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="lastName"> Last Name</form:label></td>
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
</body>
<script>
	$(document).ready(function() {
		$("#create-form").on("submit", function() {
			var password = $("#create-password");
			if(password) {
				var checksum = md5(password);
				$("#create-password").val(checksum);
			}
	    });
	});

	function fillDeleteForm(librarianId) {
		$("#delete-librarian-id").val(librarianId)
	}
	
	function requestPage(page) {
		$("#page").val(page);
		$("#request-page").submit()
	}
</script>
</html>
