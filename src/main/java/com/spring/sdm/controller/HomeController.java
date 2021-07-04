package com.spring.sdm.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.sdm.helper.Message;
import com.spring.sdm.service.sendMailService;
import com.spring.sdm.service.userService;
import com.spring.sdm.vo.User;

@Controller
public class HomeController {
	
	@Autowired
	private userService userService;
	@Autowired
	private sendMailService sendMailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/")
	public String home(Model model) {
		model.addAttribute("title", "Home-SCM");
		return "home";
	}
	
	@GetMapping(value = "/about")
	public String about(Model model) {
		model.addAttribute("title", "About-SCM");
		return "about";
	}
	
	@GetMapping(value = "/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup-SCM");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	
	@PostMapping(value = "/doSignup")
	public String doSignup(@ModelAttribute("user") User user, 
			@RequestParam(value="aggrement",defaultValue = "false") boolean aggrement,
			Model model,
			HttpSession session ) {
		try {
			
			if(!aggrement) {
				
				throw new Exception("You have not agrred terms and conditions.");
			}
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			user.setActive(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("default.png");
			this.userService.saveUser(user);
			model.addAttribute("title","Signup-SCM");
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully Registered !!","alert-success"));
			this.sendMailService.sendEmail("Welcome to smart contact manager community","Greetings from SCM",user.getEmail());
			return "signup";
			
		} catch (Exception e) {
			System.out.println("akshay");
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("title","Signup-SCM");
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went wrong!! "+e.getMessage(),"alert-danger"));
			return "signup";
		}
	}
	
	@GetMapping(value = "/login")
	public String login(Model model) {
		model.addAttribute("title", "Login-SCM");
		return "login";
	}
}
