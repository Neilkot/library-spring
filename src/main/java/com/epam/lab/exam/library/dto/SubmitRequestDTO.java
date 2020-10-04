package com.epam.lab.exam.library.dto;

import javax.validation.constraints.NotNull;

import com.epam.lab.exam.library.model.RequestType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitRequestDTO {
	@NotNull
	private Integer userId;
	@NotNull
	private Integer bookId;
	@NotNull
	private RequestType requestType;
}
