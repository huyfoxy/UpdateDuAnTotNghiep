package com.java.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	}
