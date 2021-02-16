package com.scma.anilg.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scma.anilg.dao.UserRepository;
import com.scma.anilg.entities.User;
import com.scma.anilg.helper.Message;

@Controller
public class HomeController {
	
//	@Autowired
//    UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
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
	public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value="agreement", defaultValue = "false" ) boolean agreement, Model model, HttpSession session) {
		
		try {	
			if(bindingResult.hasErrors()) {
				System.out.println("Error : "+bindingResult);
				return "signup";
			}
			
			if(!agreement) {
				System.out.println("you have not agreed terms and Condition, .");
				throw new Exception("you have not accept terms and Condition.");
			}
			System.out.println("agreement : "+agreement);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setImageUrl("profile.jpg");
			user.setEnabled(true);
			user.setRole("ROLE_USER");

			System.out.println("Before Register User : "+user);
			//User result = userService.userRegister(user);
			User saveResult = userRepository.save(user);
			System.out.println("After Registered User : "+saveResult);
		
			// after successfully registered,  form data must return empty  
			User emptyUser = new User();
			model.addAttribute("user", emptyUser);
			session.setAttribute("message", new Message("Registration successfully", "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("ohhh..!"+e.getMessage(), "alert-danger") );
			return "signup";
		}
		return "signup";
	}
	
	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "Login : Smart contact Manager");
		return "login";
	}
	

}
