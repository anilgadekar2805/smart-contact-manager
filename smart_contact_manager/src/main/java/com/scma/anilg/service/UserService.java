package com.scma.anilg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scma.anilg.dao.UserRepository;
import com.scma.anilg.entities.User;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public User userRegister(User user) {
		System.out.println("userService : "+user );
		return userRepository.save(user);
	}
	
	public User findUserByEmail(String email) {
		User resultUser = userRepository.loadUserByUsername(email);
		return resultUser;
	}
	
	public User addContactInUser(User user) {
		User result = userRepository.save(user);
		return result;
	}
	
}
