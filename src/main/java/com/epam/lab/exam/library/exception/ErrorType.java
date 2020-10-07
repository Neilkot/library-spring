package com.epam.lab.exam.library.exception;

public enum ErrorType {

	USER_NOT_FOUND("user.not.found", 404), BOOK_REQUEST_NOT_FOUND("book.request.not.found", 404),
	LOGIN_IN_USE("login.in.use", 422), BOOK_NOT_FOUND("book.not.found", 404), BAD_REQUEST("bad.request", 400),
	ROLE_NOT_FOUND("role.not.found", 422), INTERNAL_SERVER_ERROR("internalserver.error", 500),
	ALREADY_LOGGED_IN("already.logged.in", 422), UNAUTHORIZED("unauthorized", 401), FORBIDDEN("forbidden", 403),
	METHOD_NOT_ALLOWED("method.not.allowed", 405), BOOK_NOT_AVALIABLE("book.not.avaliable", 422),
	REQUEST_NOT_FOUND("request.not.found", 404);

	String messageCode;
	int errorCode;

	ErrorType(String messageCode, int errorCode) {
		this.messageCode = messageCode;
		this.errorCode = errorCode;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public ClientRequestException ex() {
		return new ClientRequestException(this);
	}
}
