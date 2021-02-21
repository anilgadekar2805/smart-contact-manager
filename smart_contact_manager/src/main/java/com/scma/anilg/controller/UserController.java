package com.scma.anilg.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scma.anilg.entities.Contact;
import com.scma.anilg.entities.User;
import com.scma.anilg.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	/*
	@Autowired
	UserRepository userRepository;
	*/
	
	@Autowired
	UserService userService;
	
	/*
	 * Common data retrieved for all handler methods
	 * */
	@ModelAttribute
	public void commonUserData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("Username :"+userName);
		User user = userService.findUserByEmail(userName);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	/* User home handler*/
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal ) {
		model.addAttribute("title", "User Dashboard");
		return "user/user_dashboard";
	}
	
	/* Used to add contact to user dashboard*/
	@GetMapping("/add-contact-form")
	public String addUserContact(Model model) {
		model.addAttribute("title", "Add contact");
		model.addAttribute("contact",new Contact());	
		return "user/add_contact_form";
	}
	
	/**
	 *Add contact-form-data processing to store in
	 *respective users account
	 **/
	@PostMapping("/process-contact")
	public String processAddContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal) {
		String name = principal.getName();
		User userInfo = userService.findUserByEmail(name);	
		/**
		 * Here we added contact to respective user list to get list of contact using method
		 * First we retrieve current user
		 * next add current user into his contact fields
		 * next add this contact info retrieved from form data into users contact List
		 * send this updated contact form data to user list
		 * */
		contact.setUser(userInfo);
		userInfo.getContacts().add(contact);	
		User addContactInUser = userService.addContactInUser(userInfo);
		
		System.out.println("After successful contact added : "+addContactInUser);
		
		return "user/add_contact_form";
	}
	

	
}
