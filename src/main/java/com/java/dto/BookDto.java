package com.java.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private Integer id;
	@NotEmpty
	@Column(name = "book_name")
	private String bookName;
//	@NotEmpty
	private String description;
//	@NotEmpty
	@Column(name = "publish_date")
	private Date publishDate;
	private Integer suggest;
//	@NotEmpty
	@Column(name = "publishing_house")
	private String publishingHouse;
	private String translator;
	@Column(name = "number_of_pages")
	private Integer numberOfPages;
	@NotNull
	@Min(value = 1)
	private Integer quality;
	@NotNull
	@Min(value = 1)
	private Integer price;
	@Column(name = "cover_price")
	private Integer coverPrice;
	@Column(name = "book_image")
	public String bookImage;
	private String images;
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "updated_at")
	private Date updatedAt;
	private Integer categoryId;
	private boolean isEdit = false;
}
