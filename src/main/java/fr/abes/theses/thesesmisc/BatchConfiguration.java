package fr.abes.theses.thesesmisc;

import fr.abes.theses.thesesmisc.configuration.ThesesOracleConfig;
import fr.abes.theses.thesesmisc.tasklets.scinderVedetteRameau.ScinderVedetteRameauCompositeItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import fr.abes.theses.thesesmisc.utils.ScissionRameauList;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

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

    public BatchConfiguration(JobBuilderFactory jobs, StepBuilderFactory steps,
                              @Qualifier("thesesDatasource") DataSource thesesDatasource) {
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
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job changeUrlStarCas1(@Qualifier("changeUrlReaderCas1") ItemReader reader,
                                 @Qualifier("changeUrlCas1Processor") ItemProcessor processor,
                                 @Qualifier("tefWriter") ItemWriter writer) {
        log.info("Début du job de changement des url");

        return jobs
                .get("changeUrlStarCas1").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job deleteSubdivision(@Qualifier("tefReader") ItemReader reader, @Qualifier("tefProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer) {
        log.info("Début du job de suppression des these et écrits académiques");

        return jobs
                .get("deleteSubdivision").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteSubdivisionDeFormeJob (@Qualifier("tefReader") ItemReader reader, @Qualifier("subdivisionDeFormeProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        log.info("Début du job de suppression des Subdivision De Forme");

        return jobs
                .get("deleteSubdivisionDeForme").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job changeContentIdJob(@Qualifier("tefReader") ItemReader reader, @Qualifier("changeContentIdProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer){
        return jobs
                .get("changeContentId").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteSolrIndexJob(@Qualifier("deleteDoublonSolrReader") ItemReader reader,
                                  @Qualifier("deleteDoublonSolrProcessor") ItemProcessor processor,
                                  @Qualifier("deleteDoublonSolrWriter") ItemWriter writer) {
        return jobs
                .get("deleteSolrIndex").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job deleteHistEtCritiqueJob(@Qualifier("histoireEtCritiqueReader") ItemReader reader, @Qualifier("histoireEtCritiqueProcessor") ItemProcessor processor, @Qualifier("tefWriter") ItemWriter writer) {
        return jobs
                .get("deleteHistEtCritique").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }
    @Bean
    public Job deleteWhiteSpaceIdSourceStepJob(@Qualifier("deleteWhiteSpacesBddReader") ItemReader reader,
                                               @Qualifier("deleteWhiteSpacesBddProcessor") ItemProcessor processor,
                                               @Qualifier("tefWriter") ItemWriter writer) {
        return jobs
                .get("deleteWhiteSpaceIdSourceStep").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }
    @Bean
    public Job retourCinesSTARJob(@Qualifier("retournCinesReader") ItemReader reader,
                                               @Qualifier("retournCinesProcessor") ItemProcessor processor,
                                               @Qualifier("tefWriter") ItemWriter writer) {
        return jobs
                .get("retourCinesSTARJob").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job changeIdSource(@Qualifier("changeIdSourceReader") ItemReader reader,
                              @Qualifier("changeIdSourceProcessor") ItemProcessor processor,
                              @Qualifier("changeIdSourceWritter") ItemWriter writer) {
        return jobs
                .get("changeIdSource").incrementer(incrementer())
                .start(genericStep(reader, processor,writer))
                .build();
    }

    @Bean
    public Job searchAndReplace(@Qualifier("searchAndReplaceReader") ItemReader reader,
                            @Qualifier("searchAndReplaceProcessor") ItemProcessor processor,
                            @Qualifier("tefWriter") ItemWriter writer) {
        return jobs.get("searchAndReplace").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job abandon(@Qualifier("abandonReader") ItemReader reader,
                       @Qualifier("abandonProcessor") ItemProcessor processor,
                       @Qualifier("tefWriter") ItemWriter writer) {
        return jobs.get("abandon").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job normaliseNfc(@Qualifier("normaliseNfcReader") ItemReader reader,
                            @Qualifier("normaliseNfcProcessor") ItemProcessor processor,
                            @Qualifier("tefWriter") ItemWriter writer) {
        return jobs.get("normaliseNfc").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job fileLogSyncBddToSolr(@Qualifier("fileLogSyncBddToSolrReader") ItemReader reader,
                            @Qualifier("syncBddToSolrProcessor") ItemProcessor processor,
                            @Qualifier("syncBddToSolrWriter") ItemWriter writer) {
        return jobs.get("fileLogSyncBddToSolr").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job SyncBddToSolr(@Qualifier("syncBddToSolrReader") ItemReader reader,
                            @Qualifier("syncBddToSolrProcessor") ItemProcessor processor,
                            @Qualifier("syncBddToSolrWriter") ItemWriter writer) {
        return jobs.get("syncBddToSolr").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job MajAbesDiffuseur(@Qualifier("majAbesDiffuseurReader") ItemReader reader,
                                @Qualifier("majAbesDiffuseurProcessor") ItemProcessor processor,
                                @Qualifier("tefWriter") ItemWriter writer) {
        return jobs.get("majAbesDiffuseur").incrementer(incrementer())
                .start(genericStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Job CopyProdToTest(@Qualifier("copyProdToTestReader") ItemReader reader,
                              @Qualifier("copyProdToTestProcessor") ItemProcessor processor,
                              @Qualifier("tefWriter") ItemWriter writer,
                              @Qualifier("copyApplisProdToTestTasklet") Tasklet copyApplisProdToTestTasklet) {

        Step copyApplisStep = steps.get("copyApplisProdToTestStep")
                .tasklet(copyApplisProdToTestTasklet)
                .build();

        return jobs.get("copyProdToTest").incrementer(incrementer())
                .start(copyApplisStep)
                .next(genericStep(reader, processor, writer))
                .build();
    }

    private Step genericStep(ItemReader reader, ItemProcessor processor, ItemWriter writer) {
        return steps.get("genericStep").chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step loadScissionsStep() {
        return steps.get("loadScissionsStep")
                .tasklet((contribution, chunkContext) -> {
                    try {
                        ScissionRameauList.loadScissionRameauList();  // Charge les données du CSV
                        log.info("Chargement de la liste de scissions terminé.");
                    } catch (IOException e) {
                        log.error("Erreur lors du chargement de la liste des scissions.", e);
                        throw new RuntimeException("Erreur lors du chargement de la liste des scissions.", e);
                    }

                    try {
                        ScissionRameauList.loadScissionRameauTefIdList();  // Charge les données du CSV
                        log.info("Chargement de la liste des ID des TEF à traiter terminé.");
                    } catch (IOException e) {
                        log.error("Erreur lors du chargement de la liste des ID TEF.", e);
                        throw new RuntimeException("Erreur lors du chargement de la liste des ID TEF.", e);
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job scinderVedetteRameauJob(
            @Qualifier("scinderVedetteRameauReader") ItemReader reader,
            @Qualifier("scinderVedetteRameauProcessor") ItemProcessor processor,
            ScinderVedetteRameauCompositeItemWriter writer) {

        return jobs.get("scinderVedetteRameauJob")
                .incrementer(incrementer())
                .start(loadScissionsStep())  // Step de chargement du tableau des scissions
                .next(genericStep(reader, processor, writer))  // Step de traitement
                .build();
    }
}
