package com.spring.social.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.AppRole;
import com.spring.social.entity.AppUser;
import com.spring.social.entity.UserConnection;
import com.spring.social.form.AppUserForm;
import com.spring.social.form.MessageForm;
import com.spring.social.security.SecurityAuto;
import com.spring.social.utils.WebUtil;
import com.spring.social.validator.AppUserValidator;

@Controller
@Transactional
public class MainController {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;

	@Autowired
	private AppUserValidator appUserValidator;
	
	@Autowired
	private ConnectionRepository connectionRepo;
	
	@Autowired
	private UserConnectionDAO userConnectionDAO;
	

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		// Form target
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == AppUserForm.class) {
			dataBinder.setValidator(appUserValidator);
		}
		// ...
	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		return "welcomePage";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		// After user login successfully.
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
		

		String userInfo = WebUtil.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());
		
		model.addAttribute("appUser", logineduser2);

		return "userInfoPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		
		
		if (principal != null) {
			model.addAttribute("name", principal.getName());
		}

		return "403Page";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		
		//model.addAttribute("host", "http://localhost:8081/login");
		return "loginPage";
	}

	// User login with social networking,
	// but does not allow the app to view basic information
	// application will redirect to page / signin.
	@RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
	public String signInPage(Model model) {
		return "redirect:/login";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
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

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
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

	@RequestMapping(value = { "/sendMessage" }, method = RequestMethod.GET)
	public String sendMessage(Model model) {
		model.addAttribute("messageForm", new MessageForm());
		return "sendMessage";
	}

	@RequestMapping(value = { "/sendMessage" }, method = RequestMethod.POST)
	public void sendPostMessage(WebRequest request, Model model,
			@ModelAttribute("messageForm") @Validated MessageForm messageForm,
			Principal principal) {


		Connection<Twitter> connection = connectionRepo.findPrimaryConnection(Twitter.class);
		
		
		Twitter twitter = null;
		
		if(connection != null) {
			twitter= (Twitter) connection.getApi();


			//twitter.directMessageOperations().sendDirectMessage("_doolmen", "Hello !");
			
			//send tweet
			
			/*
			List<Tweet> timeline = twitter.timelineOperations().getHomeTimeline();
			timeline.forEach(tweet -> {
				
				if(tweet.getText().contains("confier")) {
					System.out.println("who : " + tweet.getFromUser());
					System.out.println("whoId : " + tweet.getFromUserId());
					System.out.println("text: " + tweet.getText());
					System.out.println("is retweet : " + tweet.isRetweet());
					System.out.println("a été retweeté : " + tweet.getRetweetCount());
					System.out.println("favoris : " + tweet.getFavoriteCount());
					
					
					if(tweet.isRetweet()) {
						Tweet baseTweet = tweet.getRetweetedStatus();
						System.out.println("\t user :" +baseTweet.getFromUser());
						System.out.println("\t text : " + baseTweet.getText());
						
						System.out.println();

					}
					System.out.println("image de profil :" + tweet.getProfileImageUrl());
					System.out.println("images du tweet :");
					tweet.getEntities().getMedia().forEach(media -> {
						System.out.println("\t" +media.getMediaUrl());
					});
					
					System.out.println();
					System.out.println();
				}
			

			});
			
			*/
			twitter.timelineOperations().updateStatus(messageForm.getMessage());
		}
		else {
			//twitter = new TwitterTemplate(userConnectionDAO.findUserConnectionByUserName(principal.getName()).getAccessToken());
			// nop on peux pas lol
		}
	}
}
