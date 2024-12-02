package com.java.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.common.ServiceCommon;
import com.java.entity.Book;
import com.java.entity.Customer;
import com.java.entity.Save;
import com.java.repository.BookRepository;
import com.java.repository.SaveRepository;
import com.java.service.ShoppingCartService;

@Controller
public class HomeController {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	ServiceCommon serviceCommon;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	HttpSession session;

	@Autowired
	SaveRepository saveRepository;

	public HomeController(ServiceCommon serviceCommon) {
		this.serviceCommon = serviceCommon;
	}

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}

	@GetMapping(value = "/index")
	public String home(Model model, Customer customer) {

		serviceCommon.commonData(model , customer);
		listBook10(model, customer);

		listNewBook(model);

		return "site/home";
	}

	@GetMapping(value = "/search")
	public String showsearch(Model model, @RequestParam("keyword") String keyword, HttpServletRequest request , Customer customer) {
		List<Book> bookList = bookRepository.searchBook(keyword);
		model.addAttribute("bookPage", bookList);
		serviceCommon.commonData(model , customer);
		return "site/shop";
	}

	// Hiển thị Top 10 sản phẩm bán chạy nhất.
	public void listBook10(Model model, Customer customer) {
		List<Object[]> bookList = bookRepository.listBook10();
		if (bookList != null) {
			ArrayList<Integer> listIdBookArrayList = new ArrayList<>();
			for (int i = 0; i < bookList.size(); i++) {
				String id = String.valueOf(bookList.get(i)[0]);
				listIdBookArrayList.add(Integer.valueOf(id));
			}
			List<Book> listBooks = bookRepository.findByInventoryIds(listIdBookArrayList);

			List<Book> listBookNew = new ArrayList<>();

			for (Book book : listBooks) {

				Book bookEntity = new Book();

				BeanUtils.copyProperties(book, bookEntity);

				Save save = saveRepository.selectSaves(bookEntity.getId(), customer.getCustomerId());

				if (save != null) {
					bookEntity.favorite = true;
				} else {
					bookEntity.favorite = false;
				}
				listBookNew.add(bookEntity);

			}

			model.addAttribute("listTop10Books", listBookNew);
		}
	}

	// Hiển thị Top 3 cuốn sách mới phát hành
	public void listNewBook(Model model) {
		List<Book> listNewBook = bookRepository.listNewBook3();
		model.addAttribute("listNewBook", listNewBook);
	}

}
