package com.odipartrack.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Camion {

    private int id;
    private String codigo;
    private String idInicio;
    private Office inicio;
    private int capacidad;   
    private LocalDateTime tiempo_nuevo_envio;
    private List<Sale> pedidos;         // leerPedidos          (antes)     filtrar los que tengan el mismo codigo de camion
    private List<Route> rutas;          // leerRutasXPedidos    (antes)     filtrar las rutas que tengan alguno de los pedidos

    // Propio del Algoritmo
    private List<Double> dem_Pedidos;
    private List<Double> dist_Pedidos;
    private boolean utilizable;
    private boolean en_camino;
    private LocalDateTime salida_minima;

    public Camion() {
    }

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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdInicio() {
        return idInicio;
    }

    public LocalDateTime getTiempoNuevoEnvio() {
        return tiempo_nuevo_envio;
    }

    public void setTiempoNuevoEnvio(LocalDateTime tiempo_nuevo_envio) {
        this.tiempo_nuevo_envio = tiempo_nuevo_envio;
    }

    public void setIdInicio(String idInicio) {
        this.idInicio = idInicio;
    }

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

    public LocalDateTime getTiempo_nuevo_envio() {
        return tiempo_nuevo_envio;
    }

    public void setTiempo_nuevo_envio(LocalDateTime tiempo_nuevo_envio) {
        this.tiempo_nuevo_envio = tiempo_nuevo_envio;
    }
}
