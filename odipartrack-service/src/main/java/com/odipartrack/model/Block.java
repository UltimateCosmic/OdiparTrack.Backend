package com.odipartrack.model;

import java.time.LocalDateTime;

public class Block {
    private Route route;
    private LocalDateTime start;
    private LocalDateTime end;

    public Block(Route route, LocalDateTime start, LocalDateTime end) {
        this.route = route;
        this.start = start;
        this.end = end;
    }

    // Getters y Setters
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
