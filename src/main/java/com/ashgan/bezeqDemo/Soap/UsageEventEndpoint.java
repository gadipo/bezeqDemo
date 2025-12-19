package com.ashgan.bezeqDemo.Soap;

import com.ashgan.bezeqDemo.Model.UsageEvent;
import com.ashgan.bezeqDemo.Service.UsageService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class UsageEventEndpoint {

    private static final String NAMESPACE_URI = "http://bezeqDemo.ashgan.com/soap";

    private final UsageService usageService;

    public UsageEventEndpoint(UsageService usageService) {
        this.usageService = usageService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UsageEventRequest")
    @ResponsePayload
    public UsageEventResponse processUsageEvent(@RequestPayload UsageEventRequest request) {
        UsageEvent event = new UsageEvent(
            request.getCustomerId(),
            request.getServiceType(),
            request.getEventType(),
            request.getTimestamp(),
            request.getAmount(),
            request.getUnit()
        );

        usageService.processEvent(event);

        UsageEventResponse response = new UsageEventResponse();
        response.setStatus("PROCESSED");
        response.setCustomerId(request.getCustomerId());
        response.setMessage("Usage event processed successfully");

        return response;
    }
}

