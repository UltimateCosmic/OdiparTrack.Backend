package com.odipartrack.model;

import java.time.LocalDateTime;

public class Envio {
    private Camion camion;
    private LocalDateTime tiempoSalida;
    private int capacidadRestante; 
    private LocalDateTime tiempoLlegada;
    private double demora;
    private double solucion;

    public double getSolucion() {
        return solucion;
    }

    public void setSolucion(double solucion) {
        this.solucion = solucion;
    }

    // Constructor
    public Envio(Camion camion, LocalDateTime tiempoSalida) {
        this.camion = camion;
        this.tiempoSalida = tiempoSalida;
        this.demora =0;
    }

    public Envio(Camion camion, LocalDateTime tiempoSalida, int capacidadRestante, LocalDateTime tiempoLlegada) {
        this.camion = camion;
        this.tiempoSalida = tiempoSalida;
        this.capacidadRestante = capacidadRestante;
        this.tiempoLlegada = tiempoLlegada;
        this.demora =0;
    }

    // Getters y Setters
    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public LocalDateTime getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(LocalDateTime tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public int getCapacidadRestante() {
        return capacidadRestante;
    }

    public void setCapacidadRestante(int capacidadRestante) {
        this.capacidadRestante = capacidadRestante;
    }

    public LocalDateTime getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(LocalDateTime tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public double getDemora() {
        return demora;
    }

    public void setDemora(double demora) {
        this.demora = demora;
    }
}
