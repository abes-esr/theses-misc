package fr.abes.theses.thesesmisc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
public class ThesesBatchConfigurer implements BatchConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    private PlatformTransactionManager transactionManager;

    private JobRepository jobRepository;

    private JobLauncher jobLauncher;

    private JobExplorer jobExplorer;

    private final DataSource thesesDatasource;

    public ThesesBatchConfigurer(DataSource dataSource, EntityManagerFactory entityManagerFactory) throws Exception{
        this.entityManagerFactory = entityManagerFactory;
        this.thesesDatasource = dataSource;
    }

    @Override
    public JobRepository getJobRepository() throws Exception {
        return this.jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return this.transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return this.jobLauncher;
    }


    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return this.jobExplorer;
    }


    @PostConstruct
    public void initialize() {
        try {
            this.transactionManager = createTransactionManager();
            this.jobRepository = createJobRepository();
            this.jobLauncher = createJobLauncher();
            this.jobExplorer = createJobExplorer();
        }
        catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize Spring Batch", ex);
        }
    }

    protected JobExplorer createJobExplorer() throws Exception {
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(this.thesesDatasource);
        jobExplorerFactoryBean.setTablePrefix("BATCH_");
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }

    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.thesesDatasource);
        if (this.entityManagerFactory != null) {
            log.warn("JPA does not support custom isolation levels, so locks may not be taken when launching Jobs");
            factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        }
            factory.setTablePrefix("BATCH_");
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    protected PlatformTransactionManager createTransactionManager() {
        if (this.entityManagerFactory != null) {
            return new JpaTransactionManager(this.entityManagerFactory);
        }
        return new DataSourceTransactionManager(this.thesesDatasource);
    }
}
