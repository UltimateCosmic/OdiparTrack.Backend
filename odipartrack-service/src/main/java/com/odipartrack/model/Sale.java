package com.odipartrack.model;

import java.time.LocalDateTime;

public class Sale {

    private int id;
    private int idOrigin;
    private Office origin;
    private int idDestination;
    private Office destination;
    private int quantity;
    private String clientId;  
    private int idEnvio;
    private Envio envio;
    private int idCamion;
    private Camion camion;
    private LocalDateTime dateTime;

    public Sale() {
    }

    public Sale(LocalDateTime dateTime, Office origin, Office destination, int quantity, String clientId) {
        this.dateTime = dateTime;
        this.origin = origin;
        this.destination = destination;
        this.quantity = quantity;
        this.clientId = clientId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrigin() {
        return idOrigin;
    }

    public void setIdOrigin(int idOrigin) {
        this.idOrigin = idOrigin;
    }

    public int getIdDestination() {
        return idDestination;
    }

    public void setIdDestination(int idDestination) {
        this.idDestination = idDestination;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }

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

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }
}
