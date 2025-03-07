package com.manager.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.manager.smartcontactmanager.Entity.User;
import com.manager.smartcontactmanager.Repositry.UserRepositry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDetailServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Autowired
	private UserRepositry userRepositry;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.debug("Attempting to load user by email: {}", email);
		User user =  userRepositry.getUserByEmail(email);
		
		if(user==null) {
			logger.debug("User not found: {}", email);
			throw new UsernameNotFoundException("Could not found user with this email address..");
		}
		logger.debug("User found: {}", user); 
		CustomerUserDetails customeruserdetails	 =	new CustomerUserDetails(user);
		return customeruserdetails;
	}

}
