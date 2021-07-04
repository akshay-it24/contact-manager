package com.spring.sdm.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.sdm.service.sendMailService;
import com.spring.sdm.service.userService;
import com.spring.sdm.vo.User;

@Controller
public class forgotController {
	Random random=new Random(100000);
	
	@Autowired
	private sendMailService SendMailService;
	@Autowired
	private userService userService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/forgot-password")
	public String openForgotPasswordForm(Model model) {
		model.addAttribute("title","forgot password-SCM");
		return "forgot-form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session,Model model) {
		
		boolean emailFlag=false;
		
		List<User> ls=this.userService.fetchAllUser();
		for(int i=0;i<ls.size();i++) {
			if(ls.get(i).getEmail().equals(email)) {
				emailFlag=true;
				break;
			}
		}
		
		if(emailFlag) {
			int otp=random.nextInt(999999);
			String subject="OTP from SCM";
			String message="your password change otp is "+otp;
			boolean flag=this.SendMailService.sendEmail(message,subject,email);
			
			if(flag) {
				session.setAttribute("otp",otp);
				session.setAttribute("email", email);
				model.addAttribute("title","Verify OTP-SCM");
				return "verify-otp";
			}
			else {
				session.setAttribute("message", "Please check your email id!!");
				model.addAttribute("title","forgot password-SCM");
				return "forgot-form";
			}
		}
		else {
			session.setAttribute("message","Your email id does't exist!!");
			model.addAttribute("title","forgot password-SCM");
			return "forgot-form";
		}	
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session,Model model) {
		int myotp=(int)session.getAttribute("otp");
		if(myotp==otp) {
			model.addAttribute("title","change password-SCM");
			return "change-password";
		}
		else {
			session.setAttribute("message", "You have entered wrong otp");
			model.addAttribute("title","Verify OTP-SCM");
			return "verify-otp";
		}
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword,
			HttpSession session,
			Model model) {
		if(newPassword.equals(confirmPassword)) {
			String email=(String)session.getAttribute("email");
			User user=this.userService.getUserByUsername(email);
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userService.saveUser(user);
			return "redirect:/login?change=Password changed successfully!!";
		}
		else {
			model.addAttribute("title","change password-SCM");
			session.setAttribute("message","new password and confirm password are not same!!");
			return "change-password";
		}
	}
}
