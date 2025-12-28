package com.ashgan.bezeqDemo.Client.SOAP;

import com.ashgan.bezeqDemo.soap.pbx.GetCallRecordRequest;
import com.ashgan.bezeqDemo.soap.pbx.GetCallRecordResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

public class PbxClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbxClient.class);

    private final WebServiceTemplate webServiceTemplate;

    public PbxClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public GetCallRecordResponse getCallRecord(String customerId, String phoneNumber) {
        try {
            GetCallRecordRequest request = new GetCallRecordRequest();
            request.setCustomerId(customerId);
            request.setPhoneNumber(phoneNumber);

            LOGGER.info("Sending SOAP request to PBX for customer ID:{} , Phone Number:{}",customerId,phoneNumber);

            GetCallRecordResponse response = (GetCallRecordResponse) webServiceTemplate.marshalSendAndReceive(request);

            LOGGER.info("Received SOAP response from PBX: duration={}s, destination={}, type={}", response.getDuration(),response.getDestination(),response.getCallType());

            return response;
        } catch (Exception e) {
            LOGGER.error("Failed to get call record from PBX for customer:{}",customerId, e);
            throw new RuntimeException("PBX SOAP call failed", e);
        }
    }

}
