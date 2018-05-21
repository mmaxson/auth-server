package com.murun.authserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties
@Validated
public class ApplicationProperties {


    public static class App{

        @NotBlank
        private String allowedOrigins;

        public String getAllowedOrigins() {
            return allowedOrigins;
        }
        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }



    private App app;


    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

}
