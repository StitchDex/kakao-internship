package com.example.iam_guide_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//172.26.100.54

    @Autowired
    private IamAuthentication authProvider;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/error").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/admin/**","/admin").hasRole("ADMIN")
                .anyRequest().authenticated()

       .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/guide")
       .and()
                .logout()
                .deleteCookies("JSESSIONID").invalidateHttpSession(true)
        .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
        http.sessionManagement().invalidSessionUrl("/login").maximumSessions(1) .expiredUrl("/login");

    }


    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/webjars/**", "/fonts/**");
    }

}
