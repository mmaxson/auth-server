package com.murun.authserver.main;

import com.murun.authserver.config.AuthorizationServerConfiguration;
import com.murun.authserver.config.OAuth2SecurityConfiguration;
import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;


@EnableTransactionManagement
@ComponentScan(basePackages="com.murun.*")


@Configuration
@EnableSwagger2
@RefreshScope
@Import({ AuthorizationServerConfiguration.class, OAuth2SecurityConfiguration.class })

public class ApplicationConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Profile("dev")
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        // this is for 1.4 compatibility
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SPRING_WEB)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "AuthServer",
                "OAuth Server",
                "1.0",
                "Terms of service",
                "mmurun@gmail.com",
                "License of API",
                "API license URL");
        return apiInfo;
    }



}
