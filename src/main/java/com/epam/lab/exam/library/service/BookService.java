package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.BookDTO;
import com.epam.lab.exam.library.dto.BookItemDTO;
import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.QueryDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.repository.AuthorRepository;
import com.epam.lab.exam.library.repository.BookItemRepository;
import com.epam.lab.exam.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    private AuthorRepository authorRepository;

    private BookRepository bookRepository;
    private BookItemRepository bookItemRepository;

    public BookService(BookRepository bookRepository, BookItemRepository bookItemRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookItemRepository = bookItemRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookDTO> getAvailableBooks(int page, int size) {
        return bookRepository.getAvailableBooks(PageRequest.of(page, size)).stream().map(BookDTO::from)
                .collect(Collectors.toList());
    }

    public long countAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    public List<BookDTO> getAvailableBooks(QueryDTO dto, int page, int size) {
        return bookRepository.getAvailableBooks(dto.getQuery(), PageRequest.of(page, size)).stream().map(BookDTO::from)
                .collect(Collectors.toList());
    }

    public List<BookItemDTO> getBooks(int page, int size) {
        return bookItemRepository.getBooks(PageRequest.of(page, size)).stream().map(BookItemDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBook(BookItemDTO bookDto) {
        Author author = authorRepository.findByName(bookDto.getAuthorName())
                .orElseGet(() -> authorRepository.save(Author.builder().name(bookDto.getAuthorName()).build()));

        Book book = bookRepository
                .findByNameAndAuthorAndPublisherAndPublishedYear(bookDto.getBookName(), author, bookDto.getPublisher(),
                        bookDto.getPublishedYear())
                .orElseGet(() -> bookRepository
                        .save(Book.builder().name(bookDto.getBookName()).author(author).imgLink(bookDto.getImgLink())
                                .publishedYear(bookDto.getPublishedYear()).publisher(bookDto.getPublisher()).build()));

        bookItemRepository.save(BookItem.builder().book(book).build());
    }

    @Transactional
    public void updateBook(BookItemDTO bookDto) throws ClientRequestException {

        BookItem bookItem = bookItemRepository.findById(bookDto.getBookItemId())
                .orElseThrow(() -> ErrorType.BOOK_NOT_FOUND.ex());

        Author author = authorRepository.findByName(bookDto.getAuthorName())
                .orElseGet(() -> authorRepository.save(Author.builder().name(bookDto.getAuthorName()).build()));

        Book book = bookRepository
                .findByNameAndAuthorAndPublisherAndPublishedYear(bookDto.getBookName(), author, bookDto.getPublisher(),
                        bookDto.getPublishedYear())
                .orElseGet(() -> bookRepository
                        .save(Book.builder().name(bookDto.getBookName()).author(author).imgLink(bookDto.getImgLink())
                                .publishedYear(bookDto.getPublishedYear()).publisher(bookDto.getPublisher()).build()));

        bookItem.setBook(book);
        bookItemRepository.save(bookItem);
    }

//TODO: deleteNonRequestedQuery , logic of last 2 lines. Need time to think
    @Transactional
    public void deleteBook(IdentityDTO dto) {
        bookItemRepository.findById(dto.getId()).ifPresent(bi -> {
            bookItemRepository.deleteNonRequested(bi.getId());
            if (bookItemRepository.findAvailableByBookId(bi.getBook().getId()).isEmpty()) {
                bookRepository.deleteById(bi.getBook().getId());
            }
        });
    }

    public long countBooks() {
        return bookItemRepository.countBooks();
    }
}
