package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.model.RequestType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRequestControllerTest {
    @Autowired
    private BookRequestController bookRequestController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRedirectToRequestPageWhenGet() throws Exception {
        this.mockMvc.perform(get("/reader-request"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("reader-book"));
    }

    @Test
    public void shouldRedirectToRequestPageWhenPost() throws Exception {
        this.mockMvc.perform(post("/reader-request"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("reader-book"));
    }


    @Test
    @WithUserDetails("user")
    public void shouldRedirectToReaderBookPageWhenPostSubmitRequest() throws Exception {
        this.mockMvc.perform(post("/request-submit").flashAttr("request", SubmitRequestDTO.builder().bookId(1).requestType(RequestType.ABONEMENT)
        .userId(1).build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reader-book"));
    }


}
