package com.java.controller.admin;

import com.java.entity.Customer;
import com.java.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

	@Value("./")
	private String pathUploadImage;

	@Autowired
	CustomersRepository customerRepositoy;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	// show customer
//	@GetMapping(value = "/admin/userList")
//	public String authorList(Model model) {
//
//		List<Customer> listCustomers = customerRepositoy.findAllRoleUser();
//		model.addAttribute("listCustomers", listCustomers);
//		model.addAttribute("customer", new Customer());
//
//		return "admin/userList";
//	}
	@GetMapping(value = "/admin/userList")
	public String authorList(Model model) {
	    List<Customer> listCustomers = customerRepositoy.findAllRoleUser();
	    
	    // Hiển thị danh sách khách hàng ra console
	    System.out.println("Danh sách khách hàng:");
	    listCustomers.forEach(System.out::println);

	    model.addAttribute("listCustomers", listCustomers);
	    model.addAttribute("customer", new Customer());

	    return "admin/userList";
	}


//	 Edit customer
	@GetMapping(value = "/admin/editUser")
	public String editAuthor(@RequestParam("id") String id, Model model, @ModelAttribute("customer") Customer customer) {
		customer = customerRepositoy.findCustomersLogin(id);
		model.addAttribute("customer", customer);
		return "admin/editUser";
	}

	// Edit customer
	@PostMapping(value = "/admin/doEditUser/{id}")
	public String editUser(@PathVariable("id") String id, @ModelAttribute("customer") Customer customer, Model model) {

		Customer oldCustomer = customerRepositoy.findCustomersLogin(id);
		oldCustomer.setEmail(customer.getEmail());
		oldCustomer.setFullname(customer.getFullname());
		oldCustomer.setEmail(customer.getEmail());
		customerRepositoy.save(oldCustomer);
		return "redirect:/admin/userList";
	}

	// add customer
	@PostMapping(value = "/admin/addUser")
	public String addUser(Model model, @Valid @ModelAttribute("customer") Customer customer, BindingResult result,
			 HttpServletRequest httpServletRequest) {
		Customer oldCustomer = customerRepositoy.findCustomersLogin(customer.getCustomerId());
		Optional<Customer> emailCustomer = customerRepositoy.findByEmail(customer.getEmail());

		if(oldCustomer != null || emailCustomer.isPresent()){
			model.addAttribute("message", "Thêm mới thất bại!");
			model.addAttribute("customer", customer);
			return "redirect:/admin/userList";
		}

		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		customer.setUsing2FA(false);
		customer.setRoleId("2");
		Customer sucessCustomer = customerRepositoy.save(customer);

		if (null != sucessCustomer) {
			model.addAttribute("message", "Thêm mới thành công!");
			model.addAttribute("customer", customer);
		} else {
			model.addAttribute("message", "Thêm mới thất bại!");
			model.addAttribute("customer", customer);
		}

		return "redirect:/admin/userList";
	}

	// delete customer
	@GetMapping("/admin/deleteUser")
	public String deleteAuthor(@RequestParam("id") String id, Model model) {
		Customer customer = customerRepositoy.findCustomersLogin(id);
		if (null != customer){
			customerRepositoy.delete(customer);
		}

		return "redirect:/admin/userList";
	}

}
