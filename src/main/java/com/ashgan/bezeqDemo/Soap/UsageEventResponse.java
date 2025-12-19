package com.ashgan.bezeqDemo.Soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "status",
    "customerId",
    "message"
})
@XmlRootElement(name = "UsageEventResponse")
public class UsageEventResponse {

    @XmlElement(required = true)
    private String status;

    @XmlElement(required = true)
    private String customerId;

    @XmlElement(required = true)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

