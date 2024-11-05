package com.odipartrack.model;
import java.time.LocalDateTime;

public class Sale {
    private LocalDateTime dateTime;
    private Office origin;
    private Office destination;
    private int quantity;
    private String clientId;

    public Sale(LocalDateTime dateTime, Office origin, Office destination, int quantity, String clientId) {
        this.dateTime = dateTime;
        this.origin = origin;
        this.destination = destination;
        this.quantity = quantity;
        this.clientId = clientId;
    }

    // Getters y Setters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Office getOrigin() {
        return origin;
    }

    public void setOrigin(Office origin) {
        this.origin = origin;
    }

    public Office getDestination() {
        return destination;
    }

    public void setDestination(Office destination) {
        this.destination = destination;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
