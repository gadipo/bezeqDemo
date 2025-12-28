package com.ashgan.bezeqDemo.Controller.REST;

import com.ashgan.bezeqDemo.Model.UsageSummary;
import com.ashgan.bezeqDemo.Service.UsageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usage")
public class UsageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageController.class);

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @GetMapping("{customerId}/summary")
    public ResponseEntity<?> getCustomerSummary(@PathVariable Long customerId) {
        LOGGER.info("GET /api/usage/{}/summary - Get Customer Summary Request received", customerId);

        try {
            UsageSummary usageSummary = usageService.getSummary(String.valueOf(customerId));
            LOGGER.info("Found Summary for Customer:{}. Summary: {}", customerId,usageSummary.toString());
            return ResponseEntity.ok(usageSummary);
        } catch (Exception e) {
            LOGGER.error("Could not Retrieve Customer:{} Summary",customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Error occurred while retrieving Customer Summary");
        }
    }
}
