package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	// tổng số lượng đã được đặt
	@Query(value = "SELECT SUM(od.quality) AS total_orders FROM order_details od", nativeQuery = true)
	Integer getTotalOrders();

	// tổng doanh thu
	@Query(value = "SELECT SUM(price * quality) AS total_revenue\n" + "FROM order_details \n" + "WHERE order_id IN (\n"
			+ "  SELECT id FROM orders WHERE order_status = 'Đã Thanh Toán'\n" + ")", nativeQuery = true)
	Double Tongdoanhthu();

	@Query(value = "SELECT YEAR(o.created_at) AS year, MONTH(o.created_at) AS month, SUM(od.price * od.quality) AS total_revenue "
			+ "FROM order_details od " + "JOIN orders o ON od.order_id = o.id " + "WHERE o.order_status = :status "
			+ "GROUP BY YEAR(o.created_at), MONTH(o.created_at) " + "ORDER BY year, month", nativeQuery = true)
	List<Object[]> getRevenueByMonth(@Param("status") String status);

	@Query(value = "SELECT \n" + "    o.id AS order_id,\n" + "    o.nameReceiver,\n" + "    o.order_status,\n"
			+ "    od.book_id,\n" + "    b.book_name,\n" + "    od.quality,\n" + "    od.price,\n"
			+ "    SUM(od.price * od.quality) OVER (PARTITION BY o.id) AS total_order_value\n" + "FROM \n"
			+ "    book_store.orders o\n" + "JOIN \n" + "    book_store.order_details od ON o.id = od.order_id\n"
			+ "JOIN \n" + "    book_store.books b ON od.book_id = b.id\n" + "WHERE \n"
			+ "    o.id = ?; -- Thay thế ? bằng ID của đơn hàng bạn muốn lấy", nativeQuery = true)
	List<Object[]> getOrder_detail();

}
