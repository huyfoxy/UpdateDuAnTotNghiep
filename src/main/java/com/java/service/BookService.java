package com.java.service;

import java.util.List;
import com.java.entity.Book;

public interface BookService {
	Book saveBook(Book book);

	Book updateBook(Integer id, Book book) throws Exception;

	void deleteBook(Integer id);

	Book getBookById(Integer id);

	List<Book> getAllBooks();
}