package com.murun.authserver.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Client

@SpringBootApplication(exclude={SecurityFilterAutoConfiguration.class, SecurityAutoConfiguration.class})
public class AuthServer extends WebSecurityConfigurerAdapter  {



    public static void main(String[] arguments) {

        SpringApplication.run(AuthServer.class, arguments);
    }




}
