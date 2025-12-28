package com.ashgan.bezeqDemo.Client.SOAP;

import com.ashgan.bezeqDemo.Model.UsageSummary;
import com.ashgan.bezeqDemo.soap.BillCustomerRequest;
import com.ashgan.bezeqDemo.soap.BillCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

public class BillingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingClient.class);

    private final WebServiceTemplate webServiceTemplate;

    public BillingClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public BillCustomerResponse sendCustomerBill(String customerId, String serviceType, Double amount) {
        try {
            BillCustomerRequest request = new BillCustomerRequest();
            request.setCustomerId(customerId);
            request.setServiceType(serviceType);
            request.setAmount(amount);

            LOGGER.info("Sending Customer Bill SOAP Request to Legacy System for Customer:{} for service:{} for amount:{}", customerId, serviceType, amount);

            BillCustomerResponse response = (BillCustomerResponse) webServiceTemplate.marshalSendAndReceive(request);

            LOGGER.info("Received SOAP Response from Legacy Billing Service. Succes:{}, billId:{}", response.isSuccess(), response.getBillId());

            return response;
        } catch (Exception e) {
            LOGGER.error("Failed to send Customer Bill to Legacy System for Customer:{} for service:{} for amount:{}", customerId,serviceType,amount, e);
            throw new RuntimeException("Legacy Billing Service call failed", e);
        }
    };
}
