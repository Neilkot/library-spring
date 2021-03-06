package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {

    Optional<Book> findByNameAndAuthorAndPublisherAndPublishedYear(String name, Author author,
                                                                   String publisher, Integer publishedYear);

    @Query(value = "SELECT DISTINCT b.name as name, b.id as id,  b.author_id as author_id, b.publisher as publisher, b.publish_year as publish_year,b.image_link as image_link FROM books b JOIN book_items ON b.id = book_items.book_id WHERE book_items.id NOT IN ( SELECT book_requests.book_item_id FROM book_requests JOIN book_requests_journals ON book_requests.id = book_requests_journals.book_request_id WHERE book_requests_journals.return_date IS NULL) ORDER BY b.id", nativeQuery = true)
    List<Book> getAvailableBooks(Pageable pageable);

    @Query(value = "SELECT count(DISTINCT b.name) FROM books b JOIN book_items ON b.id = book_items.book_id WHERE book_items.id NOT IN ( SELECT book_requests.book_item_id FROM book_requests JOIN book_requests_journals ON book_requests.id = book_requests_journals.book_request_id WHERE book_requests_journals.return_date IS NULL);", nativeQuery = true)
    long countAvailableBooks();

    @Query(value = "SELECT DISTINCT b.id as id, b.name as name, b.author_id as author_id, b.publisher as publisher, b.publish_year as publish_year,b.image_link as image_link FROM books b JOIN book_items ON b.id = book_items.book_id JOIN authors a ON b.author_id = a.id WHERE (b.name LIKE %?1% OR a.name LIKE %?1%) AND book_items.id NOT IN ( SELECT book_requests.book_item_id FROM book_requests JOIN book_requests_journals ON book_requests.id = book_requests_journals.book_request_id WHERE book_requests_journals.return_date IS NULL) ORDER BY b.id", nativeQuery = true)
    List<Book> getAvailableBooks(String query, Pageable pageable);
}
