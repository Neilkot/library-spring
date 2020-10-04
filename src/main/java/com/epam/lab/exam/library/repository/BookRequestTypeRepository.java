package com.epam.lab.exam.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;

public interface BookRequestTypeRepository extends JpaRepository<BookRequestType, Integer> {

	Optional<BookRequestType> findByType(@Param("type") RequestType type);
}
