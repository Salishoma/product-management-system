package com.oma.productmanagementsystem.security;

import com.oma.productmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserService usersService;
    private final Environment environment;
    private final JwtTokenVerifier jwtTokenVerifier;

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder,
                             UserService usersService,
                             Environment environment,
                             JwtTokenVerifier jwtTokenVerifier){
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.environment = environment;
        this.jwtTokenVerifier = jwtTokenVerifier;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/", "/api/home", "/v2/api-docs/**", "/swagger.json", "/swagger-ui/**", "/swagger-ui.html",
                        "/swagger-resources/**", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.POST,
                        environment.getProperty("login.url.path"), "/api/users/", "/api/users/authenticate").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/users/hello").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtTokenVerifier, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
