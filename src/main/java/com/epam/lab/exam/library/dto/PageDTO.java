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
	private Integer page;

	public Integer getPage() {
		return Optional.ofNullable(page).orElse(1);
	}
}
