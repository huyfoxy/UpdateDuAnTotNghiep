package com.java.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Customer;
import com.java.error.UserAlreadyExistException;
import com.java.repository.CustomersRepository;
import com.java.service.IUserService;
import com.java.service.OnRegistrationCompleteEvent;

@Controller
public class RegisterController extends CommomController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private IUserService service;

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/registered")
	public String showRegistrationForm(final HttpServletRequest request, final Model model) {
		final Customer accountDto = new Customer();
		model.addAttribute("user", accountDto);
		return "site/register";
	}

	@PostMapping("/registered")
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final Customer customer,
			final HttpServletRequest request, final Errors errors) {

		try {
			customer.setUsing2FA(true);

			customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			final Customer registered = customersRepository.save(customer);

			final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		} catch (final UserAlreadyExistException uaeEx) {
			ModelAndView mav = new ModelAndView("site/register", "user", customer);
			return mav;
		} catch (final RuntimeException ex) {
			return new ModelAndView("emailError", "user", customer);
		}
		return new ModelAndView("site/successRegister", "user", customer);
	}

	@GetMapping("/registrationConfirm")
	public String confirmRegistration(final HttpServletRequest request, final ModelMap model,
			@RequestParam("token") final String token) throws UnsupportedEncodingException {
		Locale locale = request.getLocale();
		model.addAttribute("lang", locale.getLanguage());
		final String result = service.validateVerificationToken(token);
		if (result.equals("valid")) {
			final Customer user = service.getCustomer(token);
			if (user.isUsing2FA()) {
				model.addAttribute("qr", service.generateQRUrl(user));
				return "site/qrcode";
			}
			model.addAttribute("messageKey", "message.accountVerified");
		}

		model.addAttribute("messageKey", "auth.message." + result);
		model.addAttribute("expired", "expired".equals(result));
		model.addAttribute("token", token);
		return "redirect:/badUser";
	}

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}

	// check email
	public boolean checkEmail(String email) {
		List<Customer> list = customersRepository.findAll();
		for (Customer c : list) {
			if (c.getEmail().equalsIgnoreCase(email)) {
				return false;
			}
		}
		return true;
	}

	// check ID Login
	public boolean checkIdlogin(String customerId) {
		List<Customer> listC = customersRepository.findAll();
		for (Customer c : listC) {
			if (c.getCustomerId().equalsIgnoreCase(customerId)) {
				return false;
			}
		}
		return true;
	}
}
