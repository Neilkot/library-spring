package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	
	Optional<Author> findByName(String name);

}
