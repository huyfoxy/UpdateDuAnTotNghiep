package com.java.service;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.java.entity.CartItem;
import com.java.entity.Customer;
import com.java.entity.Order;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	@Autowired
	private IUserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
		final Customer user = event.getCustomer();
		final String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(user, token);

		// final SimpleMailMessage email = constructEmailMessage(event, user, token);
		// mailSender.send(email);
		try {
			sendSimpleEmail(event, user, token);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//

	private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final Customer user,
			final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
		final String message = messages.getMessage("message.regSuccLink", null,
				"You registered successfully. To confirm your registration, please click on the below link.",
				event.getLocale());
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		email.setFrom("bookstore2021vn@gmail.com");
		return email;
	}

	public void sendSimpleEmail(final OnRegistrationCompleteEvent event, final Customer user, final String token)
			throws MessagingException {

		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;

		Locale locale = LocaleContextHolder.getLocale();

		// Prepare the evaluation context
		Context ctx = new Context(locale);

		ctx.setVariable("url", confirmationUrl);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setSubject("Xác Nhận Đăng Ký");
		mimeMessageHelper.setTo(user.getEmail());
		// Create the HTML body using Thymeleaf
		String htmlContent = "";
		htmlContent = templateEngine.process("mail/email_registered.html", ctx);
		mimeMessageHelper.setText(htmlContent, true);

		// Send Message!
		mailSender.send(mimeMessage);

	}

}
