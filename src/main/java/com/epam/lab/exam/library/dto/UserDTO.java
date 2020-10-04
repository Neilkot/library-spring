package com.epam.lab.exam.library.dto;

import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	private Integer id;
	private String login;
	private String firstName;
	private String lastName;
	private Boolean isBlocked;
	private RoleType roleType;

	public static UserDTO from(User user) {
		return UserDTO.builder().id(user.getId()).login(user.getLogin()).firstName(user.getFirstName())
				.lastName(user.getLastName()).isBlocked(user.getIsBlocked()).roleType(user.getRole().getType()).build();
	}

	public String getUsername() {
		return firstName.concat(" ").concat(lastName);
	}
}
