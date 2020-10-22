package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.BookItemDTO;
import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.repository.AuthorRepository;
import com.epam.lab.exam.library.repository.BookItemRepository;
import com.epam.lab.exam.library.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookItemRepository bookItemRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    public void whenCreateBookShouldSafeAuthor() throws Exception {
        BookItemDTO dto = BookItemDTO.builder().authorName("Luter").bookName("BookName").build();
        Author author = Author.builder().name(dto.getAuthorName()).build();
        Book book = Book.builder().name(dto.getBookName()).author(author).build();
        BookItem bookItem = BookItem.builder().book(book).build();
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(book)).thenReturn(book);

        bookService.createBook(dto);
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).save(book);
        verify(bookItemRepository, times(1)).save(bookItem);
    }

    @Test
    public void whenCreateBookWithExistingAuthorShouldNotCreateAuthor() throws Exception {
        BookItemDTO dto = BookItemDTO.builder().authorName("Luter").bookName("BookName").build();
        Author author = Author.builder().name(dto.getAuthorName()).build();
        Book book = Book.builder().name(dto.getBookName()).author(author).build();
        BookItem bookItem = BookItem.builder().book(book).build();
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        bookService.createBook(dto);
        verify(authorRepository, never()).save(author);
        verify(authorRepository, times(1)).findByName(author.getName());
        verify(bookRepository, times(1)).save(book);
        verify(bookItemRepository, times(1)).save(bookItem);
    }

    @Test(expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenBookNotFound() throws Exception {
        bookService.updateBook(new BookItemDTO());
    }

    @Test
    public void shouldSaveAuthorWhenAuthorNotFound() throws Exception {
        BookItemDTO bookDto = BookItemDTO.builder().authorName("Martin").bookName("FooBar").build();
        Author author = Author.builder().name(bookDto.getAuthorName()).build();
        when(bookItemRepository.findById(bookDto.getBookItemId())).thenReturn(Optional.of(new BookItem()));
        bookService.updateBook(bookDto);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    public void shouldSaveBookWhenNotFound() throws Exception {
        BookItemDTO bookDto = BookItemDTO.builder().authorName("Martin").bookName("FooBar").build();
        Author author = Author.builder().name(bookDto.getAuthorName()).build();
        Book book = Book.builder().name(bookDto.getBookName()).author(author).build();

        when(bookItemRepository.findById(bookDto.getBookItemId())).thenReturn(Optional.of(new BookItem()));
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        bookService.updateBook(bookDto);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void shouldDeleteBookWhenBookItemNonRequested() throws Exception {
        IdentityDTO dto = new IdentityDTO();
        Book book = Book.builder().id(1).build();
        BookItem bookItem = new BookItem();
        bookItem.setId(11);
        bookItem.setBook(book);
        when(bookItemRepository.findById(dto.getId())).thenReturn(Optional.of(bookItem));
        when(bookItemRepository.findAvailableByBookId(bookItem.getBook().getId())).thenReturn(new ArrayList<>());
        bookService.deleteBook(dto);
        verify(bookItemRepository, times(1)).deleteNonRequested(bookItem.getId());
        verify(bookRepository, times(1)).deleteById(bookItem.getBook().getId());
    }

    @Test
    public void shouldNotDeleteBookWhenBookItem() throws Exception {
        IdentityDTO dto = new IdentityDTO();
        Book book = Book.builder().id(1).build();
        BookItem bookItem = new BookItem();
        bookItem.setId(11);
        bookItem.setBook(book);
        when(bookItemRepository.findById(dto.getId())).thenReturn(Optional.of(bookItem));
        when(bookItemRepository.findAvailableByBookId(bookItem.getBook().getId())).thenReturn(new ArrayList<>());
        bookService.deleteBook(dto);
        verify(bookItemRepository, times(1)).deleteNonRequested(bookItem.getId());
        verify(bookRepository, times(1)).deleteById(bookItem.getBook().getId());
    }

}
