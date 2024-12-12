package com.java.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.java.entity.Book;
import com.java.repository.BookRepository;
import com.java.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	    @Autowired
	    private BookRepository bookRepository;

	    @Override
	    public Book saveBook(Book book) {
	        // Thiết lập thời gian tạo sách nếu cần
	        book.setCreatedAt(new Date());
	        return bookRepository.save(book);
	    }


	    @Override
	    public Book updateBook(Integer id, Book book) throws Exception {
	        // Kiểm tra xem sách có tồn tại không
	        Optional<Book> existingBookOptional = bookRepository.findById(id);
	        if (!existingBookOptional.isPresent()) {
	            throw new Exception("Không tìm thấy sách với ID: " + id);
	        }

	        // Lấy thực thể sách hiện tại
	        Book existingBook = existingBookOptional.get();

	        // Cập nhật thông tin sách
	        existingBook.setBookName(book.getBookName());
	        existingBook.setDescription(book.getDescription());
	        existingBook.setPublishDate(book.getPublishDate());
	        existingBook.setSuggest(book.getSuggest());
	        existingBook.setPublishingHouse(book.getPublishingHouse());
	        existingBook.setTranslator(book.getTranslator());
	        existingBook.setNumberOfPages(book.getNumberOfPages());
	        existingBook.setQuality(book.getQuality());
	        existingBook.setPrice(book.getPrice());
	        existingBook.setCoverPrice(book.getCoverPrice());
	        existingBook.setBookImage(book.getBookImage());
	        existingBook.setImages(book.getImages());
	        existingBook.setCreatedAt(book.getCreatedAt());
	        existingBook.setUpdatedAt(new Date()); // Cập nhật thời gian sửa đổi
	        existingBook.setCategory(book.getCategory()); // Cập nhật thể loại nếu cần
	        existingBook.setAuthor(book.getAuthor()); // Cập nhật tác giả nếu cần
	        existingBook.setCompanie(book.getCompanie()); // Cập nhật công ty nếu cần

	        // Lưu sách đã cập nhật
	        return bookRepository.save(existingBook);
	    }

	    @Override
	    public void deleteBook(Integer id) {
	        bookRepository.deleteById(id);
	    }

	    @Override
	    public Book getBookById(Integer id) {
	        return bookRepository.findById(id).orElse(null); // Hoặc ném một ngoại lệ nếu không tìm thấy sách
	    }

	    @Override
	    public List<Book> getAllBooks() {
	        return bookRepository.findAll();
	    }
	    @Override
	    public List<Book> getTop10SanPhamBanChay() {
	        // Gọi phương thức listTop10 từ repository và truyền Pageable
	        List<Object[]> results = bookRepository.lisTop10(PageRequest.of(0, 10));

	        // Xử lý kết quả trả về từ repository
	        return results.stream()
	                      .map(result -> {
	                          Integer bookId = (Integer) result[0];  // Lấy book_id
	                          // Tìm Book theo bookId
	                          return bookRepository.findById(bookId)
	                                               .orElse(null);  // Trả về Book nếu tìm thấy, nếu không thì trả null
	                      })
	                      .filter(Objects::nonNull)  // Lọc bỏ các kết quả null (nếu có)
	                      .collect(Collectors.toList());
	    }
	    
	    @Override
	    public Integer getsumqualityBook() {
	        // Lấy tổng từ repository
	        Integer totalOrders = bookRepository.SumQuantityBook();
	        // Xử lý logic (nếu cần)
	        return totalOrders != null ? totalOrders : 0; // Đảm bảo không trả về null
	    }

	}
