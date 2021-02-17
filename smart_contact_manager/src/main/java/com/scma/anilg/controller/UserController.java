package com.scma.anilg.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scma.anilg.dao.UserRepository;
import com.scma.anilg.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal ) {
		String userName = principal.getName();
		System.out.println("Username :"+userName);
		
		// retrieve user information using username
		User user = userRepository.loadUserByUsername(userName);
		System.out.println(user);
		
		model.addAttribute("user", user);
		
		return "user/user_dashboard";
	}
	
	
}
