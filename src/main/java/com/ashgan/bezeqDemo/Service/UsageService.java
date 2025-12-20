package com.ashgan.bezeqDemo.Service;


import com.ashgan.bezeqDemo.Model.UsageEvent;
import com.ashgan.bezeqDemo.Model.UsageSummary;
import com.ashgan.bezeqDemo.Repository.UsageEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsageService {

    // private final MessageProducer messageProducer;
    // private final UsageEventRepository repository;
    private final Map<String, UsageSummary> customerSummaries = new ConcurrentHashMap<>();

    // public UsageService(MessageProducer messageProducer, UsageEventRepository repository) {
    //     this.messageProducer = messageProducer;
    //     this.repository = repository;
    // }

    // public void processEvent(UsageEvent event) {
    //     UsageSummary summary = customerSummaries.computeIfAbsent(
    //         event.getCustomerId(),
    //         k -> new UsageSummary(k)
    //     );

    //     addEvent(summary, event);
    //     messageProducer.sendToQueue(event);


    // }

    private void addEvent(UsageSummary usageSummary, UsageEvent usageEvent) {
        String eventType = usageEvent.getEventType();
        String serviceType = usageEvent.getServiceType();

        switch (serviceType) {
            case "INTERNET":
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
