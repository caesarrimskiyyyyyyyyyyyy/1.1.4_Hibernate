package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Util {
    private static final String DIALECT_KEY = "hibernate.dialect";
    private static final String DRIVER_KEY = "hibernate.connection.driver_class";
    private static final String URL_KEY = "hibernate.connection.url";
    private static final String USERNAME_KEY = "hibernate.connection.username";
    private static final String PASSWORD_KEY = "hibernate.connection.password";
    private static final String SQL_KEY = "hibernate.show_sql";
    private static final String HBM_KEY = "hibernate.hbm2ddl";

    private Util() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperty(URL_KEY),
                    PropertiesUtil.getProperty(USERNAME_KEY),
                    PropertiesUtil.getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private static Configuration getHibernateConfig() {
        Configuration configuration = new Configuration();
        configuration.setProperty(DIALECT_KEY, PropertiesUtil.getProperty(DIALECT_KEY));
        configuration.setProperty(DRIVER_KEY, PropertiesUtil.getProperty(DRIVER_KEY));
        configuration.setProperty(URL_KEY, PropertiesUtil.getProperty(URL_KEY));
        configuration.setProperty(USERNAME_KEY, PropertiesUtil.getProperty(USERNAME_KEY));
        configuration.setProperty(PASSWORD_KEY, PropertiesUtil.getProperty(PASSWORD_KEY));
        configuration.setProperty(SQL_KEY, PropertiesUtil.getProperty(SQL_KEY));
        configuration.setProperty(HBM_KEY, PropertiesUtil.getProperty(HBM_KEY));
        configuration.addAnnotatedClass(User.class);

        return configuration;
    }

    public static SessionFactory getSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(getHibernateConfig().getProperties())
                .build();

        return getHibernateConfig().buildSessionFactory(registry);
    }
}