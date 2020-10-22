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
@Table(name = "book_request_types", uniqueConstraints = { @UniqueConstraint(columnNames = { "type" }) })
public class BookRequestType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private RequestType type;

}
