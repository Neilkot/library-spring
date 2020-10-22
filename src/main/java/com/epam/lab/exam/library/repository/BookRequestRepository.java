package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.BookRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRequestRepository extends PagingAndSortingRepository<BookRequest, Integer> {

	List<BookRequest> findByUserId(Integer userId, Pageable pageable);


}
