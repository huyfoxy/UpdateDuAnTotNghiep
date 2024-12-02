//package com.java.controller.admin;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.java.entity.Author;
//import com.java.repository.AuthorRepositoy;
//
//@Controller
//public class AuthorController {
//
//	@Value("./")
//	private String pathUploadImage;
//
//	// show author
//	@GetMapping(value = "/admin/authorList")
//	public String authorList(Model model) {
//
//		List<Author> listAuthors = authorRepositoy.findAll();
//		model.addAttribute("listAuthors", listAuthors);
//		model.addAttribute("author", new Author());
//
//		return "admin/authorList";
//	}
//
//	@Autowired
//	AuthorRepositoy authorRepositoy;
//
//	// Edit author
//	@GetMapping(value = "/admin/editAuthor")
//	public String editAuthor(@RequestParam("id") int id, Model model, @ModelAttribute("author") Author author) {
//		author = authorRepositoy.finAuthor(id);
//		model.addAttribute("author", author);
//		return "admin/editAuthor";
//	}
//
//	// Edit author
//	@PostMapping(value = "/admin/doEditAuthor/{id}")
//	public String editAuthor(@PathVariable("id") Integer id, @ModelAttribute("author") Author author, Model model) {
//
//		author.setId(id);
//		authorRepositoy.save(author);
//		return "redirect:/admin/authorList";
//	}
//
//	// add author
//	@PostMapping(value = "/addAuthor")
//	public String addAuthor(Model model, @Valid @ModelAttribute("author") Author author, BindingResult result,
//			@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
//		try {
//			File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
//			FileOutputStream fos = new FileOutputStream(convFile);
//			fos.write(file.getBytes());
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		author.setAuthorImage(file.getOriginalFilename());
//		Author b = authorRepositoy.save(author);
//		if (null != b) {
//			model.addAttribute("message", "Thêm mới thành công!");
//			model.addAttribute("book", author);
//		} else {
//			model.addAttribute("message", "Thêm mới thất bại!");
//			model.addAttribute("book", author);
//		}
//		model.addAttribute("message", "Thêm mới thành công!");
//
//		return "redirect:/admin/authorList";
//	}
//
//	// delete author
//	@GetMapping("/deleteAuthor/{id}")
//	public String deleteAuthor(@PathVariable("id") Integer id, Model model) {
//
//		authorRepositoy.deleteById(id);
//		return "redirect:/admin/authorList";
//	}
//}
