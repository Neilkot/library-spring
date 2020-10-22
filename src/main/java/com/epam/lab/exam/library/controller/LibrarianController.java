package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.*;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.service.BookRequestService;
import com.epam.lab.exam.library.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LibrarianController implements GenericController {

	@Autowired
	private BookRequestService bookRequestService;

	@Autowired
	private ConfigService configService;

	@GetMapping("/pending-request")
	public ModelAndView allPendingRequestsGet(Model model, @ModelAttribute("userSession") UserSessionDTO userSession) {
		return allPendingRequests(model, PageDTO.builder().currPage(0).build(), userSession);
	}

	@PostMapping("/pending-request")
	public ModelAndView allPendingRequestsPost(Model model, @ModelAttribute("page") PageDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) {
		return allPendingRequests(model, dto, userSession);
	}

	private ModelAndView allPendingRequests(Model model, PageDTO dto, UserSessionDTO userSession) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting approved requests currPage={} pageSize={}", currPage, pageSize);
		List<PendingRequestDTO> requests = bookRequestService.getPending(currPage, pageSize);
		ControllerHelper.addModelsForHeader(model);
		model.addAttribute("request", new IdentityDTO());
		updateLocation(model, View.LIBRARIAN_PENDING, userSession);

		long noOfRecords = bookRequestService.countPending();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		log.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		dto.setCurrPage(currPage);
		model.addAttribute("page", dto);

		log.info("returning pending requests {}", requests);

		return new ModelAndView(View.LIBRARIAN_PENDING.page(), "requests", requests);
	}

	@GetMapping("/approved-request")
	public ModelAndView approvedRequestGet(Model model, @ModelAttribute("userSession") UserSessionDTO userSession) {
		return getApprovedRequests(model, PageDTO.builder().currPage(0).build(), userSession);
	}

	@PostMapping("/approved-request")
	public ModelAndView approvedRequestPost(Model model, @ModelAttribute("page") PageDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) {
		return getApprovedRequests(model, dto, userSession);
	}

	private ModelAndView getApprovedRequests(Model model, PageDTO dto, UserSessionDTO userSession) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting approved requests currPage={} pageSize={}", currPage, pageSize);

		ControllerHelper.addModelsForHeader(model);
		model.addAttribute("return-book", new IdentityDTO());
		List<BookRequestDTO> requests = bookRequestService.getApproved(currPage, pageSize);
		updateLocation(model, View.READER_REQUEST, userSession);
		long noOfRecords = bookRequestService.countApproved();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		log.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		dto.setCurrPage(currPage);
		model.addAttribute("page", dto);

		log.info("returning approved requests {}", requests);

		return new ModelAndView(View.READER_REQUEST.page(), "requests", requests);
	}

	@PostMapping("/request-approve")
	public String approveRequest(@ModelAttribute("request") IdentityDTO dto) throws ClientRequestException {
		log.info("approving request:{}", dto);
		bookRequestService.approve(dto);
		return View.LIBRARIAN_PENDING.redirect();
	}

	@PostMapping("/request-cancel")
	public String cancelRequest(@ModelAttribute("request") IdentityDTO dto) throws ClientRequestException {
		log.info("canceling request:{}", dto);
		bookRequestService.disapprove(dto);
		return View.LIBRARIAN_PENDING.redirect();
	}

	@PostMapping("/return-book")
	public String returnBook(@ModelAttribute("request") IdentityDTO dto) throws ClientRequestException {
		log.info("returning book:{}", dto);
		bookRequestService.returnBook(dto);
		return View.APPROVED_REQUEST.redirect();
	}

}
