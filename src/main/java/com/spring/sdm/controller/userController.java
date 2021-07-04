package com.spring.sdm.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.spring.sdm.helper.Message;
import com.spring.sdm.service.contactService;
import com.spring.sdm.service.userService;
import com.spring.sdm.vo.Contact;
import com.spring.sdm.vo.User;

@Controller
@RequestMapping("/user")
public class userController {
	
	@Autowired
	private userService userService;
	@Autowired
	private contactService contactService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String index(Model model, Principal principal) {
		model.addAttribute("title","User_dashboard-SCM");
		return "normal/user_dashboard";
	}
	
	@RequestMapping("/add-contact")
	public String addContactForm(Model model,Principal principal) {
		model.addAttribute("title","Add Contact-SCM");
		model.addAttribute("contact",new Contact());
		return "normal/add-contact-form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
			Model model,
			Principal principal,
			HttpSession session) {
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		
		String UPLOAD_DIR=null;
		String filename=null;
		try {
			
			if(file.isEmpty()) {
				System.out.println("FIle is Empty");
				contact.setImage("default.png");
			}
			else {
				UPLOAD_DIR = new ClassPathResource("static/image").getFile().getAbsolutePath();
				System.out.println(contact.getCid());
				if(file.getContentType().equals("image/jpeg")) {
					filename=Integer.toString(contact.getCid())+contact.getName()+".jpeg";
				}
				else if(file.getContentType().equals("image/png")) {
					filename=Integer.toString(contact.getCid())+contact.getName()+".png";
				}
				
				Path path=Paths.get(UPLOAD_DIR+File.separator+filename);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("File uploaded");
				session.setAttribute("message", new Message("Contact added successfully !! Add more..","alert-success" ));
				contact.setImage(filename);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			session.setAttribute("message", new Message("Something went wrong !! Try again..", "alert-danger"));
			e1.printStackTrace();
		}
	
		contact.setUser(user);
	
		this.contactService.saveContact(contact);
	
		model.addAttribute("title","Add Contact-SCM");
		model.addAttribute("contact", new Contact());
		return "redirect:/user/add-contact";
	}
	
	@GetMapping("/view-contacts/{page}")
	public String showContacts(@PathVariable("page") int page,Model model,Principal principal) {
		int n=7;
		int start=page*n;
		
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		int totalSize=this.contactService.findContactListSize(user.getId());
		
		int totalPage=(int)Math.ceil(totalSize/n);
		
		if(totalSize%n!=0) {
			totalPage++;
		}
		
		List<Contact> ls=this.contactService.findContactByUser(user.getId(),start,n);
		if(totalPage==0) {
			model.addAttribute("flag", true);
		}
		model.addAttribute("title","View Contacts-SCM");
		model.addAttribute("contacts", ls);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPage", totalPage);
		
		return "normal/show_contacts";
	}
	
	@GetMapping("{cid}/contact_details")
	public String contactDetails(@PathVariable("cid") int cid,Model model,Principal principal) {
		
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		
		Contact contact=this.contactService.findContactById(cid);
		if(user.getId()==contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title",contact.getName()+"-SCM");
		}
		
		
		return "normal/contact_details";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid,Model model,Principal principal,HttpSession httpSession) throws IOException {
		
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		Contact contact=this.contactService.findContactById(cid);
		String UPLOAD_DIR = new ClassPathResource("static/image").getFile().getAbsolutePath();
		
		if(user.getId()==contact.getUser().getId()) {
			contact.setUser(null);
			
			if(!contact.getImage().equals("default.png")){
				Path path=Paths.get(UPLOAD_DIR+File.separator+contact.getImage());
				Files.delete(path);
			}
			
			this.contactService.deleteContact(contact);
			httpSession.setAttribute("message", new Message("Contact delete successfully!!","alert-success"));
		}
		return "redirect:/user/view-contacts/0";
	}
	
	@GetMapping("/update/{cid}")
	public String updateContact(@PathVariable("cid") int cid,Model model,Principal principal,HttpSession httpSession) throws IOException {
		
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		Contact contact=this.contactService.findContactById(cid);
		
		if(user.getId()==contact.getUser().getId()) {
			model.addAttribute("title","Update Contact-SCM");
			model.addAttribute("contact",contact);
			
		}
		return "normal/update_contact";
	}
	
	@PostMapping("/update-contact")
	public String processUpdateContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
			Model model,
			Principal principal,
			HttpSession session) throws IOException {
		
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		contact.setUser(user);
		Contact oldcontact=this.contactService.findContactById(contact.getCid());
		
		if(!file.isEmpty()) {
			String UPLOAD_DIR = new ClassPathResource("static/image").getFile().getAbsolutePath();
			String filename="";
			
			if(file.getContentType().equals("image/jpeg")) {
				filename=Integer.toString(contact.getCid())+contact.getName()+".jpeg";
			}
			else if(file.getContentType().equals("image/png")) {
				filename=Integer.toString(contact.getCid())+contact.getName()+".png";
			}
			
			if(!oldcontact.getImage().equals("default.png")) {
				Path deletePath=Paths.get(UPLOAD_DIR+File.separator+oldcontact.getImage());
				Files.delete(deletePath);
			}
			
			
			Path path=Paths.get(UPLOAD_DIR+File.separator+filename);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(filename);
		}
		else {
			contact.setImage(oldcontact.getImage());
			
		}

		this.contactService.saveContact(contact);
		session.setAttribute("message",new Message("Contact update successfully..", "alert-success"));
		
		return "redirect:/user/"+contact.getCid()+"/contact_details";
		
	}
	
