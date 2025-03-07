package com.manager.smartcontactmanager.Controller;

import java.io.File;
import java.nio.file.*;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manager.helper.Message;
import com.manager.smartcontactmanager.Entity.Contacts;
import com.manager.smartcontactmanager.Entity.User;
import com.manager.smartcontactmanager.Repositry.ContactsRepositry;
import com.manager.smartcontactmanager.Repositry.UserRepositry;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {
	
	@Autowired
	private UserRepositry userrepo;

	@Autowired
	private ContactsRepositry contactsrepo;
	
	public User userDynamicData(Principal principal) {
		String uname = principal.getName();
		User user = userrepo.getUserByEmail(uname);
		return user;
	}

	@RequestMapping("/dashboard")
	public String user_dashboard(Model model,Principal principal) 
	{
		model.addAttribute("user",userDynamicData(principal));
		model.addAttribute("title","User_Dashboard");
		return "User/userdashboard";
	}
	
	@RequestMapping("/add-contact")
	public String addContacts(Model model) 
	{
		model.addAttribute("user_contacts", new Contacts());
		model.addAttribute("title","Add_Contacts");
		return "User/AddContacts";
	}
	@PostMapping("/process-data")
	public String processdata(@Valid @ModelAttribute("user_contacts") Contacts contacts, 
			BindingResult result,@RequestParam("profileimg") MultipartFile file, Principal principal, Model model, HttpSession session) {
		try {
			if(result.hasErrors())
			{
				model.addAttribute("user_contacts", contacts);
				return "User/AddContacts";
			}
	        if (contactsrepo.existsByName(contacts.getName())) {
	            Message message = new Message("Name already exists! Please use a different name.", "alert-danger");
	            model.addAttribute("message", message);
	            session.setAttribute("message", message);
	            return "User/AddContacts";
	        }
	        if (contactsrepo.existsByPhone(contacts.getPhone())) {
	            Message message = new Message("Phone no. already exists! Please use a different number.", "alert-danger");
	            model.addAttribute("message", message);
	            session.setAttribute("message", message);
	            return "User/AddContacts";
	        }

//	        file uploading ......
	        if(file.isEmpty())
	        {
	        	System.out.println("File is empty.......");
	        	contacts.setImg("contacts_profile_default.jpg");
	        } else {
	        	contacts.setImg(file.getOriginalFilename());
	        	File savedFile = new ClassPathResource("static/image").getFile();
	        	Path path = Paths.get(savedFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
	        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        	System.out.println("Image uploaded successfully......");
	        }
			User user = userDynamicData(principal);
			contacts.setUser(user);
			user.getContacts().add(contacts);
			userrepo.save(user);
			
			Message message = new Message("Contact is been added successfully ..", "alert-success");
			session.setAttribute("message", message);
			model.addAttribute("message", message);
			session.removeAttribute("message");
			
			return "User/AddContacts";
			
		} catch (Exception e) {
			Message message = new Message("Something gone wrong !!", "alert-danger");
			model.addAttribute("message", message);
			session.setAttribute("message", message);
			session.removeAttribute("message");
			
			return "User/AddContacts";
		}
	}
	
	@RequestMapping("/profile")
	public String profile(Model model, Principal principal) {
		model.addAttribute("user",userDynamicData(principal));
		model.addAttribute("title","Your_Profile");
		return "User/Profile";
	}
	@RequestMapping("/userabout")
	public String userabout(Model model) {
		model.addAttribute("title","About");
		return "User/user_about";
	}
	//View Contacts
	@RequestMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m, Principal principal) {
		m.addAttribute("title","View_Contacts");
		String email = principal.getName();
		User user =  userrepo.getUserByEmail(email);
		Pageable pageable = (Pageable) PageRequest.of(page, 7);
		Page<Contacts> con = contactsrepo.getAllContacts(user,pageable);
//		con.forEach(c -> {System.out.println(c);});
		m.addAttribute("contacts",con);
		m.addAttribute("currentpage",page);
		m.addAttribute("totalpages",con.getTotalPages());
		return "User/ShowContacts";
	}
	
	@RequestMapping("/{c_id}/contacts")
	public String getAllContactDetails(@PathVariable("c_id") Long Cid, Model model, Principal principal) {
		
		Optional<Contacts> optional = contactsrepo.findById(Cid);
		Contacts contacts = optional.get();
		
		User user = userDynamicData(principal);
		if(user.getId()==contacts.getUser().getId())
		{
			model.addAttribute("con_details",contacts);
		}
		return "User/All_Contact_Details";
	}
	
	@GetMapping("/delete/{cid}")
	public String getMethodName(@PathVariable("cid") Long cid,RedirectAttributes redirect, Principal principle) {
		
		Optional<Contacts> optional = contactsrepo.findById(cid);
		Contacts contacts = optional.get();
		 User user = userDynamicData(principle);
		
		if(optional.isPresent() || user.getId()==contacts.getUser().getId()) {
			contacts.setUser(null);
			contactsrepo.delete(contacts);
			redirect.addFlashAttribute("message", new Message("Contact deleted successfully....", "alert-success"));
		} else {
			redirect.addFlashAttribute("message", new Message("Contact not present....", "alert-danger"));
		}
		
		return "redirect:/user/show_contacts/0";
	}
	
	// updating Contacts details 
	@PostMapping("/update_contact/{cid}")
	public String updateContact(@PathVariable("cid") long cid, Model model) {
		
		model.addAttribute("title","Update Contact");
		Contacts contacts = contactsrepo.findById(cid).get();
		model.addAttribute("con", contacts);
		return "User/UpdateContact";
	}
	@PostMapping("/update-data")
	public String processUpdateData(@Valid @ModelAttribute("con") Contacts con,BindingResult result, Principal principal,
			@RequestParam("profileimg") MultipartFile file,Model model, RedirectAttributes redirect, HttpSession session)
	{
		if(result.hasErrors())
		{
			model.addAttribute("con", con);
			return "User/UpdateContact";
		}

		try {
			User user = userDynamicData(principal);
			Optional<Contacts> optional = contactsrepo.findById(con.getC_id());
			Contacts existContacts = optional.get();
			existContacts.setName(con.getName());
			existContacts.setNickName(con.getNickName());
			existContacts.setEmail(con.getEmail());
			existContacts.setPhone(con.getPhone());
			existContacts.setDescription(con.getDescription());
		
//	        file uploading ......
	        if(!file.isEmpty())
			{
	        	//deleting old image
	        	File deletefile = new ClassPathResource("static/image").getFile();
	        	File file2 = new File(deletefile, existContacts.getImg());
	        	file2.delete();
	        	
	        	//saving new image 
				existContacts.setImg(file.getOriginalFilename());
				File savedfile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(savedfile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				existContacts.setImg(existContacts.getImg());
			}
			con.setUser(user);
			contactsrepo.save(existContacts);
			
			Message message = new Message("Contact is been updated successfully ..", "alert-success");
			redirect.addFlashAttribute("message", message);
			model.addAttribute("message", message);model.addAttribute("message", message);
			
			return "redirect:/user/" + con.getC_id() + "/contacts";
	
		} catch (Exception e) {
			e.printStackTrace();
			Message message = new Message("Something gone wrong !!", "alert-danger");
			model.addAttribute("message", message);
			session.setAttribute("message", message);
			
			return "User/UpdateContact";
		}

	}
	
}
