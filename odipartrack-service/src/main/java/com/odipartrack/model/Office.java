package com.odipartrack.model;

public class Office {
    
    private int id;
    private String ubigeo;
    private int capacity;
    private double latitude;
    private double longitude;
    private String region;
    private String department;
    private String province;  
    
    public Office(int id, String ubigeo, String department, String province, double latitude, double longitude, String region, int capacity) {
        this.id = id;
        this.ubigeo = ubigeo;
        this.department = department;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
        this.capacity = capacity;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}