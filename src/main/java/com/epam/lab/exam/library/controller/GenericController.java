package com.epam.lab.exam.library.controller;

import org.springframework.ui.Model;

import com.epam.lab.exam.library.dto.UserSessionDTO;

public interface GenericController {

	default UserSessionDTO updateLocation(Model model, View view, UserSessionDTO userSession) {
		if (userSession == null) {
			userSession = new UserSessionDTO();
		}
		userSession.setLocation(view.page());
		model.addAttribute("userSession", userSession);
		return userSession;
	}
}
