package com.ashgan.bezeqDemo.Client.REST;

import com.ashgan.bezeqDemo.DTO.BillingNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BillingClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingClient.class);

    @Value("${external.billing.api.url}")
    private String billingApiUrl;

    private final RestTemplate restTemplate;

    public BillingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyBillingSystem(String customerId, String serviceType, Double amount) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept",MediaType.APPLICATION_JSON_VALUE);

            BillingNotification billingNotification = new BillingNotification(customerId,serviceType,amount);
            HttpEntity<BillingNotification> request = new HttpEntity<>(billingNotification,headers);

            String response = restTemplate.postForObject(billingApiUrl + "/notify",request, String.class);

            LOGGER.info("Sending Billing notification for customer:{} , Service Type: {} , for Amount:{}" , customerId, serviceType, amount);
        } catch (Exception e) {
            LOGGER.error("Failed to notify billing system for customer:{} , Service Type: {} , for Amount:{}" , customerId, serviceType, amount);
        }
    }


}
