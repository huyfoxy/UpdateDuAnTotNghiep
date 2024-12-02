package com.java.controller;

import java.security.Principal;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.entity.Customer;

@Controller
public class MyErrorController implements ErrorController {

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}
	
	@RequestMapping("/error")
	public String handleError() {
		// do something like logging
		return "site/notFound";
	}

	public String getErrorPath() {
		return null;
	}

}
