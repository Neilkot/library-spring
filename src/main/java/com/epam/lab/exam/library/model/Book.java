package com.epam.lab.exam.library.model;

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
@Table(name = "books", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name", "author_id", "publisher", "publish_year" }) })
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", nullable = false)
	private Author author;
	
	@Column(nullable = false)
	private String publisher;
	
	@Column(name = "publish_year", nullable = false)
	private Integer publishedYear;
	
	@Column(name = "image_link", nullable = false)
	private String imgLink;

}
