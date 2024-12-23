package com.java.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.entity.Customer;
import com.java.repository.CustomersRepository;

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

	/*// add customer
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
	}*/
	@PostMapping(value = "/admin/addUser")
	public String addUser(Model model, 
	                      @Valid @ModelAttribute("customer") Customer customer, 
	                      BindingResult result,
	                      HttpServletRequest httpServletRequest,
	                      RedirectAttributes redirectAttributes) {
	    // Kiểm tra username đã tồn tại hay chưa
	    Customer existingCustomer = customerRepositoy.findCustomersLogin(customer.getCustomerId());
	    // Kiểm tra email đã tồn tại hay chưa
	    Optional<Customer> existingEmail = customerRepositoy.findByEmail(customer.getEmail());

	    // Nếu username đã tồn tại
	    if (existingCustomer != null) {
	        redirectAttributes.addFlashAttribute("error", "Tên tài khoản đã tồn tại!");
	        redirectAttributes.addFlashAttribute("customer", customer); // Giữ lại dữ liệu
	        return "redirect:/admin/userList";
	    }

	    // Nếu email đã tồn tại
	    if (existingEmail.isPresent()) {
	        redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
	        redirectAttributes.addFlashAttribute("customer", customer); // Giữ lại dữ liệu
	        return "redirect:/admin/userList";
	    }

	    // Xử lý khi dữ liệu hợp lệ
	    try {
	        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
	        customer.setUsing2FA(false);
	        customer.setRoleId("2");
	        Customer savedCustomer = customerRepositoy.save(customer);

	        if (savedCustomer != null) {
	            redirectAttributes.addFlashAttribute("success", "Thêm mới người dùng thành công!");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Thêm mới thất bại! Vui lòng thử lại.");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi trong quá trình thêm mới!");
	    }

	    return "redirect:/admin/userList";
	}


	/*// delete customer
	@GetMapping("/admin/deleteUser")
	public String deleteAuthor(@RequestParam("id") String id, Model model) {
		Customer customer = customerRepositoy.findCustomersLogin(id);
		if (null != customer){
			customerRepositoy.delete(customer);
		}

		return "redirect:/admin/userList";
	}*/
	@GetMapping("/admin/deleteUser")
	public String deleteUser(@RequestParam("id") String id, Model model, RedirectAttributes redirectAttributes) {
	    // Tìm customer theo ID
	    Customer customer = customerRepositoy.findCustomersLogin(id);
	    
	    if (customer != null) {
	        try {
	            // Kiểm tra ràng buộc: xem customer có liên kết với các Order không
	            if (!customer.getOrders().isEmpty()) {
	                // Nếu có liên kết, gửi thông báo cảnh báo
	                redirectAttributes.addFlashAttribute("errordelete", "Không thể xóa người dùng vì có đơn hàng liên kết.");
	            } else {
	                // Không có liên kết, tiến hành xóa
	                customerRepositoy.delete(customer);
	                redirectAttributes.addFlashAttribute("successdelete", "Xóa người dùng thành công.");
	            }
	        } catch (Exception e) {
	            // Xử lý các lỗi khác (ví dụ ngoại lệ SQL)
	            redirectAttributes.addFlashAttribute("errordelete", "Không thể xóa người dùng vì có đơn hàng liên kết.");
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại.");
	    }
	    
	    // Chuyển hướng về danh sách người dùng
	    return "redirect:/admin/userList";
	}


}
