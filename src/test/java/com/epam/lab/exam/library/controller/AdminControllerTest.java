package com.epam.lab.exam.library.controller;

import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.UserDTO;
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
@WithUserDetails("admin")
public class AdminControllerTest {
    @Autowired
    private AdminController controller;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldLoadMainPageGet() throws Exception {
        this.mockMvc.perform(get("/admin-librarian"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-librarian.jsp"));
    }

    @Test
    public void shouldLoadMainPagePost() throws Exception {
        this.mockMvc.perform(post("/admin-librarian"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-librarian.jsp"));
    }

    @Test
    public void shouldRedirectToLibrarianPageWhenPostDeleting() throws Exception {
        this.mockMvc.perform(post("/librarian-delete").flashAttr("delete-librarian", IdentityDTO.builder().id(2).build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-librarian"));
    }


    @Test
    public void shouldRedirectToLibrarianPageWhenPostCreating() throws Exception {
        this.mockMvc.perform(post("/librarian-create").flashAttr("librarian", RegisterDTO.builder().firstName("Foo")
                .lastName("Foo").username("Bar").password("EEE").build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-librarian"));
    }

    @Test
    public void shouldRedirectToReadersPageGet() throws Exception {
        this.mockMvc.perform(get("/admin-reader"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-reader.jsp"));
    }

    @Test
    public void shouldRedirectToReadersPagePost() throws Exception {
        this.mockMvc.perform(post("/admin-reader"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin-reader.jsp"));
    }

    @Test
    public void shouldRedirectToReadersPageWhenPostReaderStatus() throws Exception {
        this.mockMvc.perform(post("/reader-status").flashAttr("isBlocked", UserDTO.builder().id(2).isBlocked(false).build()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-reader"));
    }


}
