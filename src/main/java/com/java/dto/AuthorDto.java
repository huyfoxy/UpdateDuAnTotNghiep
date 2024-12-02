package com.java.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto implements Serializable {

	private Integer id;
	@NotEmpty(message = "Tên không được để trống!")
	@Column(name = "author_name")
	private String authorName;
	@NotEmpty(message = "Thông tin tác giả không được để trống!")
	@Column(name = "author_info")
	private String authorInfo;
	@Column(name = "author_image")
	private String authorImage;
	
	private boolean isEdit = false;

}
