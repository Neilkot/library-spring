package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.ErrorDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.service.MessageService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
@SessionAttributes(types = UserSessionDTO.class)
public class ErrorHandler {

	@Autowired
	private MessageService messageService;

	@Getter
	private AuthenticationEntryPoint forbiddenHandler = new AuthenticationEntryPoint() {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			UserSessionDTO userSession = (UserSessionDTO) request.getSession().getAttribute("userSession");
			request.setAttribute("errorDesc", ErrorDTO.builder().message(ErrorType.FORBIDDEN.getMessageCode())
					.code(ErrorType.FORBIDDEN.getErrorCode()).build());
			String location = userSession != null ? userSession.getLocation() : View.READER_BOOK.page();
			log.info("redirecting to current location {}", location);
			request.getRequestDispatcher(location).forward(request, response);
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
		ra.addFlashAttribute("errorDesc", ErrorDTO.builder().message(messageService.getMessage(e.getMessageCode()))
				.code(e.getErrorCode()).build());
		UserSessionDTO userSession = (UserSessionDTO) request.getSession().getAttribute("userSession");
		String location = userSession != null ? userSession.getLocation() : View.READER_BOOK.page();
		log.info("redirecting to current location {}", location);
		return "redirect:/" + location;
	}

	@ExceptionHandler(BindException.class)
	public String handleError(RedirectAttributes ra, HttpServletRequest request, BindException e) {
		log.info(e.getMessage(), e);
		ra.addFlashAttribute("errorDesc",
				ErrorDTO.builder().message(messageService.getMessage(ErrorType.BAD_REQUEST.getMessageCode()))
						.code(ErrorType.BAD_REQUEST.getErrorCode()).build());
		UserSessionDTO userSession = (UserSessionDTO) request.getSession().getAttribute("userSession");
		String location = userSession != null ? userSession.getLocation() : View.READER_BOOK.page();
		log.info("redirecting to current location {}", location);
		return "redirect:/" + location;
	}

	@ExceptionHandler(Exception.class)
	public String handleError(RedirectAttributes ra, Exception e) {
		log.info(e.getMessage(), e);
		ra.addFlashAttribute("errorDesc", ErrorDTO.builder().message(ErrorType.INTERNAL_SERVER_ERROR.getMessageCode())
				.code(ErrorType.INTERNAL_SERVER_ERROR.getErrorCode()).build());
		return View.ERROR.redirect();
	}
}
