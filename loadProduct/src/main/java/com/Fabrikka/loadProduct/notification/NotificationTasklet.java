package com.Fabrikka.loadProduct.notification;

import com.Fabrikka.loadProduct.config.NotificationClient;
import com.Fabrikka.loadProduct.entity.ProductFile;
import com.Fabrikka.loadProduct.repository.ProductFileRepository;
import com.fabrikka.common.NotificationDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Tasklet for sending batch process notifications and updating file status.
 */
@AllArgsConstructor
public class NotificationTasklet implements Tasklet {

    /**
     * Name of the file being processed.
     */
    private final String fileName;

    /**
     * ID of the file in the database.
     */
    private final Long fileId;

    /**
     * Repository for accessing and updating ProductFile entities.
     */
    private final ProductFileRepository productFileRepository;

    /**
     * Indicates if the batch process was successful.
     */
    private final boolean isSuccess;

    /**
     * Client for sending notifications.
     */
    private final NotificationClient notificationClient;

    /**
     * Executes the tasklet: sends notification and updates file status.
     *
     * @param contribution the step contribution
     * @param chunkContext the chunk context
     * @return RepeatStatus.FINISHED when done
     * @throws Exception if any error occurs
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String status = isSuccess ? "SUCCESS" : "FAILURE";
        String subject = buildSubject(fileName, isSuccess);
        String errorMessage = null;
        if (!isSuccess) {
            Exception exception = null;
            var stepExecution = chunkContext.getStepContext().getStepExecution();
            if (!stepExecution.getFailureExceptions().isEmpty()) {
                exception = (Exception) stepExecution.getFailureExceptions().get(0);
                errorMessage = exception.getMessage();
            }
        }
        NotificationDetailsDto notificationDetailsDto = buildNotificationDetails(subject, isSuccess, errorMessage);
        notificationClient.sendNotificationGeneric(notificationDetailsDto);
        updateProductFileStatus(fileId, status);
        return RepeatStatus.FINISHED;
    }

    /**
     * Builds the email subject based on file name and process result.
     *
     * @param fileName  the file name
     * @param isSuccess true if process succeeded, false otherwise
     * @return the subject string
     */
    private String buildSubject(String fileName, boolean isSuccess) {
        if (isSuccess) {
            return "File with ID " + fileName + " processed successfully.";
        } else {
            return "File with ID " + fileName + " failed to process.";
        }
    }

    /**
     * Constructs the notification details DTO for the email.
     *
     * @param subject   the email subject
     * @param isSuccess true if process succeeded, false otherwise
     * @return the NotificationDetailsDto object
     */
    private NotificationDetailsDto buildNotificationDetails(String subject, boolean isSuccess, String errorMessage) {
        NotificationDetailsDto dto = new NotificationDetailsDto();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("subject", subject);
        if (!isSuccess && errorMessage != null) {
            attributes.put("fileName", fileName);
            attributes.put("fileId", String.valueOf(fileId));
            attributes.put("errorMessage", errorMessage);
        }
        dto.setToUserDetails(Map.of("kumarrishav28@gmail.com", "Rishav Kumar"));
        dto.setBatchNotificationAttributes(attributes);
        dto.setTemplateName(isSuccess ? "batch" : "batch_failed");
        return dto;
    }

    /**
     * Updates the status of the product file in the database.
     *
     * @param fileId the file ID
     * @param status the new status ("SUCCESS" or "FAILURE")
     */
    private void updateProductFileStatus(Long fileId, String status) {
        Optional<ProductFile> productFileOpt = productFileRepository.findById(fileId);
        if (productFileOpt.isPresent()) {
            ProductFile file = productFileOpt.get();
            file.setStatus(status);
            productFileRepository.save(file);
        } else {
            throw new IllegalArgumentException("Product file not found for ID: " + fileId);
        }
    }
}