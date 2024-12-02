package com.java.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.java.entity.*;
import com.java.repository.*;
import com.java.service.BookService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

	@Value("./") // Tiêm giá trị cấu hình từ file application.properties (ở đây là đường dẫn lưu
					// ảnh pathUploadImage).
	private String pathUploadImage;
	
	@Autowired
	BookService bookService;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AuthorRepositoy authorRepositoy;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CompanieRepository companieRepository;

	@Autowired
	CustomersRepository customerRepository;

	@GetMapping(value = "/admin/home")
	public String home() {

		return "admin/index";
	}

	// show order
	@GetMapping(value = "/admin/orders")
	public String orders(Model model) {

		List<Order> listOrders = orderRepository.findAll();
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("order", new Order());

		return "admin/orderList";
	}

	@RequestMapping(value = "/admin/addOrder", method = RequestMethod.POST)
	public String addOrder(@ModelAttribute("order") Order order, ModelMap model,
			HttpServletRequest httpServletRequest) {
		order.setCreatedAt(new Date());
		Order b = orderRepository.save(order);
		if (null != b) {
			model.addAttribute("message", "Thêm mới thành công!");
			model.addAttribute("order", order);
		} else {
			model.addAttribute("message", "Thêm mới thất bại!");
			model.addAttribute("order", order);
		}
		model.addAttribute("message", "Thêm mới thành công!");

		return "redirect:/admin/orders";
	}

	// Edit đơn hàng
	@GetMapping(value = "/admin/updateOrder")
	public String updateOrder(@RequestParam("id") int id, Model model, @ModelAttribute("order") Order order) {
		Optional<Order> orderDto = orderRepository.findById(id);

		Order orderDsp = new Order();
		BeanUtils.copyProperties(orderDto.get(), orderDsp);
		model.addAttribute("order", orderDsp);
		return "admin/editOrder";
	}

	// update đơn hàng 
	@PostMapping(value = "/admin/doUpdateOrder")
	public String doUpdateOrder(
	        @RequestParam("id") Integer id,
	        @ModelAttribute("order") Order order,
	        RedirectAttributes rs) {
	    try {
	        // Gán ID nếu cần thiết
	        order.setId(id);

	        // Cập nhật Order
	        Order updatedOrder = orderRepository.save(order);

	        // Thêm thông báo thành công
	        rs.addFlashAttribute("message", "Cập nhật đơn hàng thành công!");
	        rs.addFlashAttribute("order", updatedOrder);
	    } catch (Exception e) {
	        // Xử lý lỗi và thêm thông báo thất bại
	        rs.addFlashAttribute("message", "Cập nhật đơn hàng thất bại. Vui lòng thử lại.");
	        rs.addFlashAttribute("order", order);
	    }

	    // Chuyển hướng về trang danh sách đơn hàng
	    return "redirect:/admin/orders";
	}

