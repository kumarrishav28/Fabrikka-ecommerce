package com.Fabrikka.loadProduct.config;

import com.Fabrikka.loadProduct.notification.NotificationTasklet;
import com.Fabrikka.loadProduct.repository.ProductFileRepository;
import com.Fabrikka.loadProduct.service.ExcelReader;
import com.Fabrikka.loadProduct.service.ProductItemProcessor;
import com.Fabrikka.loadProduct.service.ProductItemWriter;
import com.fabrikka.common.ProductDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

/**
 * Batch configuration for product import and notification steps.
 * <p>
 * Defines beans for reading Excel files, processing and writing product data,
 * sending notifications, and orchestrating the batch job.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private ProductFileRepository productFileRepository;
    @Autowired
    private ProductItemWriter productItemWriter;
    @Autowired
    private ProductItemProcessor processor;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private NotificationClient notificationClient;

    /**
     * Creates an {@link ExcelReader} bean for reading product data from an Excel file.
     * The file is identified by the provided fileId from job parameters.
     *
     * @param fileId the ID of the file to read, injected from job parameters
     * @return an instance of {@link ExcelReader}
     * @throws IOException if the file cannot be read
     */
    @StepScope
    @Bean
    public ExcelReader reader(@Value("#{jobParameters['fileId']}") Long fileId) throws IOException {
        return new ExcelReader(fileId, productFileRepository);
    }

    /**
     * Defines a step that sends a notification after processing is complete and updates the file status.
     * The step uses {@link NotificationTasklet} to perform these actions.
     *
     * @return the notification step
     */
    @Bean
    public Step notificationStep() {
        return new StepBuilder("notificationStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    Long fileId = (Long) chunkContext.getStepContext().getJobParameters().get("fileId");
                    String fileName = chunkContext.getStepContext().getJobParameters().get("fileName").toString();
                    boolean isSuccess = contribution.getStepExecution()
                            .getJobExecution()
                            .getStepExecutions()
                            .stream()
                            .filter(se -> se.getStepName().equals("importStep"))
                            .findFirst()
                            .map(se -> se.getExitStatus().getExitCode().equals("COMPLETED"))
                            .orElse(false);
                    new NotificationTasklet(fileName, fileId, productFileRepository, isSuccess, notificationClient)
                            .execute(contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    /**
     * Defines the import step for reading, processing, and writing product data in chunks.
     * Configures fault tolerance and skip logic for exceptions.
     *
     * @param processor the processor for transforming product items
     * @param productItemWriter the writer for persisting product items
     * @return the import step
     * @throws IOException if the reader cannot be created
     */
    @Bean
    public Step importStep(ProductItemProcessor processor, ProductItemWriter productItemWriter) throws IOException {
        return new StepBuilder("importStep", jobRepository)
                .<ProductDto, ProductDto>chunk(10, transactionManager)
                .reader(reader(null)) // Spring injects job parameters at runtime
                .processor(processor)
                .writer(productItemWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }

    /**
     * Defines the batch job for importing products.
     * The job starts with the import step.
     *
     * @param importStep the step for importing products
     * @return the configured job
     */
    @Bean
    public Job importJob(Step importStep , Step notificationStep) {
        return new JobBuilder("importJob", jobRepository)
                .start(importStep)
                .next(notificationStep)
                .build();
    }
}