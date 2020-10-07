package com.epam.lab.exam.library.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO {

	private Integer noOfPages;
	private Integer currPage;
	private Integer pageSize;
	
	public Integer getCurrPage() {
		return Optional.ofNullable(currPage).orElse(0);
	}
}
