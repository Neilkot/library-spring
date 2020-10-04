package com.epam.lab.exam.library.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book_requests_journals", uniqueConstraints = { @UniqueConstraint(columnNames = { "book_request_id" }) })
public class BookRequestJournal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_request_id", nullable = false)
	private BookRequest bookRequest;

	@Column(name = "create_date")
	private Instant createDate;

	@Column(name = "approve_date")
	private Instant approveDate;

	@Column(name = "expiration_date")
	private Instant expirationDate;

	@Column(name = "return_date")
	private Instant returnDate;
}
