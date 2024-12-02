package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.Save;

public interface SaveRepository extends JpaRepository<Save, Integer> {

	// Hiển thị Top 3 sách mới nhất
	@Query(value = "SELECT * FROM SAVES where book_id  = ? and customerId = ?;", nativeQuery = true)
	public Save selectSaves(Integer bookId, String customerId);
	
	// Hiển thị Top 3 sách mới nhất
	@Query(value = "SELECT * FROM SAVES where customerId = ?;", nativeQuery = true)
	public List<Save> selectAllSaves(String customerId);

	// Hiển thị Top 3 sách mới nhất
	@Query(value = "SELECT Count(id)  FROM book_store.saves  where  customerId = ?;", nativeQuery = true)
	public Integer selectCountSave(String customerId);

}
