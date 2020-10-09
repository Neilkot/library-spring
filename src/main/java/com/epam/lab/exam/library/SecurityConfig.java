package com.epam.lab.exam.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.epam.lab.exam.library.controller.ErrorHandler;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.ChecksumService;
import com.epam.lab.exam.library.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private ChecksumService checksumService;

	@Autowired
	private ErrorHandler errorHandler;

	@Bean
	public PasswordEncoder encoder() {
		return checksumService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authenticationProvider(authProvider()).userDetailsService(userDetailsService)//
				.authorizeRequests().antMatchers(HttpMethod.POST, "/**").permitAll().antMatchers("/css/**", "/js/**")
				.permitAll()//
				.antMatchers("/reader-book", "/register", "/login", "/logout", "/error", "/book-query", "/home", "/",
						"/#", "#")
				.permitAll()//
				.antMatchers("/admin-book", "/admin-librarian", "/admin-reader", "/book-add", "/book-update",
						"/book-delete", "/librarian-create", "/librarian-delete", "/reader-status")
				.hasRole(RoleType.ADMIN.toString())
				.antMatchers("/pending-request", "/request-approve", "/request-cancel", "/return-book",
						"/reader-request")
				.hasRole(RoleType.LIBRARIAN.toString())//
				.antMatchers("/reader-request").hasRole(RoleType.READER.toString())//
				.antMatchers("/request-submit").hasRole("ACTIVE_USER").anyRequest().authenticated()//
				.and().exceptionHandling().authenticationEntryPoint(errorHandler.getForbiddenHandler())//
				.and().formLogin().loginPage("/login").successForwardUrl("/login").failureForwardUrl("/reader-book")
				.permitAll()//
				.and().httpBasic()//
				.and().logout()//
				.logoutUrl("/logout").logoutSuccessUrl("/reader-book").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID").and().sessionManagement().maximumSessions(1)
				.maxSessionsPreventsLogin(true);
		;
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
}
