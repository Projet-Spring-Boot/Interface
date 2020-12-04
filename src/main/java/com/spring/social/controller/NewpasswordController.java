package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppUser;
import com.spring.social.form.NewPasswordForm;

@Controller
public class NewpasswordController {

	@Autowired
	private AppUserDAO appUserDAO;

	@RequestMapping(value = { "/newpassword" }, method = RequestMethod.GET)
	public String getPassword(Model model) {

		return "newpassword";
	}

	@RequestMapping(value = { "/newpassword" }, method = RequestMethod.POST)
	public String newpassword(Model model, Principal principal, @ModelAttribute("personForm") NewPasswordForm newPasswordForm) {

		String oldPassword = newPasswordForm.getOldPassword();
        String newPassword = newPasswordForm.getNewPassword();
        
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());

		model.addAttribute("appUser", logineduser2);
		System.out.println("Old password encrypted : " + logineduser2.getEncrytedPassword());
		System.out.println("Old password verif : " + oldPassword);
		System.out.println("New password : " + newPassword);
		
		//Comparer ancien mot de passe apres encryption
		//Envoyé le nouveau mot de passe vers la BD
		
		return "userInfoPage";
	}
	
}
