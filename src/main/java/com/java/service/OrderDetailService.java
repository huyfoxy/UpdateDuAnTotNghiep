package com.java.service;

import java.util.List;

public interface OrderDetailService  {
	Integer getsumOrderdetails();
	
	Double getsumdoanhthu();
	
	List<Object[]> getRevenueByMonth(String status);
	
}
