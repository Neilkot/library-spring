package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql", }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    private static final int PAGE = 0;
    private static final int SIZE = 25;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldFindByNameAndAuthorAndPublisherAndPublishedYear() throws Exception {
        Book expected = bookRepository.findAll().iterator().next();
        Optional<Book> actual = bookRepository.findByNameAndAuthorAndPublisherAndPublishedYear(expected.getName(), expected.getAuthor(), expected.getPublisher(), expected.getPublishedYear());
        assertNotNull(actual.get());
        assertEquals(expected, actual.get());
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnAvailableBooks() throws Exception {
        Long allBooks = StreamSupport.stream(bookRepository.findAll().spliterator(), false).collect(Collectors.counting());
        long actual =  bookRepository.getAvailableBooks(PageRequest.of(PAGE, SIZE)).size();
        assertEquals(allBooks - 1, actual);
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCountAvailableBooks() throws Exception {
        long actual = bookRepository.countAvailableBooks();
        long expected =  bookRepository.getAvailableBooks(PageRequest.of(PAGE, SIZE)).size();
        assertEquals(expected, actual);
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnAvailableBooksForQuery() throws Exception {
        Book book = bookRepository.findAll().iterator().next();
        List<Book> availableBooks = bookRepository.getAvailableBooks(book.getName(), PageRequest.of(PAGE, SIZE));
        assertEquals(1, availableBooks.size());
        assertEquals(book, availableBooks.get(0));
    }


}
