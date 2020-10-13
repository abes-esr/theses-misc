package fr.abes.theses.deletesubdivision;

import fr.abes.theses.deletesubdivision.configuration.ThesesOracleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
@Import(ThesesOracleConfig.class)
@EnableRetry
public class BatchConfiguration {
    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;

    private final DataSource thesesDatasource;

    @Value("${chunkSize}")
    private Integer chunkSize;

    public BatchConfiguration(JobBuilderFactory jobs, StepBuilderFactory steps, DataSource thesesDatasource) {
        this.jobs = jobs;
        this.steps = steps;
        this.thesesDatasource = thesesDatasource;
    }

    protected JobParametersIncrementer incrementer() {
        return new TimeIncrementer();
    }

    @Bean
    public BatchConfigurer configurer(EntityManagerFactory entityManagerFactory) throws Exception {
        return new ThesesBatchConfigurer(thesesDatasource, entityManagerFactory);
    }

    @Bean
    public Job deleteSubdivision(ItemReader reader, @Qualifier("tefProcessor") ItemProcessor processor, ItemWriter writer) {
        log.info("Début du job de suppression des these et écrits académiques");

        return jobs
                .get("deleteSubdivision").incrementer(incrementer())
                .start(deleteTheseEcritAcademiques(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteSubdivisionDeFormeJob (ItemReader reader, @Qualifier("subdivisionDeFormeProcessor") ItemProcessor processor, ItemWriter writer){
        log.info("Début du job de suppression des Subdivision De Forme");

        return jobs
                .get("deleteSubdivisionDeForme").incrementer(incrementer())
                .start(deleteSubdivisionDeForme(reader, processor,writer))
                .build();
    }

    @Bean
    public Job changeContentIdJob(ItemReader reader, @Qualifier("changeContentIdProcessor") ItemProcessor processor, ItemWriter writer){
        return jobs
                .get("changeContentId").incrementer(incrementer())
                .start(changeContentId(reader, processor,writer))
                .build();
    }

    private Step changeContentId(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("changeContentId").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step deleteTheseEcritAcademiques(ItemReader reader, @Qualifier("tefProcessor") ItemProcessor processor, ItemWriter writer){
        return steps.get("deleteTheseEcritAcademiques").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step deleteSubdivisionDeForme(ItemReader reader, @Qualifier("subdivisionDeFormeProcessor") ItemProcessor processor, ItemWriter writer){
        return steps.get("deleteTheseEcritAcademiques").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


}
