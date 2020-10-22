package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.repository.RoleRepository;
import com.epam.lab.exam.library.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    UserService userService;

    @Test (expected= ClientRequestException.class)
    public void shouldThrowExceptionWhenUserNameInUse() throws Exception {
        RegisterDTO dto = RegisterDTO.builder().username("Foo").build();
        when(userRepository.findByLogin(dto.getUsername())).thenReturn(Optional.of(new User()));
        when(roleRepository.findByType(RoleType.READER)).thenReturn(Optional.of(new Role()));
        userService.createReader(dto);
        verify(userRepository, times(1)).findByLogin(dto.getUsername());
    }
}
