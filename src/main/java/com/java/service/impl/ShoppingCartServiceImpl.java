package com.java.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.java.entity.Book;
import com.java.entity.CartItem;
import com.java.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private Map<Integer, CartItem> map = new HashMap<>();

    // Thêm sản phẩm vào giỏ hàng
    @Override
    public void add(CartItem item) {
        CartItem existedItem = map.get(item.getBookId());

        if (existedItem != null) {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
            existedItem.setTotalPrice(existedItem.getQuantity() * existedItem.getUnitPrice());
        } else {
            map.put(item.getBookId(), item);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng (Cả CartItem và số lượng)
    @Override
    public void update(CartItem item, int qty) {
        // Lấy sản phẩm trong giỏ hàng theo ID
        CartItem existingItem = map.get(item.getBookId());

        if (existingItem != null) {
            // Giới hạn số lượng theo số lượng tồn kho
            int availableStock = item.getBook().getQuality(); // Giả sử bạn có phương thức này để lấy số lượng tồn kho
            if (qty > availableStock) {
                // Nếu số lượng người dùng nhập vào lớn hơn số lượng tồn kho, gán số lượng tối đa là số lượng tồn kho
                qty = availableStock;
            }

            // Cập nhật lại số lượng sản phẩm
            existingItem.setQuantity(qty);

            // Tính lại tổng tiền cho sản phẩm
            existingItem.setTotalPrice(existingItem.getQuantity() * existingItem.getUnitPrice());
        }
    }

//    @Override
//    public void update(CartItem item, int qty) {
//        // Tìm sản phẩm trong giỏ hàng theo ID
//        CartItem existingItem = map.get(item.getBookId());
//        
//        if (existingItem != null) {
//            // Cập nhật lại số lượng sản phẩm
//            existingItem.setQuantity(qty);
//            // Tính lại tổng tiền cho sản phẩm
//            existingItem.setTotalPrice(existingItem.getQuantity() * existingItem.getUnitPrice());
//        }
//    }

    // Lấy thông tin sản phẩm trong giỏ hàng theo ID
    public CartItem getItemById(Integer bookId) {
        return map.get(bookId);
    }

    // Xóa sản phẩm khỏi giỏ hàng theo CartItem
    @Override
    public void remove(CartItem item) {
        map.remove(item.getBookId());
    }

    // Xóa sản phẩm khỏi giỏ hàng theo Book ID
    public void removeById(Integer bookId) {
        map.remove(bookId);
    }

    // Bổ sung phương thức bắt buộc từ interface ShoppingCartService
    @Override
    public void remove(Book book) {
        if (book != null) {
            map.remove(book.getId()); // Giả sử `Book` có phương thức `getId()` để lấy `bookId`
        }
    }

    // Trả về toàn bộ giỏ hàng dưới dạng Map
    public Map<Integer, CartItem> getItems() {
        return map;
    }

    // Lấy danh sách tất cả sản phẩm trong giỏ hàng
    @Override
    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    // Xóa toàn bộ giỏ hàng
    @Override
    public void clear() {
        map.clear();
    }

    // Tính tổng tiền giỏ hàng
    @Override
    public double getAmount() {
        return map.values().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
    }

    // Đếm số lượng sản phẩm trong giỏ hàng
    @Override
    public int getCount() {
        return map.size();
    }

    // Kiểm tra giỏ hàng có rỗng hay không
    public boolean isEmpty() {
        return map.isEmpty();
    }

    // Kiểm tra xem sản phẩm có tồn tại trong giỏ hàng hay không
    public boolean containsItem(Integer bookId) {
        return map.containsKey(bookId);
    }
}
