package com.epam.lab.exam.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.lab.exam.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	
	Optional<Author> findByName(String name);
	

}
