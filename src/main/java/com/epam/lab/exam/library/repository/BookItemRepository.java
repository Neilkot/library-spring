package com.epam.lab.exam.library.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.epam.lab.exam.library.model.BookItem;

public interface BookItemRepository extends JpaRepository<BookItem, Integer> {

	@Modifying
	@Query(value = "DELETE FROM book_items bi WHERE bi.id = ?1 AND bi.id NOT IN (SELECT br.book_item_id FROM book_requests br JOIN book_requests_journals brj ON br.id = brj.book_request_id WHERE brj.return_date IS NOT NULL);", nativeQuery = true)
	int deleteNonRequested(Integer id);

	List<BookItem> findByBookId(Integer bookId);

	@Query(value = "SELECT * from book_items WHERE id NOT IN (SELECT br.book_item_id FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id WHERE return_date IS NULL)", nativeQuery = true)
	List<BookItem> getBooks(Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) from book_items WHERE id NOT IN (SELECT br.book_item_id FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id WHERE return_date IS NULL)", nativeQuery = true)
	long countBooks();
}
