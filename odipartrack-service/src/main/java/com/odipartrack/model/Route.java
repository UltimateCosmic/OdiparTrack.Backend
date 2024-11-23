package com.odipartrack.model;

public class Route {

    private int id;
    private String idOrigin;
    private Office origin;
    private String idDestination;
    private Office destination;
    private double distance;
    private int idVelocity;
    private Velocidad velocity;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdOrigin() {
        return idOrigin;
    }

    public void setIdOrigin(String idOrigin) {
        this.idOrigin = idOrigin;
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

    public String getIdDestination() {
        return idDestination;
    }

    public void setIdDestination(String idDestination) {
        this.idDestination = idDestination;
    }

    public void setDestination(Office destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public Velocidad getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocidad velocity) {
        this.velocity = velocity;
    }

    public int getIdVelocity() {
        return idVelocity;
    }

    public void setIdVelocity(int idVelocity) {
        this.idVelocity = idVelocity;
    }
}
