package com.epam.lab.exam.library.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.epam.lab.exam.library.dto.ErrorDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@SessionAttributes(types = UserSessionDTO.class)
public class ErrorHandler {

	@Getter
	private AuthenticationEntryPoint forbiddenHandler = new AuthenticationEntryPoint() {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			request.setAttribute("errorDesc", ErrorDTO.builder().message("Forbidden").code(403).build());
			request.getRequestDispatcher(View.ERROR.redirect()).forward(request, response);
		}
	};

	@GetMapping("/error")
	public String error(Model model, @ModelAttribute("errorDesc") ErrorDTO dto) {
		model.addAttribute("errorDesc", dto);
		return View.ERROR.page();
	}

	@ExceptionHandler(ClientRequestException.class)
	public String handleError(RedirectAttributes ra, HttpServletRequest request, ClientRequestException e) {
		log.info(e.getMessage(), e);
		ra.addFlashAttribute("errorDesc",
				ErrorDTO.builder().message(e.getMessageCode()).code(e.getErrorCode()).build());
		UserSessionDTO userSession = (UserSessionDTO) request.getSession().getAttribute("userSession");
		log.info("redirecting to current location {}", userSession.getLocation());
		return userSession.getLocation();
	}

	@ExceptionHandler(BindException.class)
	public String handleError(RedirectAttributes ra, HttpServletRequest request, BindException e) {
		log.info(e.getMessage(), e);
		ra.addFlashAttribute("errorDesc", ErrorDTO.builder().message("Bad Reques").code(400).build());
		UserSessionDTO userSession = (UserSessionDTO) request.getSession().getAttribute("userSession");
		log.info("redirecting to current location {}", userSession.getLocation());
		return userSession.getLocation();
	}

	@ExceptionHandler(Exception.class)
	public String handleError(RedirectAttributes ra, Exception e) {
		log.info(e.getMessage(), e);
		ra.addFlashAttribute("errorDesc", ErrorDTO.builder().message("Ooops, omething went wrong").code(500).build());
		return View.ERROR.redirect();
	}
}
