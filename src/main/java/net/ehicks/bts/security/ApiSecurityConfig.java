package net.ehicks.bts.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class ApiSecurityConfig
{
    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/actuator/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN")
                    .and()
                    .httpBasic();
        }
    }
}
