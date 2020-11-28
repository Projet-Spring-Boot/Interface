package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppUser;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

	@Autowired
	private AppUserDAO appUserDAO;

	@RequestMapping(method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());

		model.addAttribute("appUser", logineduser2);
		
		return "userInfoPage";
	}
}
