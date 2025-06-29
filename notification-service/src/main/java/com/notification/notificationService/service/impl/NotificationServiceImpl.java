package com.notification.notificationService.service.impl;

import com.fabrikka.common.NotificationDetailsDto;
import com.fabrikka.common.NotificationTempDto;
import com.notification.notificationService.aspect.LogNotificationAware;
import com.notification.notificationService.entity.NotificationTemplate;
import com.notification.notificationService.exception.TemplateNotFoundException;
import com.notification.notificationService.repository.NotificationRepository;
import com.notification.notificationService.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service implementation for handling notification-related operations.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    public static final List<String> listOfDynamicFields = Arrays.asList("subject", "user");

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);


    @Value("${spring.mail.username}")
    private String sender;


    final NotificationRepository notificationRepository;

    final TemplateProcessor templateProcessor;

    final private JavaMailSender javaMailSender;

    /**
     * Creates a new notification template and saves it to the repository.
     *
     * @param notificationTempDto The DTO containing the details of the notification template to be created.
     */
    @Override
    public void createNotificationTemplate(NotificationTempDto notificationTempDto) {
        NotificationTemplate notificationTempEntity = new NotificationTemplate();
        notificationTempEntity.setTemplateName(notificationTempDto.getTemplateName());
        notificationTempEntity.setSubject(notificationTempDto.getSubject());
        notificationTempEntity.setDynamicFields(notificationTempDto.getDynamicFields());
        notificationRepository.save(notificationTempEntity);
    }


    /**
     * Sends a notification based on the provided details.
     *
     * @param notificationDetailsDto The DTO containing the details of the notification to be sent.
     */
    @Override
    @LogNotificationAware
    public void sendNotification(NotificationDetailsDto notificationDetailsDto) throws MessagingException {
            NotificationTemplate notificationTemplate = notificationRepository
                    .findByTemplateName(notificationDetailsDto.getTemplateName()).
                    orElseThrow(() -> new TemplateNotFoundException("Template not found", notificationDetailsDto.getTemplateName()));
            String content = prepareContent(notificationDetailsDto.getTemplateName(), notificationTemplate, notificationDetailsDto);
            sendHtmlEmail(notificationDetailsDto.getToUserDetails(), notificationDetailsDto.getCcUserDetails(), notificationTemplate.getSubject(), content);
    }

    private String prepareContent(String templateName,NotificationTemplate notificationTemplate,NotificationDetailsDto notificationDetailsDto) {
        Map<String, String> placeholders = new HashMap<>();
        if (null != notificationTemplate) {
            String deltaField = notificationTemplate.getDynamicFields();
            String[] fields = deltaField.split(",");
            for (String field : fields) {
                if (listOfDynamicFields.contains(field)) {
                    if (field.equals("user")) {
                        placeholders.put(field, createUserName(notificationDetailsDto.getToUserDetails()));
                    } else if (field.equals("subject")) {
                        placeholders.put(field, notificationTemplate.getSubject());
                    } else {
                        placeholders.put(field, field);
                    }
                }
            }
        }
        return templateProcessor.processHtmlEmailTemplate(templateName, placeholders);
    }


    private void sendHtmlEmail(Map<String,String> toUserDetails,Map<String,String> ccUserDetails, String subject, String content) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            // Set the email properties
            mailMessage.setTo(getRecipients(toUserDetails).toArray(new String[0]));
            mailMessage.setFrom(sender);
            mailMessage.setSubject(subject);
            if (null != ccUserDetails && !ccUserDetails.isEmpty()) {
                mailMessage.setCc(getCcList(ccUserDetails).toArray(new String[0]));
            }
            mailMessage.setText(content, true); // true indicates HTML content
            // Send the email
            javaMailSender.send(mimeMessage);
            logger.warn("Email sent to: {}", getRecipients(toUserDetails));
        } catch (Exception e) {
            logger.warn("Error while sending mail: {}", e.getMessage());
            throw e;
        }
    }

    private String createUserName(Map<String,String> toUserDetails) {
        StringBuilder userName = new StringBuilder();
        for (Map.Entry<String, String> entry : toUserDetails.entrySet()) {
           userName.append(entry.getValue()).append(",");
        }
        if (!userName.isEmpty()) {
            userName.setLength(userName.length() - 1); // Remove the last comma
        }
        return userName.toString();
    }

    private List<String> getRecipients(Map<String,String> toUserDetails) {
        List<String> recipients = new ArrayList<>();
        for (Map.Entry<String, String> entry : toUserDetails.entrySet()) {
            recipients.add(entry.getKey());
        }
        return recipients;
    }

    private List<String> getCcList(Map<String,String> ccUserDetails) {
        List<String> cc = new ArrayList<>();
        for (Map.Entry<String, String> entry : ccUserDetails.entrySet()) {
            cc.add(entry.getKey());
        }
        return cc;
    }

    @Override
    @LogNotificationAware
    public void sendNotificationGeneric(NotificationDetailsDto notificationDetailsDto) throws MessagingException {
        if (notificationDetailsDto != null && notificationDetailsDto.getTemplateName() != null && notificationDetailsDto.getBatchNotificationAttributes() != null) {
            String batchContent = prepareBatchContent(notificationDetailsDto);
            sendHtmlEmail(notificationDetailsDto.getToUserDetails(),
                    notificationDetailsDto.getCcUserDetails(), notificationDetailsDto.getBatchNotificationAttributes().get("subject"), batchContent);
        } else {
            throw new IllegalArgumentException("Batch notification attributes cannot be null");
        }
    }

    private String prepareBatchContent(NotificationDetailsDto notificationDetailsDto) {
        return templateProcessor.processHtmlEmailTemplate(notificationDetailsDto.getTemplateName(), notificationDetailsDto.getBatchNotificationAttributes());
    }



}