package com.odipartrack.controller;

import com.odipartrack.model.Envio;
import com.odipartrack.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    /**
     * Endpoint para obtener todos los envíos.
     *
     * @return Lista de envíos.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Envio>> obtenerEnvios() {
        List<Envio> envios = envioService.obtenerEnvios();
        return ResponseEntity.ok(envios);
    }
}
