package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@SessionAttributes(types = UserSessionDTO.class)
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public String loginPost(Model model, @ModelAttribute("login") LoginDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) throws ClientRequestException {
		return login(model, dto, userSession);
	}

	@GetMapping("/login")
	public String loginGet(Model model, @ModelAttribute("login") LoginDTO dto,
			@ModelAttribute("userSession") UserSessionDTO userSession) throws ClientRequestException {
		return login(model, dto, userSession);
	}

	private String login(Model model, LoginDTO dto, UserSessionDTO userSession) throws ClientRequestException {
		log.info("user logging in: {}", dto);
		UserDTO user = userService.findByLogin(dto.getUsername());
		log.info("user logged in: {}", user);
		userSession = userSession.enrich(user);
		model.addAttribute("userSession", userSession);
		return View.defaultFor(userSession.getRoleType());
	}

	@GetMapping("/home")
	public String loginGet(@Value("#{session.getAttribute('userSession')}") UserSessionDTO userSession)
			throws ClientRequestException {
		return userSession != null ? View.defaultFor(userSession.getRoleType()) : View.READER_BOOK.redirect();
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, @ModelAttribute("userSession") UserSessionDTO userSession)
			throws ClientRequestException {
		if (userSession != null) {
			SecurityContextHolder.clearContext();
			log.info("user logging out {}", userSession);
			session.invalidate();
		} else {
			log.info("no session attribute");
		}

		return View.READER_BOOK.redirect();
	}

	@PostMapping("/register")
	public String register(Model model, @Valid @ModelAttribute("register") RegisterDTO dto)
			throws ClientRequestException {
		log.info("cereating user:{}", dto);
		UserDTO createReader = userService.createReader(dto);
		log.info("user created:{}", createReader);
		return View.READER_BOOK.redirect();
	}

}
