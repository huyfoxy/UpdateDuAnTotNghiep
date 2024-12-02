package com.java.common;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.java.entity.CartItem;
import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.repository.AuthorRepositoy;
import com.java.repository.BookRepository;
import com.java.repository.CategoryRepository;
import com.java.repository.SaveRepository;
import com.java.service.ShoppingCartService;

/**
 * @author Admin
 *
 */
@Service
public class ServiceCommon {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AuthorRepositoy authorRepositoy;

	@Autowired
	TemplateEngine templateEngine;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	SaveRepository saveRepository;

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}

	public void commonData(Model model, Customer customer) {
		listCategoryByBookName(model);
		listAuthorByBookName(model);
		listNXBByBookName(model);
		Integer totalSave = 0;
		// get count yêu thích
		if (customer != null) {
			totalSave = saveRepository.selectCountSave(customer.getCustomerId());
		}

		Integer totalCartItems = shoppingCartService.getCount();

		model.addAttribute("totalSave", totalSave);

		model.addAttribute("totalCartItems", totalCartItems);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);

	}

	// Hiển thị mỗi thể loại sách có bao nhiêu cuốn
	public void listCategoryByBookName(Model model) {

		List<Object[]> coutnBookByCategory = bookRepository.listCategoryByBookName();
		model.addAttribute("coutnBookByCategory", coutnBookByCategory);
	}

	// Hiển thị mỗi tác giả có bao nhiêu tác phẩm
	public void listAuthorByBookName(Model model) {

		List<Object[]> coutnBookByAuthor = bookRepository.listAuthorByBookName();
		model.addAttribute("coutnBookByAuthor", coutnBookByAuthor);
	}

	// Hiển thị mỗi NXB có bao nhiêu tác phẩm
	public void listNXBByBookName(Model model) {

		List<Object[]> coutnBookByNXB = bookRepository.listNXBByBookName();
		model.addAttribute("coutnBookByNXB", coutnBookByNXB);
	}

	public void sendSimpleEmail(String email, String subject, String contentEmail, Collection<CartItem> cartItems,
			double totalPrice, Order orderFinal) throws MessagingException {
		Locale locale = LocaleContextHolder.getLocale();

		// Prepare the evaluation context
		Context ctx = new Context(locale);
		ctx.setVariable("cartItems", cartItems);
		ctx.setVariable("totalPrice", totalPrice);
		ctx.setVariable("orderFinal", orderFinal);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(email);
		// Create the HTML body using Thymeleaf
		String htmlContent = "";
		htmlContent = templateEngine.process("mail/email_en.html", ctx);
		mimeMessageHelper.setText(htmlContent, true);

		// Send Message!
		emailSender.send(mimeMessage);

	}

}
