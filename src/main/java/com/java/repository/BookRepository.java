package com.java.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	// Hiển thị Top 10 sách bán chạy nhất
	@Query(value = "SELECT p.book_id,\r\n" + "  COUNT(*) AS SoLuong\r\n" + "FROM order_details p\r\n"
			+ "JOIN books c ON p.book_id = c.id\r\n" + "GROUP BY p.book_id \r\n"
			+ "ORDER by SoLuong DESC limit 10;", nativeQuery = true)
	public List<Object[]> listBook10();

	// Hiển thị Top 3 sách mới nhất
	@Query(value = "SELECT *FROM BOOKS  ORDER BY id DESC LIMIT 3;", nativeQuery = true)
	public List<Book> listNewBook3();

	@Query(value = "select * from books o where id in :ids", nativeQuery = true)
	List<Book> findByInventoryIds(@Param("ids") List<Integer> listBookId);

	// Hiển thị mỗi loại sách có bao nhiêu cuốn
	@Query(value = "SELECT c.id,c.category_name,\r\n" + "COUNT(*) AS SoLuong\r\n" + "FROM books b\r\n"
			+ "JOIN categories c ON b.category_id = c.id\r\n" + "GROUP BY c.category_name;", nativeQuery = true)
	List<Object[]> listCategoryByBookName();

	// Hiển thị mỗi tác giả có bao nhiêu tác phẩm
	@Query(value = "SELECT a.id,a.author_name,\r\n" + "COUNT(*) AS SoLuong\r\n" + "FROM books b\r\n"
			+ "JOIN authors a ON b.author_id = a.id\r\n" + "GROUP BY a.author_name", nativeQuery = true)
	List<Object[]> listAuthorByBookName();

	// Hiển thị mỗi NXB có bao nhiêu tác phẩm
	@Query(value = "SELECT c.id, c.company_name,\r\n" + "COUNT(*) AS SoLuong\r\n" + "FROM books b\r\n"
			+ "JOIN companies c ON b.company_id = c.id\r\n" + "GROUP BY c.company_name", nativeQuery = true)
	List<Object[]> listNXBByBookName();

	// Top 10 bookName by category
	@Query(value = "SELECT \r\n" + "*FROM books AS b\r\n" + "WHERE b.category_id = ? limit 10;", nativeQuery = true)
	List<Book> listBookByCategory10(Integer categoryId);

	// Search Product
	@Query(value = "SELECT * FROM books WHERE book_name LIKE %?1%", nativeQuery = true)
	public List<Book> searchBook(String bookName);

	// Sách theo danh mục
	@Query(value = "SELECT * FROM books where category_id = ?", nativeQuery = true)
	public List<Book> listBookByCategory(Integer categoryId);

	// Sách theo tác giả
	@Query(value = "SELECT * FROM books where author_id = ?", nativeQuery = true)
	public List<Book> listBookByAuthor(Integer authorId);

	// Sách theo NXB
	@Query(value = "SELECT * FROM books where company_id = ?", nativeQuery = true)
	public List<Book> listBookByPublishingHouse(Integer companyId);

	// Hiển thị danh sách thống kê 5 sản phẩm bán chạy nhất
	@Query(value = "SELECT b.id AS book_id, b.book_name, b.price, b.book_image, SUM(od.quality) AS total_quantity "
			+ "FROM order_details od " + "JOIN books b ON od.book_id = b.id "
			+ "GROUP BY b.id, b.book_name, b.price, b.book_image " + "ORDER BY total_quantity DESC", nativeQuery = true)
	public List<Object[]> lisTop10(Pageable pageable);
	//Hiển thị tổng số lượng sản phẩm 
	@Query(value = "SELECT SUM(quality) AS total_quantity FROM books", nativeQuery = true)
	Integer SumQuantityBook();


}