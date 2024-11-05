package com.odipartrack.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Camion {

    private int capacidad;
    private String codigo;
    private Office inicio;
    private List<Route> rutas;
    private List<Sale> pedidos;
    private List<Double> dem_Pedidos;
    private List<Double> dist_Pedidos;
    private boolean utilizable;

    public List<Double> getDist_Pedidos() {
        return dist_Pedidos;
    }

    public void setDist_Pedidos(List<Double> dist_Pedidos) {
        this.dist_Pedidos = dist_Pedidos;
    }

    public boolean isEn_camino() {
        return en_camino;
    }

    public void setEn_camino(boolean en_camino) {
        this.en_camino = en_camino;
    }

    public LocalDateTime getSalida_minima() {
        return salida_minima;
    }

    public void setSalida_minima(LocalDateTime salida_minima) {
        this.salida_minima = salida_minima;
    }

    public List<Double> getDem_Pedidos() {
        return dem_Pedidos;
    }

    public void setDem_Pedidos(List<Double> dem_Pedidos) {
        this.dem_Pedidos = dem_Pedidos;
    }
    private boolean en_camino;
    private LocalDateTime salida_minima;

    // Constructor
    public Camion(int capacidad, String codigo, Office inicio, List<Route> rutas, List<Sale> pedidos) {
        this.capacidad = capacidad;
        this.codigo = codigo;
        this.inicio = inicio;
        this.rutas = rutas;
        this.pedidos = pedidos;
        this.en_camino = false;
        this.salida_minima = null;
        this.dem_Pedidos = new ArrayList<>();
        this.dist_Pedidos = new ArrayList<>();
        this.setUtilizable(true);
    }

    // Getters y Setters
    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Office getInicio() {
        return inicio;
    }

    public void setInicio(Office inicio) {
        this.inicio = inicio;
    }

    public List<Route> getRutas() {
        return rutas;
    }

    public void setRutas(List<Route> rutas) {
        this.rutas = rutas;
    }

    public List<Sale> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Sale> pedidos) {
        this.pedidos = pedidos;
    }

    public boolean isEnCamino() {
        return en_camino;
    }

    public void setEnCamino(boolean en_camino) {
        this.en_camino = en_camino;
    }

    public LocalDateTime getFechaSalida() {
        return salida_minima;
    }

    public void setFechaSalida(LocalDateTime fecha_salida) {
        this.salida_minima = fecha_salida;
    }

    public boolean isUtilizable() {
        return utilizable;
    }

    public void setUtilizable(boolean utilizable) {
        this.utilizable = utilizable;
    }
}