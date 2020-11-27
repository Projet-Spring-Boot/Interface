package com.spring.social.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NewpasswordController {
	@RequestMapping(value = { "/newpassword" }, method = RequestMethod.GET)
	public String login(Model model) {
		
		//model.addAttribute("host", "http://localhost:8081/login");
		return "newpassword";
	}
}
