package com.ashgan.bezeqDemo.Service;

import com.ashgan.bezeqDemo.Client.SOAP.BillingClient;
import com.ashgan.bezeqDemo.Model.UsageSummary;
import com.ashgan.bezeqDemo.Util.Util;
import com.ashgan.bezeqDemo.soap.BillCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BillingScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingScheduler.class);

    private final UsageService usageService;
    private final BillingClient billingClient;

    @Value("${billing.mobile.tier1.threshold}")
    private double tier1Threshold;
    @Value("${billing.mobile.tier1.price}")
    private double tier1Price;
    @Value("${billing.mobile.tier2.threshold}")
    private double tier2Threshold;
    @Value("${billing.mobile.tier2.price}")
    private double tier2Price;
    @Value("${billing.mobile.tier3.threshold}")
    private double tier3Threshold;
    @Value("${billing.mobile.tier3.price}")
    private double tier3Price;
    @Value("${billing.mobile.overage.rate}")
    private double overageRate;
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
        LOGGER.info("Checking if Customer:{} has any usage....",customerId);
        UsageSummary usageSummary = usageService.getSummary(customerId);

        // Bill mobile internet using tiered pricing
        if (usageSummary.getInternetUsageGB() > 0) {
            double gbUsage = usageSummary.getInternetUsageGB();
            double amount = calculateMobileBill(Util.formatToTwoDecimals(gbUsage));
            boolean success = sendBill(customerId, "MOBILE_INTERNET", Util.formatToTwoDecimals(amount));
            if (success) {
                usageSummary.setInternetUsageGB(0);
            }
        }

        if (usageSummary.getLandlineMinutes()>0) {
            double amount = usageSummary.getLandlineMinutes() * landLineRatePerMinute;
            boolean success = sendBill(customerId,"LANDLINE",Util.formatToTwoDecimals(amount));
            if (success) {
                usageSummary.setLandlineMinutes(0);
            }
        }

        if (usageSummary.getEnergyKWH()>0) {
            double amount = usageSummary.getEnergyKWH() * energyRatePerKWH;
            boolean sucesss = sendBill(customerId,"ENERGY",Util.formatToTwoDecimals(amount));
            if(sucesss) {
                usageSummary.setEnergyKWH(0);
            }
        }
    }

    private double calculateMobileBill(double gbUsage) {
        if (gbUsage <= tier1Threshold) {
            LOGGER.info("Mobile usage {} GB falls in Tier 1 (<= {} GB) - Price: {} NIS", gbUsage, tier1Threshold, tier1Price);
            return tier1Price;
        } else if (gbUsage <= tier2Threshold) {
            LOGGER.info("Mobile usage {} GB falls in Tier 2 (<= {} GB) - Price: {} NIS", gbUsage, tier2Threshold, tier2Price);
            return tier2Price;
        } else if (gbUsage <= tier3Threshold) {
            LOGGER.info("Mobile usage {} GB falls in Tier 3 (<= {} GB) - Price: {} NIS", gbUsage, tier3Threshold, tier3Price);
            return tier3Price;
        } else {
            // Usage exceeds tier3, charge tier3 price + overage per GB
            double overage = gbUsage - tier3Threshold;
            double overageCharge = overage * overageRate;
            double totalAmount = tier3Price + overageCharge;
            LOGGER.warn("Mobile usage {} GB exceeds Tier 3 ({} GB) - Overage: {} GB x {} NIS/GB = {} NIS. Total: {} NIS",
                gbUsage, tier3Threshold, Util.formatToTwoDecimals(overage), overageRate,
                Util.formatToTwoDecimals(overageCharge), Util.formatToTwoDecimals(totalAmount));
            return totalAmount;
        }
    }

    private boolean sendBill(String customerId, String serviceType, double amount) {
        LOGGER.info("Billing Scheduler is sending Customer:{} a {} Bill for {} NIS",customerId,serviceType,amount);

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
