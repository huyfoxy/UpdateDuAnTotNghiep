package com.java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.repository.OrderDetailRepository;
import com.java.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Integer getsumOrderdetails() {
        // Lấy tổng từ repository
        Integer totalOrders = orderDetailRepository.getTotalOrders();
        // Xử lý logic (nếu cần)
        return totalOrders != null ? totalOrders : 0; // Đảm bảo không trả về null
    }
    @Override
    public Double getsumdoanhthu() {
        // Lấy tổng từ repository
    	Double Sumdoanhthu = orderDetailRepository.Tongdoanhthu();
        // Xử lý logic (nếu cần)
        return Sumdoanhthu != null ? Sumdoanhthu : 0; // Đảm bảo không trả về null
    }
    
    @Override
    public List<Object[]> getRevenueByMonth(String status) {
        return orderDetailRepository.getRevenueByMonth(status);
    }
    @Override
    public List<Object[]> getOrderDeltail(String id ) {
        return orderDetailRepository.getOrder_detail();
    }
}
