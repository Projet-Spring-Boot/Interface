package com.spring.social.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppUser;
import com.spring.social.security.SecurityAuto;
//import com.spring.social.dao.InfoConnectionDAO;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
	
	//private long connectionId = 0;
	@Autowired
	private AppUserDAO appUserDAO;
	//@Autowired
	//private InfoConnectionDAO infoConnectionDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());

		model.addAttribute("appUser", logineduser2);
		//List<Long> list = infoConnectionDAO.getConnectionIdByUserId(logineduser2.getUserId());
		//Long connectionId = Collections.max(list,null);
		
		//Long userId = logineduser2.getUserId()
		//Long dureeSession = infoConnectionDAO.getElapsedTime(connectionId);
		//Long nbConnexion = infoConnectionDAO.getNbConnection(userId)
		Long dureeSession = (long) 5;
		Long nbConnexion = (long) 2;
		model.addAttribute("dureeSession", dureeSession);
		model.addAttribute("nbConnexion", nbConnexion);

		return "userInfoPage";
	}
}
