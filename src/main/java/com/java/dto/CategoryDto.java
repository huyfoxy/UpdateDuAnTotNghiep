package com.java.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable{
	
	private Integer id;
	@NotEmpty
	@Length(min = 3)
	private String categoryName;
	

}
