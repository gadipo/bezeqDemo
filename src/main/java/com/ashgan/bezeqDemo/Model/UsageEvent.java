package com.ashgan.bezeqDemo.Model;

import java.util.Objects;

public class UsageEvent {

    private String customerId;
    private String serviceType;
    private String eventType;
    private long timestamp;
    private double amount;
    private String unit;

    public UsageEvent() {
    }

    public UsageEvent(String customerId, String serviceType, String eventType, long timestamp, double amount, String unit) {
        this.customerId = customerId;
        this.serviceType = serviceType;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.amount = amount;
        this.unit = unit;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "UsageEvent{" +
                "customerId='" + customerId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsageEvent that = (UsageEvent) o;
        return timestamp == that.timestamp && Double.compare(amount, that.amount) == 0 && Objects.equals(customerId, that.customerId) && Objects.equals(serviceType, that.serviceType) && Objects.equals(eventType, that.eventType) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, serviceType, eventType, timestamp, amount, unit);
    }
}
