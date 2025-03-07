package com.manager.smartcontactmanager.Controller;

import org.hibernate.exception.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manager.helper.Message;
import com.manager.smartcontactmanager.Entity.User;
import com.manager.smartcontactmanager.Repositry.UserRepositry;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ManagerControl {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepositry userrepo;
	
	@GetMapping("/smart-manager")
	public String smartManager(Model mode) {
		mode.addAttribute("title", "home-smartcontactmanager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model mode) {
		mode.addAttribute("title", "about-smartcontactmanager");
		return "about";
	}
	
	@GetMapping("/contact")
	public String contact(Model mode) {
		mode.addAttribute("title", "contact-smartcontactmanager");
		return "contact";
	}
	
	@PostMapping("/submitContact")
	public String submitcontact(Model mode) {
		mode.addAttribute("title", "contact-smartcontactmanager");
		return "submitContact";
	}
	
	@GetMapping("/register")
	public String register(Model mode) {
		mode.addAttribute("title", "Signup-smartcontactmanager");
		mode.addAttribute("user_registry", new User());
		return "register";
	}
	
	@PostMapping("/registry-process")
	public String registryprocess(@Valid @ModelAttribute("user_registry") User user, 
			BindingResult result,
			@RequestParam(value = "terms", defaultValue = "false") boolean agreed, 
			HttpSession session, Model model) {
		try {
			if(!agreed) {
				throw new Exception();
			}
			
			if(result.hasErrors())
			{
				model.addAttribute("user_registry",user);
				return "register";
			}
			user.setImgUrl("default_img.jpg");
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userrepo.save(user);
			
			Message success = new Message("Successfully Registered !!","alert-success");
			session.setAttribute("message", success);
			model.addAttribute("message", success);
			session.removeAttribute("message");
			return "register";
		} catch (DataIntegrityViolationException ex) {
	        // Handle duplicate email case
	        ex.printStackTrace();
	        Message error = new Message("Email already exists! Please use a different email.", "alert-danger");
	        session.setAttribute("message", error);
	        return "register";
		}
		catch (Exception e) {
			e.printStackTrace();
			Message error = new Message("Please check the terms and conditions !!", "alert-danger");
			session.setAttribute("message", error);
			model.addAttribute("message", error);
			session.removeAttribute("message");
			return "register";
		}
	}
	
	@GetMapping("/login")
	public String login(Model mode) {
		mode.addAttribute("title", "login-smartcontactmanager");
		return "login";
	}
}
