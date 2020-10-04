package com.epam.lab.exam.library.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.PageDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.service.BookRequestService;
import com.epam.lab.exam.library.service.ConfigService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes(types = UserSessionDTO.class)
public class BookRequestController implements GenericController {

	@Autowired
	private BookRequestService bookRequestService;

	@Autowired
	private ConfigService configService;

	@GetMapping("/reader-request")
	public ModelAndView allApprovedReaderRequestGet(Model model, @ModelAttribute("page") PageDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) {
		return allApprovedReaderRequests(model, PageDTO.builder().page(1).build(), userSession);
	}

	@PostMapping("/reader-request")
	public ModelAndView allApprovedReaderRequestPost(Model model, @ModelAttribute("page") PageDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) {
		return allApprovedReaderRequests(model, dto, userSession);
	}

	private ModelAndView allApprovedReaderRequests(Model model, PageDTO dto, UserSessionDTO userSession) {
		int page = dto.getPage() - 1;
		int size = configService.getDefaultPageSize();
		log.info("getting books page={} size={}", page, size);
		List<BookRequestDTO> approved = bookRequestService.getApproved(userSession.getId(), page, size);

		model.addAttribute("login", new LoginDTO());
		model.addAttribute("register", new RegisterDTO());
		model.addAttribute("request", new SubmitRequestDTO());
		model.addAttribute("return-book", new IdentityDTO());
		updateLocation(model, View.READER_REQUEST, userSession);
		long noOfRecords = bookRequestService.countApprovedReaderRequests(userSession.getId());
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		dto.setNoOfPages(noOfPages);
		model.addAttribute("page", dto);
		
		log.info("returning approved requests {}", approved);
		return new ModelAndView(View.READER_REQUEST.page(), "requests", approved);
	}

	@PostMapping("/request-submit")
	public String submitRequest(@Valid @ModelAttribute("request") SubmitRequestDTO request) throws ClientRequestException {
		log.info("submitting book request: {}", request);
		bookRequestService.submit(request);
		return View.READER_BOOK.redirect();
	}

}
