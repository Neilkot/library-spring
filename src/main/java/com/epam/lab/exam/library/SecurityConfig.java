package com.epam.lab.exam.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.epam.lab.exam.library.controller.ErrorHandler;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.CustomUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private ErrorHandler errorHandler;
	
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().userDetailsService(customUserDetailsService)//
				.authorizeRequests().antMatchers(HttpMethod.POST, "/**").permitAll().antMatchers("/css/**", "/js/**").permitAll()//
				.antMatchers("/reader-book", "/register", "/login", "/logout", "/error", "/book-query","/home", "/", "/#", "#").permitAll()//
				.antMatchers("/admin-book", "/admin-librarian", "/admin-reader", "/book-add", "/book-update",
						"/book-delete", "/librarian-create", "/librarian-delete", "/reader-status")
				.hasRole(RoleType.ADMIN.toString())
				.antMatchers("/pending-request", "/request-approve", "/request-cancel",
						"/return-book", "/reader-request")
				.hasRole(RoleType.LIBRARIAN.toString())//
				.antMatchers("/reader-request").hasRole(RoleType.READER.toString())//
				.antMatchers("/request-submit").hasRole("ACTIVE_USER").anyRequest().authenticated()//
				.and().exceptionHandling().authenticationEntryPoint(errorHandler.getForbiddenHandler())//
				.and().formLogin().loginPage("/login").successForwardUrl("/login").failureForwardUrl("/reader-book").permitAll()//
				.and().httpBasic()//
				.and().logout()//
				.logoutSuccessUrl("/reader-book").logoutUrl("/logout")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID");
	}
}
