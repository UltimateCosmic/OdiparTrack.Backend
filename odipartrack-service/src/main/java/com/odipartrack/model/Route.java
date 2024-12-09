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
    private int time;

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

    public int getTime() {
        return time;
    }

    // Método para calcular el tiempo en segundos y truncarlo a 0 decimales
    public void calculateTime() {
        if (velocity != null && velocity.getVelocidad() > 0) {
            // Calcula el tiempo en horas y lo convierte a segundos (multiplicando por 3600)
            double timeInSeconds = (distance / velocity.getVelocidad()) * 3600;

            // Trunca el tiempo a 0 decimales (se convierte en un valor entero)
            this.time = (int) Math.floor(timeInSeconds); // Trunca y asigna como double
        } else {
            this.time = 0; // Si la velocidad es nula o no válida, el tiempo será 0
        }
    }
}
