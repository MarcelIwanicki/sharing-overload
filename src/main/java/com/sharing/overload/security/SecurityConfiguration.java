package com.sharing.overload.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private AppUrlAuthenticationSuccessHandler appAuthenticationHandler;

    @Autowired
    private AppLogoutSuccessHandler appLogoutHandler;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/rest/users").permitAll()
        .and()
        .authorizeRequests()
                .antMatchers("/").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login")
        .and()
                .authorizeRequests()
                .antMatchers("/profile/*").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login")
        .and()
                .authorizeRequests()
                .antMatchers("/friends/*").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login")
        .and()
                .authorizeRequests()
                .antMatchers("/post/*").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login")
        .and()
                .authorizeRequests()
                .antMatchers("/chat/*", "/chat").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login")
        .and()
                .authorizeRequests()
                .antMatchers("/search/*", "/seach").hasAnyAuthority("REGULAR_USER", "ADMIN").and().formLogin().loginPage("/login");

        http.formLogin().successHandler(appAuthenticationHandler);
        http.logout().logoutSuccessHandler(appLogoutHandler);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
