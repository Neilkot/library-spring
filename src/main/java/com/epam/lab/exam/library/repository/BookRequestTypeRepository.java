package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRequestTypeRepository extends JpaRepository<BookRequestType, Integer> {

	Optional<BookRequestType> findByType(RequestType type);
}
