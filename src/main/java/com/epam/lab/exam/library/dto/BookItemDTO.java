package com.epam.lab.exam.library.dto;

import java.io.Serializable;

import com.epam.lab.exam.library.model.BookItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer bookItemId;
	private Integer bookId;
	private String bookName;
	private String authorName;
	private String publisher;
	private Integer publishedYear;
	private String imgLink;

	public static BookItemDTO from(BookItem book) {
		return BookItemDTO.builder().bookId(book.getBook().getId()).bookItemId(book.getId())
				.bookName(book.getBook().getName()).authorName(book.getBook().getAuthor().getName())
				.publisher(book.getBook().getPublisher()).publishedYear(book.getBook().getPublishedYear())
				.imgLink(book.getBook().getImgLink()).build();
	}

}
