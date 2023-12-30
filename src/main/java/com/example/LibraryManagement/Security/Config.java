package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
public class Config extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityService service;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(service);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/student/**").hasAuthority(Constants.STUDENT_SELF_INFO_AUTHORITY)
                .antMatchers(HttpMethod.GET,"/student-by-id/**").hasAuthority(Constants.STUDENT_INFO_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/admin/**").hasAuthority(Constants.CREATE_ADMIN_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/author/**").hasAuthority(Constants.CREATE_AUTHOR_AUTHORITY)
                .antMatchers(HttpMethod.POST, "/book/**").hasAuthority(Constants.CREATE_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/book/**").hasAuthority(Constants.READ_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/books/**").hasAuthority(Constants.READ_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/findBook/**").hasAuthority(Constants.READ_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/transaction/**").hasAuthority(Constants.INITIATE_TRANSACTION_AUTHORITY)
                .antMatchers(HttpMethod.POST, "/payment/**").hasAuthority(Constants.MAKE_PAYMENT_AUTHORITY)

                .antMatchers("/**").permitAll()
                .and()
                .formLogin();
    }
}
//cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//                .and()
//                .csrf().disable