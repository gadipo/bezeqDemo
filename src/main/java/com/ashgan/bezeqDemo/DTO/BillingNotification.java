package com.ashgan.bezeqDemo.DTO;

import java.io.Serializable;
import java.util.Objects;

public class BillingNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerId;
    private String serviceType;
    private Double amount;
    private long timestamp;

    public BillingNotification() {
    }

    public BillingNotification(String customerId, String serviceType, Double amount) {
        this.customerId = customerId;
        this.serviceType = serviceType;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public Double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BillingNotification{" +
                "customerId='" + customerId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BillingNotification that = (BillingNotification) o;
        return timestamp == that.timestamp && Objects.equals(customerId, that.customerId) && Objects.equals(serviceType, that.serviceType) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, serviceType, amount, timestamp);
    }
}
