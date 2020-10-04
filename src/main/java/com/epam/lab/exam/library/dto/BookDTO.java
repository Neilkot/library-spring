package com.epam.lab.exam.library.dto;

import java.io.Serializable;

import com.epam.lab.exam.library.model.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer bookId;
	private String bookName;
	private String authorName;
	private String publisher;
	private Integer publishedYear;
	private String imgLink;

	public static BookDTO from(Book book) {
		return BookDTO.builder().bookId(book.getId()).bookName(book.getName()).authorName(book.getAuthor().getName())
				.publisher(book.getPublisher()).publishedYear(book.getPublishedYear()).imgLink(book.getImgLink())
				.build();
	}

}
