package com.game.config;

import com.game.entity.Player;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final ApplicationProperties appProps;

    public HibernateConfig(ApplicationProperties appProps) {
        this.appProps = appProps;
    }

    @Bean("sessionFactory")
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperty(ApplicationProperties.HIBERNATE_CONNECTION_URL, appProps.getUrl());
        configuration.setProperty(ApplicationProperties.HIBERNATE_CONNECTION_USERNAME, appProps.getUsername());
        configuration.setProperty(ApplicationProperties.HIBERNATE_CONNECTION_PASSWORD, appProps.getPassword());
        configuration.setProperty(ApplicationProperties.HIBERNATE_CONNECTION_DRIVER_CLASS, appProps.getDriverClass());
        configuration.addAnnotatedClass(Player.class);

        return configuration.buildSessionFactory();
    }
}

