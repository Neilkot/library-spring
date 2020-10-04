package com.epam.lab.exam.library.controller;

import org.springframework.ui.Model;

import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;

public final class ControllerHelper {
	
	public static void addModelsForHeader(Model model) {
		model.addAttribute("login", new LoginDTO());
		model.addAttribute("register", new RegisterDTO());
		model.addAttribute("request", new SubmitRequestDTO());
	}

}
