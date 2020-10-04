package com.epam.lab.exam.library.dto;

import java.time.Instant;

import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.RequestType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingRequestDTO {

	private Integer requestId;
	private Integer bookId;
	private String username;
	private String bookName;
	private String authorName;
	private String publisher;
	private Integer publishedYear;
	private RequestType requestType;
	private Instant createDate;

	public static PendingRequestDTO from(BookRequestJournal request) {
		return PendingRequestDTO.builder().requestId(request.getBookRequest().getId())
				.bookId(request.getBookRequest().getBookItem().getBook().getId())
				.username(request.getBookRequest().getUser().getFirstName() + " "
						+ request.getBookRequest().getUser().getLastName())
				.bookName(request.getBookRequest().getBookItem().getBook().getName())
				.authorName(request.getBookRequest().getBookItem().getBook().getAuthor().getName())
				.requestType(request.getBookRequest().getBookRequestType().getType())
				.publisher(request.getBookRequest().getBookItem().getBook().getPublisher())
				.publishedYear(request.getBookRequest().getBookItem().getBook().getPublishedYear())
				.createDate(request.getCreateDate()).build();
	}

}
