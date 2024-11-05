package com.odipartrack.model;

public class Route {
    private Office origin;
    private Office destination;
    private double distance;

    public Route(Office origin, Office destination, double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    // Getters y Setters
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
