package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleService {

	private RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role getByType(RoleType type) throws ClientRequestException {
		return roleRepository.findByType(type).orElseThrow(() -> new ClientRequestException(ErrorType.ROLE_NOT_FOUND));
	}

}
