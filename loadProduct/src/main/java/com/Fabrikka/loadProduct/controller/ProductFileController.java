package com.Fabrikka.loadProduct.controller;

import com.Fabrikka.loadProduct.entity.ProductFile;
import com.Fabrikka.loadProduct.repository.ProductFileRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * REST controller for handling product file uploads and triggering batch jobs.
 * <p>
 * Exposes an endpoint to upload Excel files, saves file metadata to the database,
 * and starts a Spring Batch job to process the uploaded file.
 */
@RestController
@RequestMapping("/productFile")
public class ProductFileController {

    private final ProductFileRepository productFileRepository;

    /**
     * Constructs the controller with the required repository.
     *
     * @param productFileRepository repository for managing ProductFile entities
     */
    public ProductFileController(ProductFileRepository productFileRepository) {
        this.productFileRepository = productFileRepository;
    }

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importJob;

    /**
     * Handles file upload requests, saves the file, and triggers the batch import job.
     *
     * @param file the uploaded Excel file
     * @return HTTP response indicating the result of the operation
     * @throws IOException if file reading fails
     * @throws JobInstanceAlreadyCompleteException if the job instance is already complete
     * @throws JobExecutionAlreadyRunningException if the job is already running
     * @throws JobParametersInvalidException if job parameters are invalid
     * @throws JobRestartException if the job cannot be restarted
     */
    @PostMapping(value = "/uploadProductFile", consumes = "multipart/form-data")
    ResponseEntity<String> uploadProductFile(@RequestParam("file") MultipartFile file) throws IOException,
            JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Invalid file type. Only Excel files are allowed.");
        }
        ProductFile productFile = new ProductFile();
        productFile.setFileName(file.getOriginalFilename());
        productFile.setUploadedAt(LocalDateTime.now());
        productFile.setFileData(file.getBytes());
        productFile.setStatus("PENDING");

        ProductFile savedFile = productFileRepository.save(productFile);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", savedFile.getFileName())
                .addLong("fileId", savedFile.getId())
                .addLocalDateTime("uploadedAt", savedFile.getUploadedAt())
                .toJobParameters();

        jobLauncher.run(importJob, jobParameters);
        return ResponseEntity.ok("Job triggered for File ID: " + savedFile.getId());
    }
}