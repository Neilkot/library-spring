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
public class BookRequestDTO {

	private Integer requestId;
	private Integer bookId;
	private String username;
	private String bookName;
	private String authorName;
	private String publisher;
	private Integer publishedYear;
	private RequestType requestType;
	private Instant createDate;
	private Instant approveDate;
	private Instant expirationDate;
	private Instant returnDate;
	private String fee;
	
	public static BookRequestDTO from(BookRequestJournal request) {
		return BookRequestDTO.builder().requestId(request.getBookRequest().getId())
				.bookId(request.getBookRequest().getBookItem().getBook().getId())
				.username(request.getBookRequest().getUser().getFirstName() + " "
						+ request.getBookRequest().getUser().getLastName())
				.bookName(request.getBookRequest().getBookItem().getBook().getName())
				.authorName(request.getBookRequest().getBookItem().getBook().getAuthor().getName())
				.publisher(request.getBookRequest().getBookItem().getBook().getPublisher())
				.publishedYear(request.getBookRequest().getBookItem().getBook().getPublishedYear())
				.requestType(request.getBookRequest().getBookRequestType().getType())
				.createDate(request.getCreateDate()).approveDate(request.getApproveDate())
				.expirationDate(request.getExpirationDate()).returnDate(request.getReturnDate()).build();
	}
}
