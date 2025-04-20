package com.notification.notificationService.service.impl;

import com.notification.notificationService.aspect.LogNotificationAware;
import com.notification.notificationService.dto.NotificationDetailsDto;
import com.notification.notificationService.dto.NotificationTempDto;
import com.notification.notificationService.entity.NotificationTemplate;
import com.notification.notificationService.entity.User;
import com.notification.notificationService.exception.TemplateNotFoundException;
import com.notification.notificationService.exception.UserNotFoundException;
import com.notification.notificationService.repository.NotificationRepository;
import com.notification.notificationService.repository.userRepository;
import com.notification.notificationService.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service implementation for handling notification-related operations.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    public static final List<String> listOfDynamicFields = Arrays.asList("subject", "user");

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);


    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    userRepository userRepository;

    @Autowired
    TemplateProcessor templateProcessor;

    @Autowired
    private JavaMailSender javaMailSender;

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
     * Sends an email to a user based on the provided template and CC list.
     *
     * @param userId       The ID of the user to whom the email will be sent.
     * @param templateName The name of the template to be used for the email.
     * @param toCcList     The list of email addresses to be CC'd.
     */
    @Override
    public void sendEmail(String userId, String templateName, List<String> toCcList) throws MessagingException {
        try {
            NotificationTemplate notificationTemplate = notificationRepository
                    .findByTemplateName(templateName).orElseThrow(() -> new TemplateNotFoundException("Template not found", templateName));
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UserNotFoundException("User not found", userId));
            String content = null != user ? prepareContent(templateName, user, notificationTemplate) : null;
            if (null != content && null != notificationTemplate) {
                sendHtmlEmail(Collections.singletonList(user.getUserEmail()), toCcList, notificationTemplate.getSubject(), content);
            } else {
                logger.warn("User not found or content is null");
            }
        } catch (Exception e) {
            logger.warn("Error occurred while sending email: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Sends a notification based on the provided details.
     *
     * @param notificationDetailsDto The DTO containing the details of the notification to be sent.
     */
    @Override
    @LogNotificationAware
    public void sendNotification(NotificationDetailsDto notificationDetailsDto) throws MessagingException {
        if (notificationDetailsDto.getUserId() != null && notificationDetailsDto.getTemplateName() != null) {
            logger.warn("userId {} ,templateName {} ", notificationDetailsDto.getUserId(), notificationDetailsDto.getTemplateName());
            sendEmail(notificationDetailsDto.getUserId(), notificationDetailsDto.getTemplateName(), notificationDetailsDto.getCcList());
        } else {
            NotificationTemplate notificationTemplate = notificationRepository
                    .findByTemplateName(notificationDetailsDto.getTemplateName()).
                    orElseThrow(() -> new TemplateNotFoundException("Template not found", notificationDetailsDto.getTemplateName()));
            String content = prepareContent(notificationDetailsDto.getTemplateName(), null, notificationTemplate);
            sendHtmlEmail(notificationDetailsDto.getToList(), notificationDetailsDto.getCcList(), notificationTemplate.getSubject(), content);
        }
    }

    /**
     * Prepares the content of an email by processing the template with placeholders.
     *
     * @param templateName         The name of the template to be processed.
     * @param user                 The user object containing user-specific data.
     * @param notificationTemplate The notification template containing dynamic fields and subject.
     * @return The processed email content as a string.
     */
    private String prepareContent(String templateName, User user, NotificationTemplate notificationTemplate) {
        Map<String, String> placeholders = new HashMap<>();
        if (null != notificationTemplate) {
            String deltaField = notificationTemplate.getDynamicFields();
            String[] fields = deltaField.split(",");
            for (String field : fields) {
                if (listOfDynamicFields.contains(field)) {
                    if (field.equals("user")) {
                        placeholders.put(field, user.getUserName());
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

    /**
     * Sends an HTML email to the specified recipients with optional CC addresses.
     *
     * @param recipients The list of email addresses to send the email to.
     * @param cc         The list of email addresses to be CC'd.
     * @param subject    The subject of the email.
     * @param content    The HTML content of the email.
     */
    private void sendHtmlEmail(List<String> recipients, List<String> cc, String subject, String content) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            // Set the email properties
            mailMessage.setTo(recipients.toArray(new String[0])); // Convert list to array
            mailMessage.setFrom(sender);
            mailMessage.setSubject(subject);
            if (!cc.isEmpty()) {
                mailMessage.setCc(cc.toArray(new String[0])); // Convert list to array
            }
            mailMessage.setText(content, true); // true indicates HTML content
            // Send the email
            javaMailSender.send(mimeMessage);
            logger.warn("Email sent to: {}", recipients);
        } catch (Exception e) {
            logger.warn("Error while sending mail: {}", e.getMessage());
            throw e;
        }
    }
}