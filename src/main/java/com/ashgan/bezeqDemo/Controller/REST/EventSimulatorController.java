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
@RequestMapping("api/eventSim")
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

    @PostMapping("/sim-batch/{customers}/{events}")
    public ResponseEntity<?> simulateBatchEvents(@RequestParam int customers, @RequestParam int events) {

        LOGGER.info("Simulating batch events: {} customers , {} events per customer",customers,events);

        for (int i=0;i<=customers;i++) {
            String customerId = "CUST" + i;
            for (int j=0;j<=events;j++) {
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
                usageEvent.setAmount(random.nextDouble(MAX_INTERNET_PER_EVENT));
                usageEvent.setUnit("GB");
                break;
            case "LANDLINE":
                usageEvent.setEventType("USAGE");
                usageEvent.setAmount(random.nextDouble(MAX_LANDLINE_PER_EVENT));
                usageEvent.setUnit("MINUTES");
                break;
            case "ENERGY":
                usageEvent.setEventType("METER_READ");
                usageEvent.setAmount(random.nextDouble(MAX_ENERGY_PER_EVENT));
                usageEvent.setUnit("KWH");
                break;
        }
        return usageEvent;
    }
}
