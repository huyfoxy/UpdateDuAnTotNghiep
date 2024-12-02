package com.java.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.common.ServiceCommon;
import com.java.entity.Author;
import com.java.entity.Customer;
import com.java.repository.AuthorRepositoy;

@Controller
public class AuthorsController {

	@Autowired
	AuthorRepositoy authorRepositoy;

	@Autowired
	ServiceCommon serviceCommon;

	public AuthorsController(ServiceCommon serviceCommon) {
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

	@GetMapping(value = "/authors")
	public String authors(Model model, Customer customer) {

		List<Author> authorPage = authorRepositoy.findAll();

		model.addAttribute("authorPage", authorPage);

		serviceCommon.commonData(model, customer);

		return "site/authors";
	}

	public Page<Author> findPaginated(Pageable pageable) {

		List<Author> authorPage = authorRepositoy.findAll();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Author> list;

		if (authorPage.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, authorPage.size());
			list = authorPage.subList(startItem, toIndex);
		}

		Page<Author> authorPages = new PageImpl<Author>(list, PageRequest.of(currentPage, pageSize), authorPage.size());

		return authorPages;
	}

	public Page<Author> listAll(int pageNum) {
		int pageSize = 5;

		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

		return authorRepositoy.findAll(pageable);
	}

	// Hiển thị mỗi NXB có bao nhiêu tác phẩm
	public void listAuthorByBook(Model model) {

		List<Object[]> coutnBookByAuthor = authorRepositoy.listAuthorByBook();
		model.addAttribute("coutnBookByAuthor", coutnBookByAuthor);
	}

}
