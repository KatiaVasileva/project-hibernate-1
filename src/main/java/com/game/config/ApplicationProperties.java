package com.game.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class ApplicationProperties {
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";

    private final String url;
    private final String username;
    private final String password;
    private final String driverClass;

    public ApplicationProperties(
            @Value("${"+ HIBERNATE_CONNECTION_URL + "}") String url,
            @Value("${"+ HIBERNATE_CONNECTION_USERNAME + "}") String username,
            @Value("${"+ HIBERNATE_CONNECTION_PASSWORD + "}") String password,
            @Value("${"+ HIBERNATE_CONNECTION_DRIVER_CLASS + "}") String driverClass
    ) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
