package com.ashgan.bezeqDemo.Service;

import com.ashgan.bezeqDemo.Client.SOAP.BillingClient;
import com.ashgan.bezeqDemo.Model.UsageSummary;
import com.ashgan.bezeqDemo.soap.BillCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class BillingScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingScheduler.class);

    private final UsageService usageService;
    private final BillingClient billingClient;

    @Value("${billing.internet.rate-per-gb}")
    private double internetRatePerGB;
    @Value("${billing.landline.rate-per-minute}")
    private double landLineRatePerMinute;
    @Value("${billing.energy.rate-per-kwh}")
    private double energyRatePerKWH;

    public BillingScheduler(UsageService usageService, BillingClient billingClient) {
        this.usageService = usageService;
        this.billingClient = billingClient;
    }

    @Scheduled(fixedDelayString = "${billing.schedule.interval}")
    public void processBilling() {
        LOGGER.info("Starting periodic billing process");

        Set<String> customerIds = usageService.getAllCustomerIds();

        for (String customerId : customerIds) {
            try {
                billCustomer(customerId);
            } catch (Exception e) {
                LOGGER.error("Failed to bill Customer:{}", customerId);
            }
        }
    }

    private void billCustomer(String customerId) {
        UsageSummary usageSummary = usageService.getSummary(customerId);

        if (usageSummary.getInternetUsageGB()>0) {
            double amount = usageSummary.getInternetUsageGB() * internetRatePerGB;
            boolean success = sendBill(customerId,"INTERNET",amount);
            if (success) {
                usageSummary.setInternetUsageGB(0);
            }
        }

        if (usageSummary.getLandlineMinutes()>0) {
            double amount = usageSummary.getLandlineMinutes() * landLineRatePerMinute;
            boolean success = sendBill(customerId,"LANDLINE",amount);
            if (success) {
                usageSummary.setLandlineMinutes(0);
            }
        }

        if (usageSummary.getEnergyKWH()>0) {
            double amount = usageSummary.getEnergyKWH() * energyRatePerKWH;
            boolean sucesss = sendBill(customerId,"ENERGY",amount);
            if(sucesss) {
                usageSummary.setEnergyKWH(0);
            }
        }
    }

    private boolean sendBill(String customerId, String serviceType, double amount) {
        BillCustomerResponse response = billingClient.sendCustomerBill(customerId,serviceType,amount);

        if (response.isSuccess()) {
            LOGGER.info("Successfully Billed customer:{} for:{} of:{}", customerId,amount,serviceType);
            return true;
        } else {
            LOGGER.error("Billing failed for customer:{} for:{} of:{}. errorCode:{} , errorMessage:{}", customerId,amount,serviceType,response.getErrorCode(),response.getErrorMessage());
            return false;
        }
    }
}
