package com.scma.anilg.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scma.anilg.dao.ContactRepository;
import com.scma.anilg.dao.UserRepository;
import com.scma.anilg.entities.Contact;
import com.scma.anilg.entities.User;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contacRepository;
	
	public User userRegister(User user) {
		System.out.println("userService : "+user );
		return userRepository.save(user);
	}
	
	public User findUserByEmail(String email) {
		User resultUser = userRepository.loadUserByUsername(email);
		return resultUser;
	}
	
	public User addContactInUser(User user ) {
		User result = userRepository.save(user);
		return result;
	}
	
	/** get all contacts list with respective users UserID */
	public Page<Contact> getContactsList(int userId, Pageable pageable){
		Page<Contact> listContactsByUser = this.contacRepository.getContactsByUser(userId, pageable);
		return listContactsByUser;
	}
	
	/** getting respective contact details */
	public Contact getContactDetail(int cId) {
		Optional<Contact> optionalContact =  this.contacRepository.findById(cId);
		Contact contact = optionalContact.get();
		return contact;
	}
	
	/** find contact info by using user ID */
	public Contact getContactById(int cId) {
		Optional<Contact> optionalContact = this.contacRepository.findById(cId);
		Contact contact = optionalContact.get();
		return contact;
	}
	
	/** delete contact by using ID */
	public void deleteContact(Contact contact) {
		
		/** It is not deleted directly because its is mapped with user */
		contact.setUser(null);
		this.contacRepository.delete(contact);
		
		// Now we must delete photo from folder
		// CODE
	}
	
	public Contact updateContactInUser(Contact contact) {
		Contact saveContact = this.contacRepository.save(contact);
		return saveContact;
	}
	
}
