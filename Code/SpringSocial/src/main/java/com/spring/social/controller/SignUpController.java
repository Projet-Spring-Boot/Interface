package com.spring.social.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppRole;
import com.spring.social.entity.AppUser;
import com.spring.social.form.AppUserForm;
import com.spring.social.security.SecurityAuto;

@Controller
@RequestMapping(value = { "/signup" })
public class SignUpController {
	
	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public String signupPage(WebRequest request, Model model) {

		ProviderSignInUtils providerSignInUtils //
				= new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);

		// Retrieve social networking information.
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		// Twitter twitter= connection.getApi() lorsqu'on voudra envoyer des tweet
		// partie 2

		//
		AppUserForm myForm = null;
		//
		if (connection != null) {
			myForm = new AppUserForm(connection);

			System.out.println("provider = " + myForm.getSignInProvider());
		} else {
			myForm = new AppUserForm();
		}

		model.addAttribute("myForm", myForm);
		return "signupPage";
	}

	
	@RequestMapping(method = RequestMethod.POST)
	public String signupSave(WebRequest request, Model model,
			@ModelAttribute("myForm") @Validated AppUserForm appUserForm, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		// Validation error.
		if (result.hasErrors()) {
			return "signupPage";
		}

		List<String> roleNames = new ArrayList<String>();
		roleNames.add(AppRole.ROLE_USER);

		AppUser registered = null;

		try {
			registered = appUserDAO.registerNewUserAccount(appUserForm, roleNames);

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errorMessage", "Error " + ex.getMessage());
			return "signupPage";
		}

		// Lorsque la requête POST de login contient un signInProvider (on se connecte
		// via Goodle ou autre),
		// on créé un enregistrement dans la table UserConnection
		if (appUserForm.getSignInProvider() != null) {

			ProviderSignInUtils providerSignInUtils //
					= new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);

			// (Spring Social API):
			// If user login by social networking.
			// This method saves social networking information to the UserConnection table.

			providerSignInUtils.doPostSignUp(registered.getUserName(), request);

		}

		// After registration is complete, automatic login.
		SecurityAuto.logInUser(registered, roleNames);

		return "redirect:/userInfo";
	}
}
