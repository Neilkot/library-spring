package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.*;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.service.BookService;
import com.epam.lab.exam.library.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@SessionAttributes(types = UserSessionDTO.class)
public class BookController implements GenericController {

	@Autowired
	private BookService bookService;

	@Autowired
	private ConfigService configService;

	@GetMapping({ "/reader-book", "/" })
	public ModelAndView getBooksGet(Model model,
			@Value("#{session.getAttribute('userSession')}") UserSessionDTO userSession) {
		return getBooks(model, PageDTO.builder().currPage(0).build(), userSession);
	}

	@PostMapping({ "/reader-book", "/" })
	public ModelAndView getBooksPost(Model model, @ModelAttribute("page") PageDTO dto,
			@Value("#{session.getAttribute('userSession')}") UserSessionDTO userSession) {
		return getBooks(model, dto, userSession);
	}
	
	private ModelAndView getBooks(Model model, PageDTO dto, UserSessionDTO userSession) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting books currPage={} pageSize={}", currPage, pageSize);
		List<BookDTO> books = bookService.getAvailableBooks(currPage, pageSize);

		model.addAttribute("query", new QueryDTO());
		model.addAttribute("login", new LoginDTO());
		model.addAttribute("register", new RegisterDTO());
		model.addAttribute("request", new SubmitRequestDTO());
		updateLocation(model, View.READER_BOOK, userSession);

		long noOfRecords = bookService.countAvailableBooks();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
		log.info("noOfRecords={} noOfPages={}", noOfRecords, noOfPages);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		model.addAttribute("page", dto);

		log.info("returning books {}", books);

		return new ModelAndView(View.READER_BOOK.page(), "books", books);
	}

	@PostMapping("/book-query")
	public ModelAndView getBooksQuery(Model model, @ModelAttribute("query") QueryDTO dto) {
		log.info("getting books query page");
		List<BookDTO> books = dto != null && StringUtils.isNotEmpty(dto.getQuery())
				? bookService.getAvailableBooks(dto, 0, configService.getDefaultPageSize())
				: bookService.getAvailableBooks(0, configService.getDefaultPageSize());
		log.info("{} books returned", books.size());
		model.addAttribute("page", PageDTO.builder().currPage(0).noOfPages(1).build());
		model.addAttribute("query", new QueryDTO());
		model.addAttribute("login", new LoginDTO());
		model.addAttribute("register", new RegisterDTO());
		model.addAttribute("request", new SubmitRequestDTO());
		return new ModelAndView(View.READER_BOOK.page(), "books", books);
	}

	@GetMapping("/admin-book")
	public ModelAndView getAdminBooksGet(Model model, @ModelAttribute("userSession") UserSessionDTO userSession) {
		return getAdminBooks(model, PageDTO.builder().build(), userSession);
	}

	@PostMapping("/admin-book")
	public ModelAndView getAdminBooksPost(Model model, @ModelAttribute("page") PageDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) {
		return getAdminBooks(model, dto, userSession);
	}

	private ModelAndView getAdminBooks(Model model, PageDTO dto, UserSessionDTO userSession) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting books currPage={} pageSize={}", currPage, pageSize);
		log.info("getting books pageSize{}",  pageSize);
		List<BookItemDTO> books = bookService.getBooks(currPage, pageSize);
		model.addAttribute("book", new BookItemDTO());
		model.addAttribute("book-delete", new IdentityDTO());
		model.addAttribute("book-delete", new IdentityDTO());
		model.addAttribute("login", new LoginDTO());
		model.addAttribute("register", new RegisterDTO());
		updateLocation(model, View.ADMIN_BOOK, userSession);

		long noOfRecords = bookService.countBooks();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		log.info("noOfRecords={} noOfPages={}", noOfRecords, noOfPages);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		dto.setCurrPage(currPage);
		model.addAttribute("page", dto);

		log.info("returning admin books {}", books);

		return new ModelAndView(View.ADMIN_BOOK.page(), "books", books);
	}

	@PostMapping("/book-add")
	public String addBook(@ModelAttribute("book") BookItemDTO dto) {
		log.info("adding book:{}", dto);
		bookService.createBook(dto);
		return View.ADMIN_BOOK.redirect();
	}

	@PostMapping("/book-update")
	public String updateBook(@ModelAttribute("book") BookItemDTO dto) throws ClientRequestException {
		log.info("updating book:{}", dto);
		bookService.updateBook(dto);
		return View.ADMIN_BOOK.redirect();
	}

	@PostMapping("/book-delete")
	public String deleteBook(@ModelAttribute("book-delete") IdentityDTO dto) throws ClientRequestException {
		log.info("deleting book:{}", dto);
		bookService.deleteBook(dto);
		return View.ADMIN_BOOK.redirect();
	}
}
