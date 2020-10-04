package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.model.RoleType;

public enum View {

	READER_BOOK("reader-book"), READER_REQUEST("reader-request"), ADMIN_BOOK("admin-book"),
	ADMIN_LIBRARIAN("admin-librarian"), ADMIN_READER("admin-reader"), LIBRARIAN_PENDING("pending-request"),
	APPROVED_REQUEST("approved-request"), LOGIN("login"), ERROR("error");

	String page;

	View(String page) {
		this.page = page;
	}

	public String page() {
		return page;
	}

	public String redirect() {
		return "redirect:/" + page;
	}

	public static String defaultFor(RoleType roleType) {
		if(roleType == null) {
			return READER_BOOK.redirect();
		}
		switch (roleType) {
		case ADMIN:
			return ADMIN_BOOK.redirect();
		case LIBRARIAN:
			return LIBRARIAN_PENDING.redirect();
		case READER:
			return READER_BOOK.redirect();
		default:
			throw new IllegalArgumentException("not supported");
		}
	}
}
