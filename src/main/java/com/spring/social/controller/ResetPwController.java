package com.spring.social.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.form.ResetPwForm;

@Controller
@RequestMapping(value = {"/resetPw" })
public class ResetPwController {
	@RequestMapping(method = RequestMethod.GET)
	public String resetPw(Model model) {
		ResetPwForm resetPwForm = new ResetPwForm();
        model.addAttribute("resetPwForm", resetPwForm);
        // System.out.println("ResetPwController");
        
		return "resetPwPage";
	}
	
	// On click methode.
    @RequestMapping(method = RequestMethod.POST)
    public String sendResetEmail(Model model, //
            @ModelAttribute("resetPwForm") ResetPwForm form) {
 
        String email = form.getEmail();
    	
        // System.out.println("sendResetEmail");
        System.out.println(email);
 
        // model.addAttribute("errorMessage", errorMessage);
        return "redirect:/login";
        // return "resetPwPage";
    }
}
