package com.java.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.common.ServiceCommon;
import com.java.config.PaypalPaymentIntent;
import com.java.config.PaypalPaymentMethod;
import com.java.entity.Book;
import com.java.entity.CartItem;
import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.entity.OrderDetail;
import com.java.repository.BookRepository;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.service.PaypalService;
import com.java.service.ShoppingCartService;
import com.java.util.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class CartController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	HttpSession session;

	public Order orderFinal = new Order();

	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PaypalService paypalService;

	@Autowired
	private ServiceCommon serviceCommon;

	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}

	@GetMapping(value = "/cartItem")
	public String shoppingCart(Model model, HttpServletRequest request) {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getBook().getPrice();
			totalPrice += price;
		}
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "site/shoppingCart";
	}

	// add cartItem
	@GetMapping(value = "/addCart")
	@ResponseBody
	public Map<String, String> add(@RequestParam("id") Integer id, HttpServletRequest request, Model model) {
		Map<String, String> data = new LinkedHashMap<>();
		Book book = bookRepository.findById(id).get();
		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (book != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(book, item);
			item.setQuantity(1);
			item.setBook(book);
			item.setBookId(id);
			item.setTotalPrice(book.getPrice());
			shoppingCartService.add(item);
		}
		session.setAttribute("cartItems", cartItems);
		session.setAttribute("totalCartItems", cartItems);
		data.put("success", "add cart ok");
		return data;
	}
	
	
	
	
	
	

	// delete cartItem
	@SuppressWarnings("unlikely-arg-type")
	@GetMapping(value = "/remove/{id}")
	public String remove(@PathVariable("id") Integer id, HttpServletRequest request, Model model) {
		Book book = bookRepository.findById(id).orElse(null);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		session = request.getSession();
		if (book != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(book, item);
			item.setBook(book);
			item.setBookId(id);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "redirect:/cartItem";
	}
	// update cart item quantity
	@PostMapping("/updateQuantity")
	public String updateQuantity(@RequestParam("quantity") Integer quantity, 
	                             @RequestParam("id") Integer id, 
	                             HttpServletRequest request, 
	                             Model model) {
	    Book book = bookRepository.findById(id).orElse(null);

	    if (book != null) {
	        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
	        CartItem cartItem = cartItems.stream()
	                                      .filter(item -> item.getBook().getId().equals(id))
	                                      .findFirst()
	                                      .orElse(null);

	        if (cartItem != null) {
	            // Cập nhật số lượng và tính lại giá trị tổng
	            cartItem.setQuantity(quantity);
	            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getBook().getPrice());
	            shoppingCartService.update(cartItem, quantity);  // Cập nhật giỏ hàng
	        }
	    }

	    // Cập nhật lại session và model
	    session = request.getSession();
	    session.setAttribute("cartItems", shoppingCartService.getCartItems());
	    session.setAttribute("totalCartItems", shoppingCartService.getCount());

	    model.addAttribute("totalCartItems", shoppingCartService.getCount());
	    return "redirect:/cartItem";  // Điều hướng về trang giỏ hàng
	}

//	@GetMapping(value = "/update/{id}")
//	public String update(@PathVariable("id") Integer id, 
//	                     @RequestParam("quantity") Integer quantity, 
//	                     HttpServletRequest request, 
//	                     Model model) {
//	    // Tìm kiếm sản phẩm theo ID trong cơ sở dữ liệu
//	    Book book = bookRepository.findById(id).orElse(null);
//
//	    if (book != null) {
//	        // Cập nhật số lượng sản phẩm trong giỏ hàng
//	        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
//	        CartItem cartItem = cartItems.stream()
//	                                      .filter(item -> item.getBook().getId().equals(id))
//	                                      .findFirst()
//	                                      .orElse(null);
//
//	        if (cartItem != null) {
//	            // Cập nhật số lượng nếu sản phẩm đã có trong giỏ
//	            cartItem.setQuantity(quantity);
//	            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getBook().getPrice());
//	            shoppingCartService.update(cartItem, quantity);  // Pass both the item and quantity
//	        }
//	    }
//	    
//	    // Cập nhật lại session và model
//	    session = request.getSession();
//	    session.setAttribute("cartItems", shoppingCartService.getCartItems());
//	    session.setAttribute("totalCartItems", shoppingCartService.getCount());
//	    
//	    model.addAttribute("totalCartItems", shoppingCartService.getCount());
//	    return "redirect:/cartItem";  // Điều hướng về trang giỏ hàng
//	}


	
	

	@GetMapping(value = "/checkout")
	public String checkOut(Model model) {

		Integer totalCartItems = shoppingCartService.getCount();

		model.addAttribute("totalCartItems", totalCartItems);

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());

		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getBook().getPrice();
			totalPrice += price;
		}

		model.addAttribute("totalPrice", totalPrice);

		return "site/checkOut";
	}

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, Customer customer)
			throws MessagingException {

		String checkOut = request.getParameter("checkOut");

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();

		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getBook().getPrice();
			totalPrice += price;
		}

		BeanUtils.copyProperties(order, orderFinal);
		if (StringUtils.equals(checkOut, "paypal")) {

			String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
			String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
			try {
				totalPrice = totalPrice / 22;
				Payment payment = paypalService.createPayment(totalPrice, "USD", PaypalPaymentMethod.paypal,
						PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				log.error(e.getMessage());
			}

		}

		session = request.getSession();
		Date date = new Date();
		order.setCreatedAt(date);
		order.setOrderStatus("Đang xử lý đơn hàng");
		order.getId();
		orderRepository.save(order);

		for (CartItem cartItem : cartItems) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuality(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setBook(cartItem.getBook());
			double unitPrice = cartItem.getBook().getPrice();
			orderDetail.setPrice(unitPrice);
			orderDetailRepository.save(orderDetail);
		}

		serviceCommon.sendSimpleEmail(customer.getEmail(), "Book Store Xác Nhận Đơn hàng", "aaaa", cartItems,
				totalPrice, order);
		shoppingCartService.clear();
		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getId());

		return "redirect:/success";
	}

	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("" + "" + "") String paymentId, @RequestParam("PayerID") String payerId,
			HttpServletRequest request, Customer customer, Model model) throws MessagingException {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getBook().getPrice();
			totalPrice += price;
		}
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {

				session = request.getSession();
				Date date = new Date();
				orderFinal.setCreatedAt(date);
				orderFinal.setOrderStatus("ĐÃ THANH TOÁN");
				orderFinal.getId();
				orderFinal.setCustomer(customer);
				orderRepository.save(orderFinal);

				for (CartItem cartItem : cartItems) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setQuality(cartItem.getQuantity());
					orderDetail.setOrder(orderFinal);
					orderDetail.setBook(cartItem.getBook());
					double unitPrice = cartItem.getBook().getPrice();
					orderDetail.setPrice(unitPrice);
					orderDetailRepository.save(orderDetail);
				}

				serviceCommon.sendSimpleEmail(customer.getEmail(), "Book Store Xác Nhận Đơn hàng", "aaaa", cartItems,
						totalPrice, orderFinal);
				shoppingCartService.clear();
				session.removeAttribute("cartItems");
				model.addAttribute("orderId", orderFinal.getId());
				orderFinal = new Order();
				return "redirect:/success";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/success";
	}

	@GetMapping(value = "success")
	public String success(Model model, HttpServletRequest request) {
		return "site/success";

	}

}
