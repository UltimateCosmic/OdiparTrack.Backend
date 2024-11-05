package com.odipartrack.model;

public class Velocidad {
	private String region1;
    private String region2;
    private double velocidad;

    public Velocidad(String region1, String region2, double velocidad) {
        this.region1 = region1;
        this.region2 = region2;
        this.velocidad = velocidad;
    }

    public String getRegion1() {
        return region1;
    }

    public String getRegion2() {
        return region2;
    }

    public double getVelocidad() {
        return velocidad;
    }

    @Override
    public String toString() {
        return region1 + " - " + region2 + ": " + velocidad + " Km/h";
    }
}
