package com.spring.social.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/403")
public class ErrorController {
	@RequestMapping(method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		
		
		if (principal != null) {
			model.addAttribute("name", principal.getName());
		}

		return "403Page";
	}
}
