package com.epam.lab.exam.library.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.model.BookRequest;

public interface BookRequestRepository extends PagingAndSortingRepository<BookRequest, Integer> {

	List<BookRequest> findByUserId(@Param("user_id") Integer userId, Pageable pageable);


}
