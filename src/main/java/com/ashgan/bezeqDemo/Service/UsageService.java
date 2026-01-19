package com.ashgan.bezeqDemo.Service;


import com.ashgan.bezeqDemo.Model.UsageEvent;
import com.ashgan.bezeqDemo.Model.UsageSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageService.class);

    private final MessageProducer messageProducer;
    private final Map<String, UsageSummary> customerSummaries = new ConcurrentHashMap<>();

    public UsageService(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    public void processEvent(UsageEvent event) {
        UsageSummary summary = customerSummaries.computeIfAbsent(
                event.getCustomerId(),
                k -> new UsageSummary(k)
        );

            addEvent(summary, event);
    }

    private void addEvent(UsageSummary usageSummary, UsageEvent usageEvent) {
        String eventType = usageEvent.getEventType();
        String serviceType = usageEvent.getServiceType();

        switch (serviceType) {
            case "MOBILE_INTERNET":
                if (eventType.equals("USAGE")) {
                    usageSummary.setInternetUsageGB(usageSummary.getInternetUsageGB() + usageEvent.getAmount());
                } else if (eventType.equals("OUTAGE")) {
                    usageSummary.setOutageCount(usageSummary.getOutageCount() + 1);
                } else if (eventType.equals("THRESHOLD_EXCEEDED")) {
                    usageSummary.setThresholdAlerts(usageSummary.getThresholdAlerts() + 1);
                }
                break;

            case "LANDLINE":
                if (eventType.equals("USAGE")) {
                    usageSummary.setLandlineMinutes(usageSummary.getLandlineMinutes() + usageEvent.getAmount());
                }
                break;

            case "ENERGY":
                if (eventType.equals("METER_READ")) {
                    usageSummary.setEnergyKWH(usageSummary.getEnergyKWH() + usageEvent.getAmount());
                }
                break;
            default:
                throw new IllegalArgumentException("Service Type: " + serviceType + "does not exist !");
        }
    }


    public UsageSummary getSummary(String customerId) {
        return customerSummaries.getOrDefault(
                customerId,
                new UsageSummary(customerId)
        );
    }

    public Set<String> getAllCustomerIds() {
        return customerSummaries.keySet();
    }


}
