package com.notification.notificationService.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Service for processing templates and generating email content.
 */
@Service
@RequiredArgsConstructor
public class TemplateProcessor {

    Logger logger = LoggerFactory.getLogger(TemplateProcessor.class);


    final TemplateEngine templateEngine;

    /**
     * Processes a plain text template by replacing placeholders with their corresponding values.
     *
     * @param templateName The name of the template file (without extension).
     * @param placeholders A map of placeholder keys and their corresponding replacement values.
     * @return The processed template content as a string.
     * @throws RuntimeException If the template file is not found or cannot be read.
     */
    public String processTemplate(String templateName, Map<String, String> placeholders) {
        StringBuilder templateContent = new StringBuilder();

        String templatePath = templateName + "_template.txt";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: " + templatePath);
        }

        // Read the template file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                templateContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read template file", e);
        }

        // Replace placeholders in the template
        String content = templateContent.toString();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        return content;
    }

    /**
     * Processes an HTML email template using Thymeleaf and replaces placeholders with their values.
     *
     * @param templateName The name of the HTML template file (without extension).
     * @param placeholders A map of placeholder keys and their corresponding replacement values.
     * @return The processed HTML content as a string.
     */
    public String processHtmlEmailTemplate(String templateName, Map<String, String> placeholders) {
        Context context = new Context();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String templateFileName = templateName + "_template.html";
        logger.warn("Template file: {}", templateFileName);
        return templateEngine.process(templateFileName, context);
    }
}