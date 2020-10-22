package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.service.BookRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("jane")
public class LibrarianControllerTest {
    @Autowired
    LibrarianController librarianController;
    @MockBean
    BookRequestService bookRequestService;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldRedirectToPendingRequestWhenAllPendingRequestGet() throws Exception {
        this.mockMvc.perform(get("/pending-request").sessionAttr("userSession", UserSessionDTO.builder().build()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/pending-request.jsp"));
    }

    @Test
    public void shouldRedirectToPendingRequestWhenAllPendingRequestPost() throws Exception {
        this.mockMvc.perform(post("/pending-request").sessionAttr("userSession", UserSessionDTO.builder().build()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/pending-request.jsp"));
    }


    @Test
    public void shouldRedirectToReaderRequestWhenAllPendingRequestPost() throws Exception {
        this.mockMvc.perform(post("/approved-request").sessionAttr("userSession", UserSessionDTO.builder().build()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/reader-request.jsp"));
    }

    @Test
    public void shouldRedirectToReaderRequestWhenAllPendingRequestGet() throws Exception {
        this.mockMvc.perform(get("/approved-request").sessionAttr("userSession", UserSessionDTO.builder().build()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/reader-request.jsp"));
    }

    @Test
    public void shouldRedirectToPendingRequestWhenCancelRequest() throws Exception {
        this.mockMvc.perform(post("/request-cancel").sessionAttr("userSession", UserSessionDTO.builder().build())
                .flashAttr("request", IdentityDTO.builder().id(2).build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pending-request"));
    }

    @Test
    public void shouldRedirectToPendingRequestWhenReturnBook() throws Exception {
        this.mockMvc.perform(post("/return-book").sessionAttr("userSession", UserSessionDTO.builder().build())
                .flashAttr("request", IdentityDTO.builder().id(2).build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/approved-request"));
    }
}
