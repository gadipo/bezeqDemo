package com.ashgan.bezeqDemo.Service;

import com.ashgan.bezeqDemo.Model.UsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private final UsageService usageService;

    public MessageConsumer(UsageService usageService) {
        this.usageService = usageService;
    }

    @JmsListener(destination = "${jms.queue.usage-events}")
    public void consumeUsageEvent(UsageEvent usageEvent) {
        if (usageEvent == null) {
            LOGGER.error("Received null UsageEvent - triggering rollback");
            throw new IllegalArgumentException("UsageEvent cannot be null");
        }

        if (usageEvent.getCustomerId() == null || usageEvent.getCustomerId().isBlank()) {
            LOGGER.error("Received UsageEvent with null/empty customerId: {}", usageEvent);
            throw new IllegalArgumentException("CustomerId cannot be null or empty");
        }

        if (usageEvent.getServiceType() == null || usageEvent.getEventType() == null) {
            LOGGER.error("Received UsageEvent with null serviceType/eventType: {}", usageEvent);
            throw new IllegalArgumentException("ServiceType and EventType cannot be null");
        }

        usageService.processEvent(usageEvent);
    }

}
