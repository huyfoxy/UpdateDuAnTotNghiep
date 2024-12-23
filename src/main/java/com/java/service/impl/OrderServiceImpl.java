package com.java.service.impl;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.entity.Order;
import com.java.repository.OrderRepository;
import com.java.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order updateOrder(Integer id, Order order) throws Exception {
        // Lấy bản ghi hiện tại từ cơ sở dữ liệu
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại!"));

        // Cập nhật các trường mới nhưng giữ nguyên createdAt
        existingOrder.setOrderStatus(order.getOrderStatus());
        existingOrder.setShippingAddress(order.getShippingAddress());
        existingOrder.setPhoneReceiver(order.getPhoneReceiver());
        existingOrder.setNameReceiver(order.getNameReceiver());
        existingOrder.setShippingFee(order.getShippingFee());
        existingOrder.setUpdatedAt(new Date()); // Cập nhật updatedAt

        // Lưu lại bản ghi đã được cập nhật
        return orderRepository.save(existingOrder);
    }
}
