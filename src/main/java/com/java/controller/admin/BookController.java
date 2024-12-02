package com.java.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.entity.Book;
import com.java.service.BookService;

@Controller
@RequestMapping("/admin")
public class BookController {

    @Autowired
    private BookService bookService;

//    @PostMapping("/doUpdateBook/{id}")
//    public String updateBook(@PathVariable Integer id, @ModelAttribute("book") Book book) {
//        bookService.updateBook(id, book);
//        return "redirect:/admin/books"; // Redirect đến trang danh sách sách sau khi cập nhật
//    }

//    @GetMapping("/admin/books")
//    public String getAllBooks(Model model) {
//        List<Book> books = bookService.getAllBooks();
//        model.addAttribute("listBooks", books);
//        return "admin/bookList"; // Trả về view danh sách sách
//    }

    @GetMapping("/books/{id}")
    public String getBookById(@PathVariable Integer id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "bookForm"; // Trả về view form để chỉnh sửa sách
    }
}