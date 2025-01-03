package com.odipartrack.controller;

import com.odipartrack.model.Camion;
import com.odipartrack.model.Envio;
import com.odipartrack.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    @Autowired
    private CamionService camionService;

    /**
     * Endpoint para obtener todos los camiones.
     *
     * @return Lista de camiones.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Camion>> obtenerCamiones() {        
        List<Camion> camiones = camionService.obtenerCamiones();
        return ResponseEntity.ok(camiones);
    }

    @PostMapping("/actualizarSalida")
    public void actualizarSalidaCamiones(@RequestBody List<Envio> envios) {
        camionService.actualizarSalidaCamiones(envios);
    }

    @PostMapping("/reiniciarCamiones")
    public void reiniciarCamiones() {
        camionService.reiniciarCamiones();
    }

}
