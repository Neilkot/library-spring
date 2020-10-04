package com.epam.lab.exam.library.dto;

import com.epam.lab.exam.library.model.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionDTO {

	private Integer id;
	private String login;
	private String firstName;
	private String lastName;
	private Boolean isBlocked;
	private RoleType roleType;
	private String location;

	public UserSessionDTO enrich(UserDTO user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.isBlocked = user.getIsBlocked();
		this.roleType = user.getRoleType();
		return this;
	}

	public String getUsername() {
		return firstName.concat(" ").concat(lastName);
	}
}
