package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import web.model.User;

import javax.sql.DataSource;
import java.util.Properties;

    @Configuration
    @PropertySource("classpath:db.properties")
    @EnableTransactionManagement
    public class AppContext {

        @Autowired
        private Environment environment;

        @Bean
        public LocalSessionFactoryBean sessionFactory() {
            LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
            factoryBean.setDataSource(dataSource());

            Properties properties = new Properties();
            properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
            properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));

            factoryBean.setHibernateProperties(properties);
            factoryBean.setAnnotatedClasses(User.class);
            return factoryBean;
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(environment.getProperty("db.driver"));
            dataSource.setUrl(environment.getProperty("db.url"));
            dataSource.setUsername(environment.getProperty("db.username"));
            dataSource.setPassword(environment.getProperty("db.password"));
            return dataSource;
        }

        @Bean
        public HibernateTransactionManager getTransactionManager() {
            HibernateTransactionManager transactionManager = new HibernateTransactionManager();
            transactionManager.setSessionFactory(sessionFactory().getObject());
            return transactionManager;
        }
}