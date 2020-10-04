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
<title>Reader Requests</title>

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
	<h1>Book Requests List</h1>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td>ID</td>
				<c:if test="${userSession.roleType == 'LIBRARIAN'}">
					<td>User name</td>
				</c:if>
				<td>Name</td>
				<td>Author</td>
				<c:if test="${userSession.roleType == 'READER'}">
					<td>Publisher</td>
					<td>Publish Year</td>
				</c:if>
				<td>Type</td>
				<td>Create Date</td>
				<td>Approve Date</td>
				<td>Expiration Date</td>
				<td>Return Date</td>
				<td>Fee</td>
				<c:if test="${userSession.roleType == 'LIBRARIAN'}">
					<td>Action</td>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="request" items="${requests}">
				<tr>
					<td>${request.requestId}</td>
					<c:if test="${userSession.roleType == 'LIBRARIAN'}">
						<td>${request.username}</td>
					</c:if>
					<td>${request.bookName}</td>
					<td>${request.authorName}</td>
					<c:if test="${userSession.roleType == 'READER'}">
						<td>${request.publisher}</td>
						<td>${request.publishedYear}</td>
					</c:if>
					<td>${request.requestType}</td>
					<td class="table-column-date">${request.createDate}</td>
					<td class="table-column-date">${request.approveDate}</td>
					<td class="table-column-date">${request.expirationDate}</td>
					<td class="table-column-date">${request.returnDate}</td>
					<td>${request.fee}</td>
					<c:if test="${userSession.roleType == 'LIBRARIAN'}">
						<td><c:if
								test="${empty request.returnDate}">
								<a class="dropdown-item" href="#"
									onclick="returnBook(${request.requestId})">Return Book</a>
							</c:if></td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<c:choose>
		<c:when test="${userSession.roleType == 'LIBRARIAN'}">
			<form:form id="request-page" method="POST" action="/approved-request"
				modelAttribute="page">
				<form:hidden id="page" path="page" />
			</form:form>
		</c:when>
		<c:otherwise>
			<form:form id="request-page" method="POST" action="/reader-request"
				modelAttribute="page">
				<form:hidden id="page" path="page" />
			</form:form>
		</c:otherwise>
	</c:choose>


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
	<form:form id="return-book-request-form" method="POST"
		action="/return-book" modelAttribute="return-book">
		<form:hidden id="return-book-request-id" path="id" />
	</form:form>
</body>
<script>
	$(document).ready(function() {
		
	});
	
	function returnBook(requestId) {
		$("#return-book-request-id").val(requestId)
		$("#return-book-request-form").submit()
	}
	
	function requestPage(page) {
		$("#page").val(page);
		$("#request-page").submit()
	}
</script>
</html>
