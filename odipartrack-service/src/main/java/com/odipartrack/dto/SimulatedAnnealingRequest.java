package com.odipartrack.dto;

import com.odipartrack.model.*;
import java.util.List;

public class SimulatedAnnealingRequest {
    private List<Sale> sales;
    private List<Route> routes;
    private List<Office> offices;
    private List<Velocidad> velocidades;
    private List<Block> bloqueos;
    private List<Envio> envios;

    // Constructor vacío
    public SimulatedAnnealingRequest() {}

    // Getters y setters
    public List<Sale> getSales() { return sales; }
    public void setSales(List<Sale> sales) { this.sales = sales; }

    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }

    public List<Office> getOffices() { return offices; }
    public void setOffices(List<Office> offices) { this.offices = offices; }

    public List<Velocidad> getVelocidades() { return velocidades; }
    public void setVelocidades(List<Velocidad> velocidades) { this.velocidades = velocidades; }

    public List<Block> getBloqueos() { return bloqueos; }
    public void setBloqueos(List<Block> bloqueos) { this.bloqueos = bloqueos; }

    public List<Envio> getEnvios() { return envios; }
    public void setEnvios(List<Envio> envios) { this.envios = envios; }
}
