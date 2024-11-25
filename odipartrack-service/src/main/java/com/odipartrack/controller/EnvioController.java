package com.odipartrack.controller;

import com.odipartrack.model.Envio;
import com.odipartrack.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    /**
     * Endpoint para obtener los envíos filtrados por fecha y hora.
     *
     * @param startDatetime Fecha y hora de inicio del rango.
     * @return Lista de envíos.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Envio>> obtenerEnviosPorFecha(@RequestParam("fecha") String startDatetime) {
        // Convertir el parámetro de la solicitud en un LocalDateTime
        LocalDateTime fechaHoraInicio = LocalDateTime.parse(startDatetime);

        // Llamar al servicio con la fecha y hora proporcionadas
        List<Envio> envios = envioService.obtenerEnviosPorFecha(fechaHoraInicio);
        return ResponseEntity.ok(envios);
    }
}

