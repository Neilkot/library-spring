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
	<h1>Books List</h1>

	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#create-modal">New</button>

	<table id="content-table" class="table" cellspacing="0" width="100%">
		<thead>
			<tr>
				<td>Item ID</td>
				<td>Book ID</td>
				<td>Name</td>
				<td>Author</td>
				<td>Publisher</td>
				<td>Publish Year</td>
				<td>Image</td>
				<td>Action</td>
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
							onclick="fillUpdateForm(${book.bookItemId}, ${book.bookId}, '${book.bookName}', '${book.authorName}', '${book.publisher}', ${book.publishedYear}, '${book.imgLink}')">Update</button>
						<button type="button" class="btn btn-primary" data-toggle="modal"
							data-target="#delete-modal"
							onclick="fillDeleteForm(${book.bookItemId})">Delete</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<form:form id="request-page" method="POST" action="/admin-book"
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

	<div id="update-modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Update Book</h5>
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
								<td><form:label path="bookName"> Book Name</form:label></td>
								<td><form:input id="update-book-name" path="bookName"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="authorName"> Author</form:label></td>
								<td><form:input id="update-author-name" path="authorName"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publisher"> Publisher</form:label></td>
								<td><form:input id="update-publisher" path="publisher"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publishedYear"> Publish Year</form:label></td>
								<td><form:input type="number" id="update-publish-year"
										path="publishedYear" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="imgLink"> Image</form:label></td>
								<td><form:input id="update-image-link" path="imgLink"
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

	<div id="delete-modal" class="modal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Delete Book</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>Are you sure you wish to delete book?</p>
				</div>
				<div class="modal-footer">
					<form:form method="POST" action="/book-delete"
						modelAttribute="book-delete">
						<form:hidden id="delete-book-item-id" path="id" />
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
					<h5 class="modal-title" id="exampleModalLabel">Create Book</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form:form method="POST" action="/book-add" modelAttribute="book">
						<table class="table">
							<tr>
								<td><form:label path="bookName"> Book Name</form:label></td>
								<td><form:input path="bookName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="authorName"> Author</form:label></td>
								<td><form:input path="authorName" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publisher"> Publisher</form:label></td>
								<td><form:input path="publisher" required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="publishedYear"> Publish Year</form:label></td>
								<td><form:input type="number" path="publishedYear"
										required="required" /></td>
							</tr>
							<tr>
								<td><form:label path="imgLink"> Image</form:label></td>
								<td><form:input path="imgLink" required="required" /></td>
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