//	@PostMapping(value = "/admin/doUpdateOrder")
//	public String doUpdateOrder(@RequestParam("id") Integer id, @ModelAttribute("order") Order order, Model model,
//			RedirectAttributes rs) {
//		order.setId(id);
//		Order cs = orderRepository.save(order);
//		if (cs != null) {
//			model.addAttribute("message", "Update success");
//			model.addAttribute("order", orderRepository.findById(cs.getId()));
//		} else {
//			model.addAttribute("message", "Update failure");
//			model.addAttribute("order", order);
//		}
//
//		return "redirect:/admin/orders";
//	}

	// delete author
	@GetMapping("/admin/deleteOrder")
	public String deleteOrder(@RequestParam("id") Integer id, Model model) {

		orderRepository.deleteById(id);
		return "redirect:/admin/orders";
	}

	// show books
	@GetMapping(value = "/admin/books")
	public String books(Model model) {

		List<Book> listBooks = bookRepository.findAll();
		model.addAttribute("listBooks", listBooks);

		model.addAttribute("book", new Book());

		return "admin/bookList";
	}

	// Hiển thị list company select
	@ModelAttribute("companyList")
	public List<Companie> companyList(Model model) {
		List<Companie> companyList = companieRepository.findAll();
		model.addAttribute("companyList", companyList);
		return companyList;
	}

	// Hiển thị list category select
	@ModelAttribute("categoryList")
	public List<Category> categories(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return categoryList;
	}

	// Hiển thị list author select
	@ModelAttribute("authorList")
	public List<Author> authors(Model model) {
		List<Author> authorList = authorRepositoy.findAll();
		model.addAttribute("authorList", authorList);
		return authorList;
	}

	@ModelAttribute("customerList")
	public List<Customer> customers(Model model) {
		List<Customer> customerList = customerRepository.findAll();
		model.addAttribute("customerList", customerList);
		return customerList;
	}

	// update author
	@PostMapping("/update/{id}")
	public String updateAuthor(@PathVariable("id") Integer id, @Valid Author author, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			author.setId(id);
			return "update-author";
		}

		authorRepositoy.save(author);
		return "redirect:/admin/authorList";
	}

	// add product
	@RequestMapping(value = "admin/addBook", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute("book") Book book, ModelMap model,
			@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
		try {
			File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		book.setBookImage(file.getOriginalFilename());
		Book b = bookRepository.save(book);
		if (null != b) {
			model.addAttribute("message", "Thêm mới thành công!");
			model.addAttribute("book", book);
		} else {
			model.addAttribute("message", "Thêm mới thất bại!");
			model.addAttribute("book", book);
		}
		model.addAttribute("message", "Thêm mới thành công!");

		return "redirect:/admin/books";
	}
//
//	// Edit book
	@GetMapping(value = "/admin/updateBook")
	public String updateBook(@RequestParam("id") int id, Model model, @ModelAttribute("book") Book book) {
		Optional<Book> bookDto = bookRepository.findById(id);

		Book bookDsp = new Book();
		BeanUtils.copyProperties(bookDto.get(), bookDsp);
		model.addAttribute("book", bookDsp);
		return "admin/editBook";
	}
//
//	// update book
	@PostMapping("/admin/doUpdateBook/{id}")
	public String doUpdateBook(
	        @PathVariable("id") Integer id,
	        @ModelAttribute("book") Book book,
	        RedirectAttributes rs) {
	    
	    // In ra các tham số nhận được
	    System.out.println("Received request to update book with ID: " + id);
	    System.out.println("Book details:");
	    System.out.println("Book Name: " + book.getBookName());
	    System.out.println("Description: " + book.getDescription());
	    System.out.println("Publish Date: " + book.getPublishDate());
	    System.out.println("Suggest: " + book.getSuggest());
	    System.out.println("Publishing House: " + book.getPublishingHouse());
	    System.out.println("Translator: " + book.getTranslator());
	    System.out.println("Number of Pages: " + book.getNumberOfPages());
	    System.out.println("Quality: " + book.getQuality());
	    System.out.println("Price: " + book.getPrice());
	    System.out.println("Cover Price: " + book.getCoverPrice());
	    System.out.println("Book Image: " + book.getBookImage());
	    System.out.println("Images: " + book.getImages());
	    System.out.println("Created At: " + book.getCreatedAt());
	    System.out.println("Updated At: " + book.getUpdatedAt());
	    System.out.println("Category: " + book.getCategory());
	    System.out.println("Author: " + book.getAuthor());
	    System.out.println("Companie: " + book.getCompanie());

	    try {
	        Book updatedBook = bookService.updateBook(id, book);
	        rs.addFlashAttribute("message", "Cập nhật sách thành công!");
	        rs.addFlashAttribute("book", updatedBook);
	    } catch (Exception e) {
	        System.out.println("Error updating book: " + e.getMessage());
	        rs.addFlashAttribute("message", "Cập nhật sách thất bại: " + e.getMessage());
	        rs.addFlashAttribute("book", book);
	    }
	    return "redirect:/admin/books";
	}
//	@PostMapping("/admin/doUpdateBook/{id}")
//    public String doUpdateBook(
//            @PathVariable("id") Integer id,
//            @ModelAttribute("book") Book book,
//            RedirectAttributes rs) {
//        try {
//            Book updatedBook = bookService.updateBook(id, book);
//            rs.addFlashAttribute("message", "Cập nhật sách thành công!");
//            rs.addFlashAttribute("book", updatedBook);
//        } catch (Exception e) {
//            rs.addFlashAttribute("message", "Cập nhật sách thất bại: " + e.getMessage());
//            rs.addFlashAttribute("book", book);
//        }
//        return "redirect:/admin/books";
//    }
	
	
//	@PostMapping(value = "/admin/doUpdateBook/{id}")
//	public String doUpdateBook(@PathVariable("id") Integer id, @ModelAttribute("book") Book book, Model model,
//			RedirectAttributes rs) {
//		book.setId(id);
//		Book cs = bookRepository.save(book);
//		if (cs != null) {
//			model.addAttribute("message", "Update success");
//			model.addAttribute("category", categoryRepository.findById(cs.getId()));
//		} else {
//			model.addAttribute("message", "Update failure");
//			model.addAttribute("category", book);
//		}
//
//		return "redirect:/admin/books";
//	}
//
	
	
	
//	// delete book
	@GetMapping("admin/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") Integer id, Model model) {

		bookRepository.deleteById(id);
		return "redirect:/admin/books";
	}

}
