package com.ashgan.bezeqDemo.Controller.REST;

import com.ashgan.bezeqDemo.Model.UsageEvent;
import com.ashgan.bezeqDemo.Service.MessageProducer;
import com.ashgan.bezeqDemo.Service.UsageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/event-sim")
public class EventSimulatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSimulatorController.class);

    private final MessageProducer messageProducer;
    private final Random random = new Random();

    @Value("${event.sim.max.internet}")
    private double MAX_INTERNET_PER_EVENT;
    @Value("${event.sim.max.landline}")
    private double MAX_LANDLINE_PER_EVENT;
    @Value("${event.sim.max.energy}")
    private double MAX_ENERGY_PER_EVENT;

    @Value("${jms.queue.usage-events}")
    private String usageEventsQueue;

    public EventSimulatorController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping("/sim-batch")
    public ResponseEntity<?> simulateBatchEvents(@RequestParam String custs, @RequestParam String evnts) {

        LOGGER.info("Simulating batch events: {} customers , {} events per customer",custs,evnts);

        int customers = Integer.parseInt(custs);
        int events = Integer.parseInt(evnts);

        for (int i=1;i<=customers;i++) {
            String customerId = "CUST" + i;
            for (int j=1;j<=events;j++) {
                UsageEvent event = generateRandomEvent(customerId);
                messageProducer.sendMessage(usageEventsQueue,event);
            }
        }
        return ResponseEntity.ok(String.format("Simulated:%d events for:%d customers",events,customers));
    }

    private UsageEvent generateRandomEvent(String customerId) {
        String[] serviceTypes = {"INTERNET","LANDLINE","ENERGY"};
        String serviceType = serviceTypes[random.nextInt(serviceTypes.length)];

        UsageEvent usageEvent = new UsageEvent();
        usageEvent.setCustomerId(customerId);
        usageEvent.setServiceType(serviceType);
        usageEvent.setTimestamp(System.currentTimeMillis());

        switch (serviceType) {
            case "INTERNET":
                usageEvent.setEventType("USAGE");
                usageEvent.setAmount(formatToTwoDecimals(random.nextDouble(MAX_INTERNET_PER_EVENT)));
                usageEvent.setUnit("GB");
                break;
            case "LANDLINE":
                usageEvent.setEventType("USAGE");
                usageEvent.setAmount(formatToTwoDecimals(random.nextDouble(MAX_LANDLINE_PER_EVENT)));
                usageEvent.setUnit("MINUTES");
                break;
            case "ENERGY":
                usageEvent.setEventType("METER_READ");
                usageEvent.setAmount(formatToTwoDecimals(random.nextDouble(MAX_ENERGY_PER_EVENT)));
                usageEvent.setUnit("KWH");
                break;
        }
        LOGGER.info("Created a Usage Event ! Event:{}",usageEvent.toString());
        return usageEvent;
    }

    private double formatToTwoDecimals(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

}
