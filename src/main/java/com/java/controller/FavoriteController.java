/**
 * 
 */
package com.java.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.common.ServiceCommon;
import com.java.entity.Book;
import com.java.entity.Customer;
import com.java.entity.Save;
import com.java.repository.BookRepository;
import com.java.repository.SaveRepository;
import com.java.service.ShoppingCartService;

/**
 * @author TuNV15
 *
 */
@Controller
public class FavoriteController {

	@Autowired
	ServiceCommon serviceCommon;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	SaveRepository saveRepository;

	@Autowired
	ShoppingCartService shoppingCartService;

	public FavoriteController(ServiceCommon serviceCommon) {
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

	@GetMapping(value = "/favorite")
	public String index(Model model, Customer customer) {
		List<Save> saveBook = saveRepository.selectAllSaves(customer.getCustomerId());
		serviceCommon.commonData(model, customer);
		model.addAttribute("saveBook", saveBook);
		listBook10(model);
		return "site/favorite";
	}

	@GetMapping(value = "/doFavorite")
	@ResponseBody
	public Map<String, String> doFavorite(Model model, Save save, Customer customer, @RequestParam("id") Integer id) {
		Map<String, String> data = new LinkedHashMap<>();
		Book book = bookRepository.getById(id);
		save.setBook(book);
		save.setCustomer(customer);
		saveRepository.save(save);
		serviceCommon.commonData(model, customer);
		listBook10(model);
		data.put("success", "add favorite ok");
		return data;
	}

	@GetMapping(value = "/doUnFavorite")
	@ResponseBody
	public Map<String, String> doUnFavorite(Model model, Customer customer, @RequestParam("id") Integer bookId) {
		Map<String, String> data = new LinkedHashMap<>();
		Save saveBook = saveRepository.selectSaves(bookId, customer.getCustomerId());
		saveRepository.delete(saveBook);
		serviceCommon.commonData(model, customer);
		listBook10(model);
		data.put("success", "add favorite ok");
		return data;
	}

	// Hiển thị Top 10 sản phẩm thịnh hành.
	public void listBook10(Model model) {
		List<Object[]> bookList = bookRepository.listBook10();
		if (bookList != null) {
			ArrayList<Integer> listIdBookArrayList = new ArrayList<>();
			for (int i = 0; i < bookList.size(); i++) {
				String id = String.valueOf(bookList.get(i)[0]);
				listIdBookArrayList.add(Integer.valueOf(id));
			}
			List<Book> listBooks = bookRepository.findByInventoryIds(listIdBookArrayList);
			model.addAttribute("listTop10Books", listBooks);
		}
	}

}
