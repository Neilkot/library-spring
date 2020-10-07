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

import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.PageDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.service.ConfigService;
import com.epam.lab.exam.library.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes(types = UserSessionDTO.class)
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigService configService;

	@GetMapping("/admin-librarian")
	public ModelAndView getLibrariansGet(Model model) {
		return getLibrarians(model, PageDTO.builder().currPage(0).build());
	}

	@PostMapping("/admin-librarian")
	public ModelAndView getLibrariansPost(Model model, @ModelAttribute("page") PageDTO dto) {
		return getLibrarians(model, dto);
	}

	private ModelAndView getLibrarians(Model model, PageDTO dto) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting librarians currPage={} pageSize={}", currPage, pageSize);
		List<UserDTO> librarians = userService.getLibrarians(currPage, pageSize);

		ControllerHelper.addModelsForHeader(model);
		model.addAttribute("delete-librarian", new IdentityDTO());
		model.addAttribute("librarian", new RegisterDTO());

		long noOfRecords = userService.countLibrarians();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		log.info("noOfRecords={} noOfPages={}", noOfRecords, noOfPages);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		dto.setCurrPage(currPage);
		model.addAttribute("page", dto);

		log.info("returning librarians {}", librarians);

		return new ModelAndView(View.ADMIN_LIBRARIAN.page(), "librarians", librarians);
	}

	@PostMapping("/librarian-delete")
	public String deleteLibrarian(@Valid @ModelAttribute("delete-librarian") IdentityDTO dto) {
		log.info("deleting librarian:{}", dto);
		userService.deleteUser(dto.getId());
		return View.ADMIN_LIBRARIAN.redirect();
	}

	@PostMapping("/librarian-create")
	public String createLibrarian(@Valid @ModelAttribute("librarian") RegisterDTO dto) throws ClientRequestException {
		log.info("creating librarian:{}", dto);
		userService.createLibrarian(dto);
		return View.ADMIN_LIBRARIAN.redirect();
	}

	@GetMapping("/admin-reader")
	public ModelAndView getReadersGet(Model model) {
		return getReaders(model, PageDTO.builder().currPage(0).build());
	}

	@PostMapping("/admin-reader")
	public ModelAndView getReadersPost(Model model, @ModelAttribute("page") PageDTO dto) {
		return getReaders(model, dto);
	}

	private ModelAndView getReaders(Model model, PageDTO dto) {
		int currPage = dto.getCurrPage();
		int pageSize = configService.getDefaultPageSize();
		log.info("getting readers currPage={} pageSize={}", currPage, pageSize);

		ControllerHelper.addModelsForHeader(model);
		List<UserDTO> readers = userService.getReaders(currPage, pageSize);
		model.addAttribute("is-blocked", new UserDTO());

		long noOfRecords = userService.countLibrarians();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / configService.getDefaultPageSize());
		log.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);
		dto.setNoOfPages(noOfPages);
		dto.setPageSize(pageSize);
		dto.setCurrPage(currPage);
		model.addAttribute("page", dto);

		log.info("returning librarians {}", readers);

		return new ModelAndView(View.ADMIN_READER.page(), "readers", readers);
	}

	@PostMapping("/reader-status")
	public String changeStatus(@ModelAttribute("isBlocked") UserDTO dto) {
		log.info("changing user {} status for:{}", dto);
		userService.updateIsBlocked(dto.getId(), dto.getIsBlocked());
		return View.ADMIN_READER.redirect();
	}
}
