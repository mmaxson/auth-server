package com.murun.authserver.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;


@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class, JpaBaseConfiguration.class})
public class AuthServer{

    public static void main(String[] args) {
        SpringApplication.run(AuthServer.class, args);
    }




}
