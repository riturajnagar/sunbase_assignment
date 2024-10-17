package com.rj.sunbase.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {

	 private final UserDetailsService userDetailsService;
	    private final JwtRequestFilter jwtRequestFilter;

	    public SecurityConfig(UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
	        this.userDetailsService = userDetailsService;
	        this.jwtRequestFilter = jwtRequestFilter;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .authorizeRequests()
	                .requestMatchers("/api/auth", "/api/register", "/api/proxy-auth", "/api/sync").permitAll() // Allow unauthenticated access to login and register endpoints
	                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow unauthenticated access to Swagger UI
	                .anyRequest().authenticated() // All other endpoints require authentication
	            .and()
	            .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Stateless session management

	        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	        
	        return http.build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }
}

