package com.java.service;

import java.io.UnsupportedEncodingException;

import com.java.entity.Customer;

public interface IUserService {

	void createVerificationTokenForUser(Customer customer, String token);

	String validateVerificationToken(String token);

	Customer getCustomer(String verificationToken);

	String generateQRUrl(Customer customer) throws UnsupportedEncodingException;

}
