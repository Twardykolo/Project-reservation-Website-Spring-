package com.Adam.Lucja.JavaPRO.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/dt-app")
                .antMatchers("/dt-app/**")
                .antMatchers(
                        "/v3/api-docs/**",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/swagger-resources/configuration/ui").permitAll()
//                .antMatchers("/swagger.html").permitAll()
//                .antMatchers("/swagger-ui.html/**").permitAll()
                .antMatchers("/swagger**").permitAll()
                .antMatchers("/tematy/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
    }
}
