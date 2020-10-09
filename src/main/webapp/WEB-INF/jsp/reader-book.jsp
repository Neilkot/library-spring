<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>Reader Book</title>

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.11.2/css/all.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/mdb.min.css">
<!-- Plugin file -->
<link rel="stylesheet" href="./css/addons/datatables.min.css">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/styles.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/popper.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/mdb.min.js"></script>
<!-- Plugin file -->
<script src="./js/addons/datatables.min.js"></script>
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
<body>
	<jsp:include page="header.jsp" />
	<h1>
		<fmt:message key="reader.book.heading" />
	</h1>

	<form:form id="submit-request-form" method="POST"
		action="/request-submit" modelAttribute="request">
		<form:hidden id="user-id" path="userId" />
		<form:hidden id="book-id" path="bookId" />
		<form:hidden id="request-type" path="requestType" />
	</form:form>

	<form:form method="POST" action="/book-query" modelAttribute="query">
		<form:label path="query">
			<fmt:message key="reader.book.search" />
		</form:label>
		<form:input class="form-control mb-4" path="query" />
	</form:form>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td><fmt:message key="table.book.id" /></td>
				<td><fmt:message key="table.book.name" /></td>
				<td><fmt:message key="table.book.author.name" /></td>
				<td><fmt:message key="table.book.publisher" /></td>
				<td><fmt:message key="table.book.published.year" /></td>
				<td><fmt:message key="table.book.image" /></td>
				<c:if
					test="${userSession.roleType == 'READER' and not userSession.isBlocked }">
					<td><fmt:message key="table.book.type" /></td>
					<td><fmt:message key="table.book.action" /></td>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="book" items="${books}">
				<tr>
					<td>${book.bookId}</td>
					<td>${book.bookName}</td>
					<td>${book.authorName}</td>
					<td>${book.publisher}</td>
					<td>${book.publishedYear}</td>
					<td><img src="${book.imgLink}" width="150" height="150" /></td>
					<c:if
						test="${userSession.roleType == 'READER' and not userSession.isBlocked }">
						<td><select id="request-type-${book.bookId}">
								<option value=""></option>
								<option value="ABONEMENT"><fmt:message
										key="table.book.abonement" /></option>
								<option value="READING_AREA"><fmt:message
										key="table.book.readingarea" /></option>
						</select></td>
						<td><input class="btn btn-primary" type="submit"
							value="<fmt:message key="table.book.button.add" />"
							onclick="submitRequest(${userSession.id}, ${book.bookId})" /></td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<form:form id="request-page" method="POST" action="/reader-book"
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
</body>
<script>
	
	function submitRequest(userId, bookId) {
		$("#user-id").val(userId)
		$("#book-id").val(bookId)
		$("#request-type").val($("#request-type-" + bookId).val())
		$("#submit-request-form").submit()
	}
	
	function requestPage(page) {
		$("#page").val(page);
		$("#request-page").submit()
	}
	
</script>
</html>
