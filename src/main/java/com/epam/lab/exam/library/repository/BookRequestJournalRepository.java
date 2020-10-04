package com.epam.lab.exam.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.epam.lab.exam.library.model.BookRequestJournal;

public interface BookRequestJournalRepository extends PagingAndSortingRepository<BookRequestJournal, Integer> {

	Optional<BookRequestJournal> findByBookRequestId(Integer bookRequestId);

	@Query(value = "SELECT * FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE brj.approve_date IS NULL ORDER BY br.id", nativeQuery = true)
	List<BookRequestJournal> getPendingNonBlockedReaderRequests(Pageable pageable);

	@Query(value = "SELECT * FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE brj.approve_date IS NOT NULL ORDER BY br.id", nativeQuery = true)
	List<BookRequestJournal> getApprovedNonBlockedReaderRequests(Pageable pageable);

	@Query(value = "SELECT * FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE u.id = ?1 AND brj.approve_date IS NOT NULL ORDER BY br.id", nativeQuery = true)
	List<BookRequestJournal> getUserApprovedRequests(Integer userId, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE u.id = ?1 AND brj.approve_date IS NOT NULL", nativeQuery = true)
	long countApprovedReaderRequests(Integer userId);

	@Query(value = "SELECT COUNT(*) FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE brj.approve_date IS NULL", nativeQuery = true)
	long countPending();
	
	@Query(value = "SELECT COUNT(*) FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id JOIN users u ON br.user_id = u.id JOIN books b ON br.book_item_id = b.id JOIN book_request_types brt ON br.book_request_type_id = brt.id WHERE brj.approve_date IS NOT NULL", nativeQuery = true)
	long countApproved();
	
}
