package fr.abes.theses.thesesmisc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages ={"fr.abes.theses.thesesmisc.service", "fr.abes.theses.thesesmisc.entities"})
@EnableJpaRepositories(transactionManagerRef = "thesesTransactionManager", entityManagerFactoryRef = "thesesEntityManagerFactory",basePackages = "fr.abes.theses.thesesmisc.dao")
public class ThesesOracleConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.jpa.database-platform}")
    private String platform;
    @Value("${spring.jpa.show-sql}")
    private String showsql;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Bean
    public LocalContainerEntityManagerFactoryBean thesesEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(thesesDatasource());
        em.setPackagesToScan("fr.abes.theses.thesesmisc.entities");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("spring.jpa.database-platform", platform);
        properties.put("hibernate.show_sql", showsql);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.dialect", dialect);
        properties.put("logging.level.org.hibernate", "DEBUG");
        properties.put("hibernate.type", "trace");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public DataSource thesesDatasource() {
        final DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(this.driver);
        datasource.setUrl(this.url);
        datasource.setUsername(this.username);
        datasource.setPassword(this.password);
        return datasource;
    }

    @Bean
    public JpaTransactionManager thesesTransactionManager(EntityManagerFactory em) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(em);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }




}
