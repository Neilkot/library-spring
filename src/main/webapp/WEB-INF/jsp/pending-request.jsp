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
<title>Pending requests</title>

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
		<fmt:message key="pending.welcome" />
	</h1>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td><fmt:message key="table.book.id" /></td>
				<td><fmt:message key="table.book.username" /></td>
				<td><fmt:message key="table.book.name" /></td>
				<td><fmt:message key="table.book.author.name" /></td>
				<td><fmt:message key="table.book.type" /></td>
				<td><fmt:message key="table.book.createdate" /></td>
				<td><fmt:message key="table.book.action" /></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="request" items="${requests}">
				<tr>
					<td>${request.requestId}</td>
					<td>${request.username}</td>
					<td>${request.bookName}</td>
					<td>${request.authorName}</td>
					<td>${request.requestType}</td>
					<td class="table-column-date">${request.createDate}</td>
					<td><a class="dropdown-item" href="#"
						onclick="approveRequest(${request.requestId})"><fmt:message
								key="table.book.approve" /></a> <a class="dropdown-item" href="#"
						onclick="cancelRequest(${request.requestId})"><fmt:message
								key="table.book.cancel" /></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<form:form id="request-page" method="POST" action="/pending-request"
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

	<form:form id="approve-request-form" method="POST"
		action="/request-approve" modelAttribute="request">
		<form:hidden id="approve-request-id" path="id" />
	</form:form>

	<form:form id="cancel-request-form" method="POST"
		action="/request-cancel" modelAttribute="request">
		<form:hidden id="cancel-request-id" path="id" />
	</form:form>
</body>
<script>
	$(document).ready(function() {

	});
	
	function approveRequest(requestId) {
		$("#approve-request-id").val(requestId)
		$("#approve-request-form").submit()
	}
	
	function cancelRequest(requestId) {
		$("#cancel-request-id").val(requestId)
		$("#cancel-request-form").submit()
	}
	
	function requestPage(page) {
		$("#page").val(page);
		$("#request-page").submit()
	}
</script>
</html>
