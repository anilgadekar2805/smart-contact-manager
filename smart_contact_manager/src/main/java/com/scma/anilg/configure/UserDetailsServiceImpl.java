package com.scma.anilg.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scma.anilg.dao.UserRepository;
import com.scma.anilg.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User loadedUser = userRepository.loadUserByUsername(username);
		if(loadedUser==null) {
			throw new UsernameNotFoundException("Could not find given user");
		}

		UserDetails userDetails = new UserDetailsImpl(loadedUser);
		return userDetails;
	}
	
	
}
