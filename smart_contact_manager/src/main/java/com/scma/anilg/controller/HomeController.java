package com.scma.anilg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scma.anilg.entities.User;
import com.scma.anilg.helper.Message;
import com.scma.anilg.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(value={"/", "/home"})
	public String home(Model model) {
		model.addAttribute("title", " Home : smart contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About : Smart contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register : Smart contact manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	/* Handling Register form data */
	@RequestMapping(value="/register/", method = RequestMethod.POST)
	public String register(@ModelAttribute("user1") User user, @RequestParam(value="agreement", defaultValue = "false" ) boolean agreement, Model model, HttpSession session) {
		
		try {	
			if(!agreement) {
				System.out.println("you have not agreed terms and Condition, .");
				throw new Exception("you have not agreed Accept terms and Condition.");
			}
			System.out.println("agreement : "+agreement);
			System.out.println(user);
			
			User result = userService.userRegister(user);
			model.addAttribute("user", result);
			session.setAttribute("message", new Message("registered successfully", "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something goes wrong .!"+e.getMessage(), "alert-danger") );
			return "signup";
		}
		return "signup";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "Login : Smart contact Manager");
		return "login";
	}
	
}
