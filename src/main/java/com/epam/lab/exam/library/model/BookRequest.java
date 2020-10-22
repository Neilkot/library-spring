package com.epam.lab.exam.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book_requests")
public class BookRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_item_id", nullable = false)
	private BookItem bookItem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_request_type_id", nullable = false)
	private BookRequestType bookRequestType;
}
