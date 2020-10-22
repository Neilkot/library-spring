package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.BookDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.service.BookService;
import com.epam.lab.exam.library.service.ConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerMockTests {

    @Mock
    private ConfigService configService;
    @Mock
    private BookService bookService;
    @Mock
    private Model model;
    @InjectMocks
    private BookController controller = new BookController();

    @Test
    public void shouldReturnBooksAttribute() {
        when(configService.getDefaultPageSize()).thenReturn(10);
        List<BookDTO> books = Arrays.asList(BookDTO.builder().build());
        when(bookService.getAvailableBooks(eq(0), eq(10))).thenReturn(books);

        ModelAndView modelAndView = controller.getBooksGet(model, new UserSessionDTO());
        List<BookDTO> actual = (List<BookDTO>) modelAndView.getModel().get("books");
        verify(bookService, times(1)).getAvailableBooks(eq(0), eq(10));
        assertEquals(books, actual);
    }

}
