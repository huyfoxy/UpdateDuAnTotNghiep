package com.java.config.google2fa;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.java.entity.Customer;
import com.java.repository.CustomersRepository;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private CustomersRepository customersRepository;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
		Customer customer = customersRepository.findCustomersLogin(auth.getName());
		if ((customer == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}
//		if (customer.isUsing2FA()) {
//			Totp totp = new Totp(customer.getSecret());
//			if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
//				throw new BadCredentialsException("Invalid verfication code");
//			}
//		}

		Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(customer, result.getCredentials(), result.getAuthorities());
	}

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
