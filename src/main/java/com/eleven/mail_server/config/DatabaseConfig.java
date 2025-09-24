package com.eleven.mail_server.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public HikariDataSource dataSource(){
        return DataSourceBuilder.create().url(env.getProperty("database.url"))
                .username(env.getProperty("database.username"))
                .password(env.getProperty("database.password"))
                .driverClassName("org.postgresql.Driver")
                .type(HikariDataSource.class).build();
    }
}