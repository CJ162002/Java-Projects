package com.manager.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailServiceImpl(); 
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
        daoAuth.setUserDetailsService(this.getUserDetailsService());
        daoAuth.setPasswordEncoder(passwordEncoder());
        return daoAuth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/user/**").hasRole("USER")   // Protect paths requiring "USER" role
                .requestMatchers("/admin/**").hasRole("ADMIN") // Protect paths requiring "ADMIN" role
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                    .loginPage("/login") 
                    .usernameParameter("email") // Use "email" as the username field
                    .defaultSuccessUrl("/user/dashboard", true) // Redirect to this URL on successful login
                    .failureUrl("/login?error=true") // Redirect to this URL on login failure
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true") // Redirect to login page after successful logout
                    .invalidateHttpSession(true) // Invalidate session on logout
                    .clearAuthentication(true) // Clear authentication
                    .deleteCookies("JSESSIONID") // Delete session cookies if applicable
                    .permitAll()
                )
            .csrf(csrf -> csrf.disable()); 

        return http.build();
    }

}
