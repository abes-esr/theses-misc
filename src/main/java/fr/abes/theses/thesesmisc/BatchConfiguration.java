package fr.abes.theses.thesesmisc;

import fr.abes.theses.thesesmisc.configuration.ThesesOracleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.BeforeRead;
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
    public Job changeUrlStar(@Qualifier("changeUrlCas5Reader") ItemReader reader, @Qualifier("changeUrlCas5Processor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer) {
        log.info("Début du job de changement des url");

        return jobs
                .get("changeUrlStarCas5").incrementer(incrementer())
                .start(ajoutUrl(reader, processor, writer))
                .build();
    }

    @Bean
    public Job changeUrlStarCas1(@Qualifier("changeUrlReaderCas1") ItemReader reader,
                                 @Qualifier("changeUrlCas1Processor") ItemProcessor processor,
                                 @Qualifier("tefWriter") ItemWriter writer) {
        log.info("Début du job de changement des url");

        return jobs
                .get("changeUrlStarCas1").incrementer(incrementer())
                .start(ajoutUrl(reader, processor, writer))
                .build();
    }

    @Bean
    public Job deleteSubdivision(@Qualifier("tefReader") ItemReader reader, @Qualifier("tefProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer) {
        log.info("Début du job de suppression des these et écrits académiques");

        return jobs
                .get("deleteSubdivision").incrementer(incrementer())
                .start(deleteTheseEcritAcademiques(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteSubdivisionDeFormeJob (@Qualifier("tefReader") ItemReader reader, @Qualifier("subdivisionDeFormeProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        log.info("Début du job de suppression des Subdivision De Forme");

        return jobs
                .get("deleteSubdivisionDeForme").incrementer(incrementer())
                .start(deleteSubdivisionDeForme(reader, processor,writer))
                .build();
    }

    @Bean
    public Job changeContentIdJob(@Qualifier("tefReader") ItemReader reader, @Qualifier("changeContentIdProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        return jobs
                .get("changeContentId").incrementer(incrementer())
                .start(changeContentId(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteSolrIndexJob(@Qualifier("deleteDoublonSolrReader") ItemReader reader, @Qualifier("deleteDoublonSolrProcessor") ItemProcessor processor, @Qualifier("deleteDoublonSolrWriter") ItemWriter writer) {
        return jobs
                .get("deleteSolrIndex").incrementer(incrementer())
                .start(deleteSolrIndex(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteHistEtCritiqueJob(@Qualifier("histoireEtCritiqueReader") ItemReader reader, @Qualifier("histoireEtCritiqueProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer) {
        return jobs
                .get("deleteHistEtCritique").incrementer(incrementer())
                .start(deleteSolrIndex(reader, processor,writer))
                .build();
    }
    @Bean
    public Job deleteWhiteSpaceIdSourceStepJob(@Qualifier("deleteWhiteSpacesBddReader") ItemReader reader,
                                               @Qualifier("deleteWhiteSpacesBddProcessor") ItemProcessor processor,
                                               @Qualifier("tefWriter") ItemWriter writer) {
        return jobs
                .get("deleteWhiteSpaceIdSourceStep").incrementer(incrementer())
                .start(deleteSolrIndex(reader, processor,writer))
                .build();
    }


    private Step deleteWhiteSpaceIdSourceStepStep(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("deleteWhiteSpaceIdSourceStep").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    private Step deleteHistEtCritique(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("deleteHistEtCritique").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    private Step deleteSolrIndex(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("deleteSolrIndex").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    private Step ajoutUrl(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("ajoutUrl").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
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
    public Step deleteTheseEcritAcademiques(@Qualifier("tefReader") ItemReader reader, @Qualifier("tefProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        return steps.get("deleteTheseEcritAcademiques").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step deleteSubdivisionDeForme(@Qualifier("tefReader") ItemReader reader, @Qualifier("subdivisionDeFormeProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        return steps.get("deleteTheseEcritAcademiques").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


}
