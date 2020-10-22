package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.service.BookService;
import com.epam.lab.exam.library.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class BookControllerTest {

    @MockBean
    private ConfigService configService;
    @MockBean
    private BookService bookService;
    @MockBean
    private Model model;
    @MockBean
    MockHttpSession session;
    @InjectMocks
    private BookController controller = new BookController();

    @Autowired
    private MockMvc mockMvc;

    private  HashMap<String, Object> attributes;

    @Before
            public void beforeEach() {
        attributes = new HashMap<String, Object>();
        attributes.put("userSession", UserSessionDTO.builder().build());
    }


    @Test
    public void shouldLoadMainPageGet() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/reader-book.jsp"));
    }

    @Test
    public void shouldLoadMainPagePost() throws Exception {
        this.mockMvc.perform(post("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/reader-book.jsp"));
    }

    @Test
    public void shouldLoadMainPageWithBookQuery() throws Exception {
        this.mockMvc.perform(post("/book-query"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/reader-book.jsp"));
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//
    @WithUserDetails("admin")
    public void shouldLoadAdminPageWhenAdminLoggedInGet() throws Exception {
        this.mockMvc.perform(get("/admin-book").sessionAttrs(attributes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-book.jsp"));
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//
    @WithUserDetails("admin")
    public void shouldLoadAdminPageWhenAdminLoggedInPost() throws Exception {
        this.mockMvc.perform(get("/admin-book").sessionAttrs(attributes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-book.jsp"));
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//
    @WithUserDetails("admin")
    public void shouldLoadAdminAddBookPage() throws Exception {
        this.mockMvc.perform(post("/book-add").sessionAttrs(attributes))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-book"));
    }

   @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//
    @WithUserDetails("admin")
    public void shouldLoadAdminUpdateBookPage() throws Exception {
        this.mockMvc.perform(post("/book-update").sessionAttrs(attributes))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-book"));
    }

    @Test
    @Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//
    @WithUserDetails("admin")
    public void shouldLoadAdminDeleteBookPage() throws Exception {
        this.mockMvc.perform(post("/book-delete").sessionAttrs(attributes))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-book"));
    }








}
