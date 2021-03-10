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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		String originalFilename = null;
		
		 /** making image name unique*/
		String currDateTime= (LocalDateTime.now()+"").replace(":", "-");
		
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
			//	throw new Exception("Image file must not selected..!!");
				originalFilename = "contact_profile.png";
			}else {
				originalFilename = currDateTime+"@"+mpFile.getOriginalFilename();
			}	
				/** retrieve current class-path resource folder relative path */
				 File savedFile = new ClassPathResource("/static/image").getFile();
			 
				 destPath = Paths.get(savedFile.getAbsolutePath()+File.separator+originalFilename);
				 System.out.println("Image path :"+destPath);
				 
				contact.setImage(originalFilename);
			
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
		model.addAttribute("contact", new Contact());
		return "user/add_contact_form";
		
		}catch(Exception e) {
			
			System.out.println("Error : "+e);
			e.printStackTrace();
			model.addAttribute("contact", contact);

			/** failure message alert */
			session.setAttribute("message", new Message("Something goes wrong, please try again.....!!", "danger"));
			return "user/add_contact_form";
		}

	}
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,  Model model, Principal principal) {
		
		/** first get current log in user details*/
		String currentUser = principal.getName();
		User currentUserDetails = this.userService.findUserByEmail(currentUser);
		
		/** First we will get Pageable objects to store current page index and number of records to show end user 
		 * 	
		 * - Current page index is - 0, --> page
		 * - Number of contacts per page = 6 ---> 
		 * */
		Pageable pageable = PageRequest.of(page, 5);	
		
		/** getting list of contacts from user */
		Page<Contact> contacts = this.userService.getContactsList(currentUserDetails.getId(), pageable);	
		System.out.println(contacts.getTotalPages());
		
		model.addAttribute("titile", "Show contacts - Smart contact Manager");
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		
		
		return "user/show_contacts";
		
	}
	
	/** Show respective contact details */
	@SuppressWarnings("unlikely-arg-type")
	@GetMapping("/{cId}/contact")
	public String showContact(@PathVariable("cId") int cId, Principal principal, Model model ) {
		System.out.println("CID : "+cId);
		
		String currentUser = principal.getName();
		model.addAttribute("title", "Contact details : Smart contact Manager");
		
		/** Retrieving user contact details */
		Contact contactDetail = this.userService.getContactDetail(cId);
		
		/** checking if url miss-leading happen then check both current login-user and contact user name is same */
		if(! currentUser.equals(contactDetail.getUser().getEmail())) 
			model.addAttribute("message", new Message("You are not an authorized user for this contact", "denger"));
		else
			model.addAttribute("contact", contactDetail);
		
		return "user/show_user_contact_details";
	}
	
	/** deleting contact from given user lists */
	@GetMapping("/delete-contact/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Principal principal, Model model ) {
		
		/** first take current login user */
		String name = principal.getName();
		User currentUser = this.userService.findUserByEmail(name);
		
		/** take contact details by using its ID */
		Contact resultContact = this.userService.getContactById(cId);
		
		
		/** compare both userID for avoiding url miss-leading purpose  */
		if(currentUser.getId() == resultContact.getUser().getId()) {
			
			/**Remove contact from given user List*/
			this.userService.deleteContact(currentUser, resultContact);
			
			/**delete image from our folder also*/
			// CODE HERE
			
		}else {
			model.addAttribute("message", new Message("You are not an authorized user for this contact", "denger"));
		}
		return "redirect:/user/show-contacts/0";
	}
	
	/** Open Update form handler */
	@GetMapping("/update-contact/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId, Model model ) {
		Contact contact = this.userService.getContactById(cId);
		
		model.addAttribute("title", "Update contact - Smart Contact Manager");
		model.addAttribute("subTitle", "Update your Contact");
		model.addAttribute("contact", contact);
		
		return "user/update_contact";
	}

	
	/** Process the update contact form  */
	@PostMapping("/process-update-contact")
	public String processUpdateContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model model, Principal principal, HttpSession session) {
		
		 /** old contact details */
		 Contact oldContact = this.userService.getContactById(contact.getcId());
		
		try {
		
		/** we need current user details to set inside contact entity fields */
		 User currentUser = this.userService.findUserByEmail(principal.getName());
				 
		 /** now set this user into passed contact list*/
		 contact.setUser(currentUser);
		
		 /** multi-part file writer */
		 File saveFile = new ClassPathResource("/static/image").getFile();
		 
		 /** creating unique file-name for each image */
			String uniqueImageName = (LocalDateTime.now()+"").replace(":", "-")+"@"+file.getOriginalFilename();
		 
		/** If file is not empty */
		if(!file.isEmpty()) {
			
			/** means user send updated photo*/
			
			// first delete previous photo from DB and from saved folder
			if(oldContact.getImage() !=null) {
				File deleteFile = new File(saveFile, oldContact.getImage());
				deleteFile.delete();
			}
			
			// Update new image into our folder location
			Path path = Paths.get(saveFile+File.separator+uniqueImageName);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			/** now copied file name must be save with same name inside DATABASE */
			contact.setImage(uniqueImageName);
			
		}else {
			
			
			/** mean user not update photo, then set it's previous/old photo*/
			if(oldContact.getImage() !=null)
				contact.setImage(oldContact.getImage());
			else
				contact.setImage("contact_profile.png");
			
		}
			
		
			/** here contact is saved if exist then update otherwise it saved as new contact */
			Contact updatedContact = this.userService.updateContactInUser(contact);
			session.setAttribute("message", new Message("Contact successfully updated", "success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Contact updation failed ", "danger"));
			e.printStackTrace();
			model.addAttribute("contact", oldContact);
			return "redirect:/user/update-contact/"+contact.getcId();
		}
		
		
		System.out.println("contact name : "+contact.getName());
		System.out.println("contact ID : "+contact.getcId());
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	/** user profile hander */
	@GetMapping("/profile")
	public String showUserProfile() {
		
		return "user/profile";
	}
	
	
	
	
	
	
}
