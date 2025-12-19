package com.ashgan.bezeqDemo.Soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "customerId",
    "serviceType",
    "eventType",
    "timestamp",
    "amount",
    "unit"
})
@XmlRootElement(name = "UsageEventRequest")
public class UsageEventRequest {

    @XmlElement(required = true)
    private String customerId;

    @XmlElement(required = true)
    private String serviceType;

    @XmlElement(required = true)
    private String eventType;

    @XmlElement(required = true)
    private long timestamp;

    @XmlElement(required = true)
    private double amount;

    @XmlElement(required = true)
    private String unit;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

