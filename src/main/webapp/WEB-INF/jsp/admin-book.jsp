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
<title>Material Design Bootstrap</title>

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
		<fmt:message key="admin.bookmsg" />
	</h1>

	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#create-modal">
		<fmt:message key="admin.newbutton" />
	</button>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td><fmt:message key="table.book.item.id" /></td>
				<td><fmt:message key="table.book.id" /></td>
				<td><fmt:message key="table.book.name" /></td>
				<td><fmt:message key="table.book.author.name" /></td>
				<td><fmt:message key="table.book.publisher" /></td>
				<td><fmt:message key="table.book.published.year" /></td>
				<td><fmt:message key="table.book.image" /></td>
				<td><fmt:message key="table.book.action" /></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="book" items="${books}">
				<tr>
					<td>${book.bookItemId}</td>
					<td>${book.bookId}</td>
					<td>${book.bookName}</td>
					<td>${book.authorName}</td>
					<td>${book.publisher}</td>
					<td>${book.publishedYear}</td>
					<td><img src="${book.imgLink}" width="150" height="150" /></td>
					<td><button type="button" class="btn btn-primary"
							data-toggle="modal" data-target="#update-modal"
							onclick="fillUpdateForm(${book.bookItemId}, ${book.bookId}, '${book.bookName}', '${book.authorName}', '${book.publisher}', ${book.publishedYear}, '${book.imgLink}')">
							<fmt:message key="admin.update" />
						</button>
						<button type="button" class="btn btn-primary" data-toggle="modal"
							data-target="#delete-modal"
							onclick="fillDeleteForm(${book.bookItemId})">
							<fmt:message key="admin.deletebutton" />
						</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<form:form id="request-page" method="POST" action="/admin-book"
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

	<div id="update-modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<fmt:message key="admin.updatebook" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form:form method="POST" action="/book-update"
						modelAttribute="book">
						<form:hidden id="update-book-item-id" path="bookItemId" />
						<form:hidden id="update-book-id" path="bookId" />
						<table class="table">
							<tr>
								<td><form:label path="bookName">
										<fmt:message key="table.book.name" />
									</form:label></td>
								<td><form:input id="update-book-name" path="bookName"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="authorName">
										<fmt:message key="table.book.author.name" />
									</form:label></td>
								<td><form:input id="update-author-name" path="authorName"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publisher">
										<fmt:message key="table.book.publisher" />
									</form:label></td>
								<td><form:input id="update-publisher" path="publisher"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publishedYear">
										<fmt:message key="table.book.published.year" />
									</form:label></td>
								<td><form:input type="number" id="update-publish-year"
										path="publishedYear" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="imgLink">
										<fmt:message key="table.book.image" />
									</form:label></td>
								<td><form:input id="update-image-link" path="imgLink"
										required="required" /></td>
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

	<div id="delete-modal" class="modal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">
						<fmt:message key="admin.deletebook" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>
						<fmt:message key="admin.deletebookmsg" />
					</p>
				</div>
				<div class="modal-footer">
					<form:form method="POST" action="/book-delete"
						modelAttribute="book-delete">
						<form:hidden id="delete-book-item-id" path="id" />
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
						<fmt:message key="admin.createbook" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form:form method="POST" action="/book-add" modelAttribute="book">
						<table class="table">
							<tr>
								<td><form:label path="bookName">
										<fmt:message key="table.book.name" />
									</form:label></td>
								<td><form:input path="bookName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="authorName">
										<fmt:message key="table.book.author.name" />
									</form:label></td>
								<td><form:input path="authorName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publisher">
										<fmt:message key="table.book.publisher" />
									</form:label></td>
								<td><form:input path="publisher" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publishedYear">
										<fmt:message key="table.book.published.year" />
									</form:label></td>
								<td><form:input type="number" path="publishedYear"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="imgLink">
										<fmt:message key="table.book.image" />
									</form:label></td>
								<td><form:input path="imgLink" required="required" /></td>
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

	function fillUpdateForm(bookItemId, bookId, bookName, authorName, publisher, publishedYear, imageLink) {
		$("#update-book-item-id").val(bookItemId)
		$("#update-book-id").val(bookId)
		$("#update-book-name").val(bookName)
		$("#update-author-name").val(authorName)
		$("#update-publisher").val(publisher)
		$("#update-publish-year").val(publishedYear)
		$("#update-image-link").val(imageLink)
	}
	
	function fillDeleteForm(bookItemId) {
		$("#delete-book-item-id").val(bookItemId)
	}
	
	function requestPage(page) {
		$("#page").val(page);
		$("#request-page").submit()
	}
</script>
</html>
