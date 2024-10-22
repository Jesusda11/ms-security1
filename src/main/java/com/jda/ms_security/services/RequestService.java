package com.jda.ms_security.services;

import com.jda.ms_security.Models.EmailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class RequestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.ms_notification-url}")
    private String notificationUrl;

    public void sendEmail(EmailContent content) {
        String endPointName= "send-email";
        String url = notificationUrl + endPointName;
        restTemplate.postForObject(url, content, String.class);
    }
}
