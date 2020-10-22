package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test (expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenRoleNotFound() throws Exception {
        roleService.getByType(RoleType.LIBRARIAN);
    }
}
