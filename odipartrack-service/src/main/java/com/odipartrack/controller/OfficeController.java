package com.odipartrack.controller;

import com.odipartrack.model.Office;
import com.odipartrack.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/oficinas")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    /**
     * Endpoint para obtener todas las oficinas.
     *
     * @return Lista de oficinas.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Office>> obtenerOficinas() {
        List<Office> oficinas = officeService.obtenerOficinas();
        return ResponseEntity.ok(oficinas);
    }
}
