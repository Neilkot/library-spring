package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.BookItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookItemRepositoryTest {
    private static final int PAGE = 0;
    private static final int SIZE = 25;
    @Autowired
    private BookItemRepository bookItemRepository;

    @Transactional
    @Test
    public void shouldDeleteNonRequestedBookItem() throws Exception {
        List<BookItem> allBooks = bookItemRepository.getBooks(PageRequest.of(PAGE, SIZE));
        BookItem bookItemToDelete = allBooks.get(0);
        bookItemRepository.deleteNonRequested(bookItemToDelete.getId());
        List<BookItem> afterDeletionBooks = bookItemRepository.getBooks(PageRequest.of(PAGE, SIZE));

        assertFalse(afterDeletionBooks.contains(bookItemToDelete));
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnAvailableBookId() throws Exception {
        List<BookItem> notAvailableByBookId = bookItemRepository.findAvailableByBookId(2);
        assertTrue(notAvailableByBookId.isEmpty());
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnBookItemsWhereBookRequestJournalsReturnDateIsNotNull() throws Exception {
        long expected =  bookItemRepository.findAll().size() - 1;
        List<BookItem> bookItems = bookItemRepository.getBooks(PageRequest.of(PAGE, SIZE));
        assertEquals(expected, bookItems.size());
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnNumberOfBoolItemsWhereBookRequestJournalsReturnDateIsNotNull() throws Exception {
        long actual = bookItemRepository.countBooks();
        long expected =  bookItemRepository.findAll().size() - 1;
        assertEquals(expected, actual);
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCountAvailableBooks() throws Exception {
        long actual = bookItemRepository.countBooks();
        long expected =  bookItemRepository.findAll().size() - 1;
        assertEquals(expected, actual);
    }




}
