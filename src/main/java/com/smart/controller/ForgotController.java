package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.helper.Message;
import com.smart.model.User;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Random random=new Random(100000);

	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session) {
		
		
		int otp=random.nextInt(999999);
		
		String subject="OTP From Smart Contact Manager";
		String message="<h1>OTP = "+otp+" </h1>";
		String to= email;
		
		boolean flag=emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}else {
			session.setAttribute("message","Check your email id !!");
			
			return "forgot_email_form";
		}
		
		
	}
	
	
//	verify otp
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session) {
		int myotp=(int) session.getAttribute("myotp");
		String email=(String) session.getAttribute("email");
		if(myotp==otp) {
			User user=userRepository.getUserByUserName(email);
			if(user==null) {
				session.setAttribute("message","User does not exists with this email id !!");
				
				return "forgot_email_form";
			}else {
				
			}
			return "password_change_form";
		}else {
			session.setAttribute("message","You have entered wrong OTP !!!" );
			return "verify_otp";
		}
		
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String password,HttpSession session) {
		String email=(String) session.getAttribute("email");
		User user=userRepository.getUserByUserName(email);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
		
		return "redirect:/signin?change=password changed successfully...";
	}
}
