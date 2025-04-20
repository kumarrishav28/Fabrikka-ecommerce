package com.notification.notificationService.aspect;

import com.notification.notificationService.dto.NotificationDetailsDto;
import com.notification.notificationService.entity.NotificationLog;
import com.notification.notificationService.repository.NotificationLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

@Aspect
@Component
public class LogNotificationAspect {

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    @Around("@annotation(com.notification.notificationService.aspect.LogNotificationAware)")
    public Object saveNotificationLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // Proceed with the method execution
            Object result = joinPoint.proceed();
            // post method execution without any error
            saveNotification(joinPoint, null, "MAIL_SENT");
            return result;
        } catch (Throwable throwable) {
            // Log the exception if it occurs
            saveNotification(joinPoint, throwable, "MAIL_FAILED");

            throw throwable; // Re-throw the exception after logging
        }
    }

    private void saveNotification(ProceedingJoinPoint joinPoint, Throwable throwable, String status) {
        NotificationLog notificationLog = new NotificationLog();
        NotificationDetailsDto notificationDetailsDto =
                joinPoint.getArgs()[0] instanceof NotificationDetailsDto ? (NotificationDetailsDto) joinPoint.getArgs()[0] : null;
        notificationLog.setNotifTempName(notificationDetailsDto.getTemplateName());
        if (throwable != null) {
            // Capture full stack trace as a string
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            String fullStackTrace = stringWriter.toString();
            notificationLog.setLogException(fullStackTrace);
        }
        notificationLog.setExcpetionDate(new Date());
        notificationLog.setStatus(status);
        notificationLogRepository.save(notificationLog);
        notificationLogRepository.save(notificationLog);
    }
}