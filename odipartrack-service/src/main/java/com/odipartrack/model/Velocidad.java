package com.odipartrack.model;

public class Velocidad {

    private int id;
    private String region1;
    private String region2;
    private double velocidad;

    // Constructor vacío
    public Velocidad() {
    }

    // Constructor completo
    public Velocidad(int id, String region1, String region2, double velocidad) {
        this.id = id;
        this.region1 = region1;
        this.region2 = region2;
        this.velocidad = velocidad;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion1() {
        return region1;
    }

    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    public String getRegion2() {
        return region2;
    }

    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return region1 + " - " + region2 + ": " + velocidad + " Km/h";
    }
}
