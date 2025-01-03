package com.odipartrack.controller;

import com.odipartrack.model.Sale;
import com.odipartrack.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     * Endpoint para obtener todos los pedidos.
     *
     * @return Lista de pedidos.
     */
    @GetMapping("/leerPorFecha")
    public ResponseEntity<List<Sale>> obtenerPedidosPorFecha(@RequestParam("fechaHoraInicio") String fechaHoraInicio) {
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaHoraInicio);
        List<Sale> pedidos = saleService.obtenerPedidosPorFecha(fechaInicio);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/leerPorFechaConIntervalo")
    public ResponseEntity<List<Sale>> obtenerPedidosPorFechaConIntervalo(@RequestParam("fechaHoraInicio") String fechaHoraInicio, @RequestParam("intervalo") String intervalo) {
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaHoraInicio);
        List<Sale> pedidos = saleService.obtenerPedidosPorFechaConIntervalo(fechaInicio, Integer.parseInt(intervalo));        
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/leer")
    public ResponseEntity<List<Sale>> obtenerPedidos() {
        List<Sale> pedidos = saleService.obtenerPedidos();
        return ResponseEntity.ok(pedidos);
    }    

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarArchivos() {
        try {
            int totalRegistros = saleService.procesarArchivosPedidos();
            return ResponseEntity.ok("Procesamiento completo. Total de registros insertados: " + totalRegistros);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error durante el procesamiento: " + e.getMessage());
        }
    }
}
