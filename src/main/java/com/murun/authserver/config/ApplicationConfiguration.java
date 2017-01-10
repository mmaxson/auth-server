package com.murun.authserver.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.h2.server.web.WebServlet;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;


@EnableTransactionManagement
@ComponentScan(basePackages="com.murun.*")


@EnableJpaRepositories("com.murun.fict.*")
@EnableCaching
@Configuration
@EnableSwagger2
@RefreshScope
@Import({ AuthorizationServerConfiguration.class })    //, OAuth2SecurityConfiguration.class

public class ApplicationConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "jdbc.driverClassName";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "jdbc.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "jdbc.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "jdbc.username";
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String PROPERTY_NAME_HIBERNATE_CONNECTION_POOL_SIZE = "hibernate.connection.pool_size";

    private static final String PROPERTY_NAME_HIBERNATE_DEFAULT_SCHEMA = "hibernate.default_schema";
    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";


    private static final String PROPERTY_NAME_HIBERNATE_ID_NEW_GENERATOR_MAPPINGS =  "hibernate.id.new_generator_mappings";


    private static final String PROPERTY_VALUE_ENTITYMANAGER_PACKAGES_TO_SCAN = "com.murun.fict.*";


    @Resource
    private Environment env;


    @Profile("dev")
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }


    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword((env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD)));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(PROPERTY_VALUE_ENTITYMANAGER_PACKAGES_TO_SCAN);
        entityManagerFactoryBean.setPersistenceUnitName("control");

        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(hibProperties());


        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT,	env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
        properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE));
        properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE));
        properties.put(PROPERTY_NAME_HIBERNATE_CONNECTION_POOL_SIZE, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_CONNECTION_POOL_SIZE));
        properties.put(PROPERTY_NAME_HIBERNATE_DEFAULT_SCHEMA, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DEFAULT_SCHEMA));
        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
        properties.put(PROPERTY_NAME_HIBERNATE_ID_NEW_GENERATOR_MAPPINGS, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ID_NEW_GENERATOR_MAPPINGS));

        return properties;
    }
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Primary
    @Bean
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        return new Jackson2ObjectMapperBuilder().modulesToInstall(Hibernate5Module.class);
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
                "entity",
                "Entity API",
                "1.0",
                "Terms of service",
                "mmurun@gmail.com",
                "License of API",
                "API license URL");
        return apiInfo;
    }

}
