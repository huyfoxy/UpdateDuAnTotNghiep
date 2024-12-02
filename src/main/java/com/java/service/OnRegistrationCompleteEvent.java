package com.java.service;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.java.entity.Customer;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private final String appUrl;
	private final Locale locale;
	private final Customer customer;

	public OnRegistrationCompleteEvent(final Customer customer, final Locale locale, final String appUrl) {
		super(customer);
		this.customer = customer;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	//

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public Customer getCustomer() {
		return customer;
	}

}
