package com.ashgan.bezeqDemo.Model;

import java.util.Objects;

public class UsageSummary {

    private String customerId;
    private double internetUsageGB;
    private double landlineMinutes;
    private double energyKWH;
    private int outageCount;
    private int thresholdAlerts;

    public UsageSummary() {
    }

    public UsageSummary(String customerId) {
        this.customerId = customerId;
        this.internetUsageGB = 0.0;
        this.landlineMinutes = 0.0;
        this.energyKWH = 0.0;
        this.outageCount = 0;
        this.thresholdAlerts = 0;
    }

    public UsageSummary(String customerId, double internetUsageGB, double landlineMinutes, double energyKWH, int outageCount, int thresholdAlerts) {
        this.customerId = customerId;
        this.internetUsageGB = internetUsageGB;
        this.landlineMinutes = landlineMinutes;
        this.energyKWH = energyKWH;
        this.outageCount = outageCount;
        this.thresholdAlerts = thresholdAlerts;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getInternetUsageGB() {
        return internetUsageGB;
    }

    public double getLandlineMinutes() {
        return landlineMinutes;
    }

    public double getEnergyKWH() {
        return energyKWH;
    }

    public int getOutageCount() {
        return outageCount;
    }

    public int getThresholdAlerts() {
        return thresholdAlerts;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setInternetUsageGB(double internetUsageGB) {
        this.internetUsageGB = internetUsageGB;
    }

    public void setLandlineMinutes(double landlineMinutes) {
        this.landlineMinutes = landlineMinutes;
    }

    public void setEnergyKWH(double energyKWH) {
        this.energyKWH = energyKWH;
    }

    public void setOutageCount(int outageCount) {
        this.outageCount = outageCount;
    }

    public void setThresholdAlerts(int thresholdAlerts) {
        this.thresholdAlerts = thresholdAlerts;
    }

    @Override
    public String toString() {
        return "UsageSummary{" +
                "customerId='" + customerId + '\'' +
                ", internetUsageGB=" + internetUsageGB +
                ", landlineMinutes=" + landlineMinutes +
                ", energyKWH=" + energyKWH +
                ", outageCount=" + outageCount +
                ", thresholdAlerts=" + thresholdAlerts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsageSummary that = (UsageSummary) o;
        return Double.compare(internetUsageGB, that.internetUsageGB) == 0 && Double.compare(landlineMinutes, that.landlineMinutes) == 0 && Double.compare(energyKWH, that.energyKWH) == 0 && outageCount == that.outageCount && thresholdAlerts == that.thresholdAlerts && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, internetUsageGB, landlineMinutes, energyKWH, outageCount, thresholdAlerts);
    }
}
