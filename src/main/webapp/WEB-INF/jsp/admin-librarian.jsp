<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
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
	<h1>
		<fmt:message key="admin.librarianssmsg" />
	</h1>

	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#create-modal">
		<fmt:message key="admin.newbutton" />
	</button>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td><fmt:message key="table.book.id" /></td>
				<td><fmt:message key="admin.login" /></td>
				<td><fmt:message key="admin.firstname" /></td>
				<td><fmt:message key="admin.lastname" /></td>
				<td><fmt:message key="table.book.action" /></td>
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
							onclick="fillDeleteForm(${librarian.id})">
							<fmt:message key="admin.deletebutton" />
						</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>


	<form:form id="request-page" method="POST" action="/admin-librarian"
		modelAttribute="page">
		<form:hidden id="page" path="currPage" />
	</form:form>

	<nav aria-label="Page navigation example">
		<ul class="pagination justify-content-right">
			<c:if test="${page.currPage != 0}">
				<li class="page-item"><a class="page-link" href="#"
					onclick="requestPage(${page.currPage - 1});"><fmt:message
							key="pagination.previous" /></a></li>
			</c:if>
			<c:forEach begin="1" end="${page.noOfPages}" var="i">
				<c:choose>
					<c:when test="${i == page.currPage + 1}">
						<li class="page-item active"><a class="page-link">${i}</a></li>
					</c:when>
					<c:otherwise>
						<li class="page-item"><a class="page-link" href="#"
							onclick="requestPage(${i - 1});">${i}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${page.noOfPages > page.currPage + 1}">
				<li class="page-item"><a class="page-link" href="#"
					onclick="requestPage(${page.currPage + 1});"><fmt:message
							key="pagination.next" /></a></li>
			</c:if>
		</ul>
	</nav>

	<div id="delete-modal" class="modal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">
						<fmt:message key="admin.delete.librarial" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="<fmt:message key="header.error.close" />">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>
						<fmt:message key="admin.delete.msg" />
					</p>
				</div>
				<div class="modal-footer">
					<form:form method="POST" action="/librarian-delete"
						modelAttribute="delete-librarian">
						<form:hidden id="delete-librarian-id" path="id" />
						<input class="btn btn-primary" type="submit"
							value="<fmt:message key="admin.yes" />" />
					</form:form>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">
						<fmt:message key="admin.nobutton" />
					</button>
				</div>
			</div>
		</div>
	</div>

	<div id="create-modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<fmt:message key="admin.createlibrarian" />
					</h5>
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
								<td><form:label path="username">
										<fmt:message key="header.login" />
									</form:label></td>
								<td><form:input path="username" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="password">
										<fmt:message key="header.password" />
									</form:label></td>
								<td><form:password id="create-password" path="password"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="firstName">
										<fmt:message key="header.firstname" />
									</form:label></td>
								<td><form:input path="firstName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="lastName">
										<fmt:message key="header.lastname" />
									</form:label></td>
								<td><form:input path="lastName" required="required" /></td>
							</tr>
						</table>

						<div class="modal-footer">
							<input class="btn btn-primary" type="submit"
								value="<fmt:message key="header.submit" />" />
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
