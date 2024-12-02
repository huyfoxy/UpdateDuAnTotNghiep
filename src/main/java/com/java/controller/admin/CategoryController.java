/**
 * 
 */
package com.java.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.dto.CategoryDto;
import com.java.entity.Category;
import com.java.repository.CategoryRepository;

/**
 * @author TuNV15
 *
 */
@Controller
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;

	// show category
	@GetMapping(value = "/admin/categories")
	public String categoryList(Model model) {
		List<Category> listCategories = categoryRepository.findAll();
		//System.out.println("tên"+listCategories);
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		return "admin/categoryList";
	}

	// add category
	@PostMapping(value = "/addCategory")
	public String addCategory(Model model, @Valid @ModelAttribute("category") CategoryDto dto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Lỗi");
			return "admin/categoryList";
		} else {

			Category category = new Category();
			BeanUtils.copyProperties(dto, category);
			categoryRepository.save(category);
			model.addAttribute("message", "Thêm mới thành công !");
		}
		return "redirect:/admin/categories";
	}

	// Edit author
	@GetMapping(value = "/admin/updateCategory")
	public String editAuthor(@RequestParam("id") int id, Model model, @ModelAttribute("category") Category category) {
		Optional<Category> categoryDto = categoryRepository.findById(id);

		Category dspCategory = new Category();
		BeanUtils.copyProperties(categoryDto.get(), dspCategory);
		model.addAttribute("category", dspCategory);
		return "admin/editCategory";
	}

	// update category
	@PostMapping(value = "/admin/doUpdateCategory/{id}")
	public String updateCategory(@PathVariable("id") Integer id, @ModelAttribute("category") Category category,
			Model model, RedirectAttributes rs) {
		category.setId(id);
		Category cs = categoryRepository.save(category);
		if (cs != null) {
			model.addAttribute("message", "Update success");
			model.addAttribute("category", categoryRepository.findById(cs.getId()));
		} else {
			model.addAttribute("message", "Update failure");
			model.addAttribute("category", category);
		}

		return "redirect:/admin/categories";
	}

	// delete category
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, Model model) {

		categoryRepository.deleteById(id);
		return "redirect:/admin/categories";
	}

}
