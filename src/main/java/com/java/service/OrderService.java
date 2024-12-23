package com.java.service;

import com.java.entity.Order;

public interface OrderService {
	Order updateOrder(Integer id, Order order) throws Exception;
}