	@GetMapping("/my-profile")
	public String userProfile(Model model,Principal principal) {
		model.addAttribute("title","My Profile-SCM");
		return "normal/user_profile";
	}
	@GetMapping("/update-profile")
	public String updateProfile(Model model,Principal principal) {
		model.addAttribute("title","Update Profile-SCM");
		return "normal/update_profile";
	}
	
	@PostMapping("/update-profile")
	public String processProfile(@ModelAttribute User user,@RequestParam("profileImage") MultipartFile file,Principal principal,HttpSession session) throws IOException {
		String username=principal.getName();
		User oldUser=this.userService.getUserByUsername(username);
		
		if(!file.isEmpty()) {
			String UPLOAD_DIR = new ClassPathResource("static/image").getFile().getAbsolutePath();
			String filename="";
			
			if(file.getContentType().equals("image/jpeg")) {
				filename=Integer.toString(user.getId())+user.getName()+".jpeg";
			}
			else if(file.getContentType().equals("image/png")) {
				filename=Integer.toString(user.getId())+user.getName()+".png";
			}
			if(!oldUser.getImageUrl().equals("default.png")) {
				Path deletePath=Paths.get(UPLOAD_DIR+File.separator+oldUser.getImageUrl());
				Files.delete(deletePath);
			}
			
			Path path=Paths.get(UPLOAD_DIR+File.separator+filename);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			user.setImageUrl(filename);
			System.out.println("akshay");
		}
		else {
			user.setImageUrl(oldUser.getImageUrl());
		}
		
		this.userService.saveUser(user);
		
		session.setAttribute("message",new Message("Profile update successfully..,","alert-success"));
		return "redirect:/user/my-profile";
	}
	
	@GetMapping("/settings")
	public String openSettings(Model model) {
		model.addAttribute("title","Settings-SCM");
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword,Principal principal,HttpSession session){
		String username=principal.getName();
		User user=this.userService.getUserByUsername(username);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword,user.getPassword())) {
			if(newPassword.equals(confirmPassword)) {
				user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				this.userService.saveUser(user);
				session.setAttribute("message", new Message("Your password changed successfully!!","alert-success"));
			}
			else {
				session.setAttribute("message",new Message("Your newPassword and confirmPassword are not same!!","alert-danger"));
				return "redirect:/user/settings";
			}
		}
		else {
			session.setAttribute("message",new Message("Please enter correct old password!!", "alert-danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
}

