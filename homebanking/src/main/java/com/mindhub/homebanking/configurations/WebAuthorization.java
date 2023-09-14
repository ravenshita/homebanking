package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
@Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/web/index.html").permitAll()
            .antMatchers(HttpMethod.POST,  "/api/login", "/api/logout", "/api/clients").permitAll()
            .antMatchers(HttpMethod.POST, "/api/clients/current/accounts","/api/clients/current/cards" ).hasAuthority("CLIENT")
            .antMatchers("/web/account.html").hasAuthority("CLIENT")
            .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
            .antMatchers("/web/cards.html").hasAuthority("CLIENT")
            .antMatchers("/web/create-cards.html").hasAuthority("CLIENT")
            .antMatchers("/web/deactivate-cards.html").hasAuthority("CLIENT")
            .antMatchers("/web/transfers.html").hasAuthority("CLIENT")
            .antMatchers("/web/loan-application.html").hasAuthority("CLIENT")
            .antMatchers("/api/clients/current").hasAuthority("CLIENT")
            .antMatchers("/api/clients/current/accounts").hasAuthority("CLIENT")
            .antMatchers("/api/clients/current/cards").hasAuthority("CLIENT")
            .antMatchers("/api/transactions").hasAuthority("CLIENT")
            .antMatchers("/api/clients").hasAuthority("ADMIN")
            .antMatchers("/api/clients/{id}").hasAuthority("ADMIN")
            .antMatchers("/admin/**").hasAuthority("ADMIN")
            .antMatchers("/rest/**").hasAuthority("ADMIN")
            .antMatchers("/h2-console/**").hasAuthority("ADMIN")
            .antMatchers("/manager.html").hasAuthority("ADMIN")
            .antMatchers("manager.js").hasAuthority("ADMIN");
    http.formLogin()
            .usernameParameter("email")
            .passwordParameter("password")
            .loginPage("/api/login");
    http.logout().logoutUrl("/api/logout");
    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
    http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}