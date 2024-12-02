package com.java.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.entity.Customer;

@Controller
@RequestMapping(value = "/")
public class CommomController {

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
//			customer = (Customer) ((OAuth2AuthenticationToken) principal).getAuthorities();
		}
		return customer;
	}

}
