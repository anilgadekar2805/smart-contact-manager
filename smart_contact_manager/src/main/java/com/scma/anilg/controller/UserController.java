package com.scma.anilg.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scma.anilg.entities.Contact;
import com.scma.anilg.entities.User;
import com.scma.anilg.helper.Message;
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
	
	User currentLogInUserDetails = null;
	
	/*
	 * Common data retrieved for all handler methods
	 * */
	@ModelAttribute
	public void commonUserData(Model model, Principal principal) {
	
		String userName = principal.getName();
		System.out.println("Logged In Username :"+userName);
		User currentLogInUserDetails = userService.findUserByEmail(userName);
		System.out.println(currentLogInUserDetails);
		model.addAttribute("user", currentLogInUserDetails);
		this.currentLogInUserDetails = currentLogInUserDetails;
	}

	/* User home handler*/
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal ) {
		
		model.addAttribute("title", "User Dashboard");
		return "user/user_dashboard";
	
	}
	
	/* Handler to add contact to user dash-board*/
	@GetMapping("/add-contact-form")
	public String addUserContact(Model model) {
			
		model.addAttribute("title", "Add contact : Smart contact Manager");
		model.addAttribute("contact",new Contact());	
		return "user/add_contact_form";
	
	}
	
	/**
	 *Add contact-form-data processing to store in
	 *respective users account
	 **/
	@PostMapping("/process-contact")
	public String processAddContact(@Valid @ModelAttribute Contact contact, BindingResult result ,@RequestParam("profileImage") MultipartFile mpFile,Principal principal, Model model, HttpSession session) {

		Path destPath = null;
		/**
		 * Here we added contact to respective user list to get list of contact using method
		 * First we retrieve current user
		 * next add current user into his contact fields
		 * next add this contact info retrieved from form data into users contact List
		 * send this updated contact form data to user contact-list
		 * */
		try {
		/**
		 * Setting explicitly retrieving image data using @RequestParam, first save image into /resource/static/image folder then  
		 * save this image unique name into database as a Url string 
		 * */
			if(mpFile.isEmpty()) {
				System.out.println("file is empty");
				throw new Exception("Image file must not selected..!!");
			}else {
			
				 /** making image name unique*/
				String currDateTime= (LocalDateTime.now()+"").replace(":", "-");
				String originalFilename = currDateTime+"@"+mpFile.getOriginalFilename();
				
				/** retrieve current class-path resource folder relative path */
				 File savedFile = new ClassPathResource("/static/image").getFile();
			 
				 destPath = Paths.get(savedFile.getAbsolutePath()+File.separator+originalFilename);
				 System.out.println("Image path :"+destPath);
				 
				contact.setImage(originalFilename);
			}
			/** first complete contact form setting all attributes details */
			contact.setUser(currentLogInUserDetails);
			
			/** Retrieving current users all contact list and again add current retrieved contact info into the list */
			currentLogInUserDetails.getContacts().add(contact);	
			
		/** save this updated or added contacts  user into database  */
		User addedContactResult = userService.addContactInUser(currentLogInUserDetails);
		
		if(addedContactResult !=null) {
			
			/** Now actual storing image into given path, when first Registered this file path location into DB then now*/
			Files.copy(mpFile.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("After successful contact added : "+addedContactResult);
		}
		
		/** success message alert */
		session.setAttribute("message", new Message("Contact saved successfully.....!!", "success"));
		
		return "user/add_contact_form";
		
		}catch(Exception e) {
			
			System.out.println("Error : "+e);
			e.printStackTrace();
			model.addAttribute("contact", new Contact());

			/** failure message alert */
			session.setAttribute("message", new Message("Something goes wrong, please try again.....!!", "danger"));
			return "user/add_contact_form";
		}

	}

}
