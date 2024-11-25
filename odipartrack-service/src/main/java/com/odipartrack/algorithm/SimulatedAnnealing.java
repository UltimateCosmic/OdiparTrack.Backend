
package com.odipartrack.algorithm;

import com.odipartrack.model.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SimulatedAnnealing {

    private List<Route> routes; // Lista de todas las rutas posibles
    private List<Sale> sales; // Lista de todas las ventas a realizar
    private List<Office> offices; // Lista de todas las ventas a realizar
    private double temperature; // Temperatura inicial
    private double coolingRate; // Tasa de enfriamiento
    private int maxIterations; // Número máximo de iteraciones
    private Random random; // Generador de números aleatorios
    private Map<Office, List<Route>> graph; // Grafo representado como una lista de adyacencia
    private List<Envio> envios; // Lista de envíos
    private List<Velocidad> velocidades; // Lista de velocidades
    private List<Block> bloqueos;
    private double bestFitness_;
    private double promedioFitness;
    private double cantEnvios;
    private List<Envio> proxEnvios; // Lista de proxEnvíos
    private List<Camion> camiones; // Lista de camiones

    // Constructor
    public SimulatedAnnealing(List<Sale> sales, List<Route> routes, double temperature, double coolingRate,
            int maxIterations, List<Office> offices, List<Velocidad> velocidades, List<Block> bloqueos,
            List<Camion> camiones, List<Envio> proxEnvios) {
        this.sales = sales;
        this.routes = routes;
        this.offices = offices;
        this.temperature = temperature;
        this.coolingRate = coolingRate;
        this.maxIterations = maxIterations;
        this.random = new Random();
        this.graph = buildGraph(routes); // Construir el grafo desde las rutas        
        this.velocidades = velocidades;
        this.envios = envios;
        this.bloqueos = bloqueos;
        this.camiones = camiones;
        this.proxEnvios = proxEnvios;
    }

    private Map<Office, List<Route>> buildGraph(List<Route> routes) {
        Map<Office, List<Route>> graph = new HashMap<>();
        for (Route route : routes) {
            if (route.getOrigin() == null || route.getDestination() == null) {
                System.out.println("Ruta inválida: Origen o destino son null");
                continue;
            }
            if (!offices.contains(route.getOrigin()) || !offices.contains(route.getDestination())) {
                System.out.println("Oficina no encontrada en el grafo: Origen=" + route.getOrigin().getUbigeo() +
                                   ", Destino=" + route.getDestination().getUbigeo());
            }
            graph.putIfAbsent(route.getOrigin(), new ArrayList<>());
            graph.get(route.getOrigin()).add(route);
        }
        return graph;
    }    

    public List<Envio> run() {
        double i = 0;
        loadCamiones();
        List<Envio> currentSolution = inicializarSolucion(camiones);
        double currentFitness = calculateFitness(currentSolution);
        double bestFitness = currentFitness;
        List<Envio> bestSolution = new ArrayList<>(currentSolution);

        System.out.println("Solución Inicial:");
        printSolution(currentSolution);
        System.out.println("Fitness de la Solución Inicial: " + currentFitness);

        while (temperature > 1) {

            List<Envio> neighborSolution = generateNeighbor(currentSolution, camiones);
            double neighborFitness = calculateFitness(neighborSolution);

            if (shouldAcceptSolution(currentFitness, neighborFitness)) {
                currentSolution = new ArrayList<>(neighborSolution);
                currentFitness = neighborFitness;
            }

            if (currentFitness > bestFitness) {
                bestSolution = new ArrayList<>(currentSolution);
                bestFitness = currentFitness;
            }

            // Enfriar la temperatura
            temperature -= coolingRate;

            // Imprimir progreso
            System.out.println("===============================================");
            System.out.println("Iteración: " + (i + 1));
            System.out.println("Temperatura: " + temperature);
            System.out.println("Fitness de la Solución Actual: " + neighborFitness);
            System.out.println("Mejor Fitness hasta Ahora: " + bestFitness);
            System.out.println("===============================================\n");
            i++;
        }

        System.out.println("Mejor Solución Encontrada:");
        printSolution(bestSolution);

        rellenarTiempoSalida(bestSolution, proxEnvios);

        // Insertar en la BD los envios (bestSolution)
        System.out.println("Fitness de la Mejor Solución: " + bestFitness);

        // Retornar la mejor solución encontrada
        return bestSolution;
    }

    public void rellenarTiempoSalida(List<Envio> bestSolution, List<Envio> proxEnvios) {
        // Crear un mapa con los códigos de camión y sus fechas de próxima salida
        Map<String, LocalDateTime> fechasSalida = new HashMap<>();
        for (Envio envio : proxEnvios) {
            if (envio.getCamion() != null && envio.getCamion().getCodigo() != null && envio.getTiempoSalida() != null) {
                fechasSalida.put(envio.getCamion().getCodigo(), envio.getTiempoSalida());
            }
        }

        // Asignar tiempos de salida a la lista de soluciones
        for (Envio envio : bestSolution) {
            if (envio.getCamion() != null && envio.getCamion().getSalida_minima() == null) {
                String codigoCamion = envio.getCamion().getCodigo();
                LocalDateTime fechaSalida = fechasSalida.get(codigoCamion);
                if (fechaSalida != null) {
                    envio.setTiempoSalida(fechaSalida);
                }
            }
        }
    }

    private void loadCamiones() {
        if (proxEnvios == null || camiones == null) {
            return;
        }

        // Crear un mapa de fechas de salida basado en proxEnvios
        Map<String, LocalDateTime> fechasSalida = generarMapaFechasSalida(proxEnvios);

        // Actualizar las fechas de salida de los camiones usando el mapa generado
        actualizarFechasSalida(camiones, fechasSalida);
    }

    private Map<String, LocalDateTime> generarMapaFechasSalida(List<Envio> proxEnvios) {
        // Generar un mapa donde la clave es el código del camión y el valor es la fecha
        // de próxima salida
        return proxEnvios.stream()
                .filter(envio -> envio.getCamion() != null && envio.getCamion().getCodigo() != null)
                .collect(Collectors.toMap(
                        envio -> envio.getCamion().getCodigo(),
                        Envio::getTiempoSalida));
    }

    private void actualizarFechasSalida(List<Camion> camiones, Map<String, LocalDateTime> fechasSalida) {
        // Actualizar la fecha de salida de cada camión en base al mapa de fechas
        for (Camion camion : camiones) {
            LocalDateTime fechaSalida = fechasSalida.get(camion.getCodigo());
            if (fechaSalida != null) {
                camion.setTiempo_nuevo_envio(fechaSalida);
            }
        }
    }

    private Office findOfficeByCode(List<Office> offices, String ubigeo) {
        for (Office office : offices) {
            if (office.getUbigeo().equals(ubigeo)) {
                return office;
            }
        }
        throw new IllegalArgumentException("Office with ubigeo " + ubigeo + " not found.");
    }

    // Función para inicializar la solución
    private List<Envio> inicializarSolucion(List<Camion> camiones) {
        List<Envio> envios = new ArrayList<>();
        Random random = new Random();
        int i = 0;

        // Inicializar envíos con camiones
        for (Camion camion : camiones) {
            Envio envio = new Envio(camion, null);
            envio.setCapacidadRestante(camion.getCapacidad());
            envios.add(envio);
        }

        // Distribuir las ventas entre los envíos
        for (Sale sale : sales) {
            i++;
            Envio envio = findOrCreateEnvio(envios, sale, camiones, i);

            if (envio == null) {
                System.out.println("Colapso logístico: No se encontró un envío adecuado para la venta.");
            } else {
                envio.setCapacidadRestante(envio.getCapacidadRestante() - sale.getQuantity());
                envio.setTiempoSalida(sale.getDateTime());
                envio.getCamion().getPedidos().add(sale);
            }
        }

        // Llamar a la función calcularCamiones después de la distribución de ventas
        calcularCamiones(envios, camiones);
        return envios;
    }

    // Función para encontrar o crear envíos
    private Envio findOrCreateEnvio(List<Envio> envios, Sale sale, List<Camion> camiones, int pedidonum) {
        // Calcular el tiempo límite del pedido
        LocalDateTime tiempoLimite = calcularTiempoLimite(sale.getDateTime(), sale.getDestination().getRegion());
        double a = 0;

        // Buscar el primer envío con tiempo de llegada menor al tiempo límite y con
        // capacidad suficiente
        for (Envio envio : envios) {
            if (envio.getTiempoLlegada() == null || envio.getTiempoLlegada().isBefore(tiempoLimite)) {
                // Calcular el tiempo de llegada estimado
                List<Sale> pedidos = envio.getCamion().getPedidos();
                Office origen = pedidos.isEmpty() ? envio.getCamion().getInicio()
                        : pedidos.get(pedidos.size() - 1).getDestination();
                LocalDateTime ultimoTiempo = sale.getDateTime();

                // Calcular el tiempo entre el destino del último pedido y el destino del nuevo
                // pedido
                Map.Entry<Double, List<Route>> result = calculateRouteDistance(origen, sale.getDestination(),
                        sale.getDateTime());
                double distancia = result.getKey();
                double velocidad = obtenerVelocidad(origen.getRegion(), sale.getDestination().getRegion());
                double tiempoLlegada = distancia / velocidad;

                // Verificar si el tramo ya está en la lista de rutas del camión
                boolean tramoExistente = envio.getCamion().getRutas().stream()
                        .anyMatch(ruta -> ruta.getOrigin().equals(origen)
                                && ruta.getDestination().equals(sale.getDestination()));

                // Acumular el tiempo de llegada en la demora del envío solo si el tramo no
                // existe
                double nuevaDemora = envio.getDemora();
                if (!tramoExistente) {
                    nuevaDemora += tiempoLlegada;
                }

                // Estimar la hora de llegada
                LocalDateTime tiempoLlegadaEstimado = safePlusHours(ultimoTiempo, (long) nuevaDemora);

                // Calcular el menor tiempo límite del primer pedido
                LocalDateTime menorTiempoLimite = tiempoLimite;
                if (!pedidos.isEmpty()) {
                    Sale primerPedido = pedidos.get(0);
                    LocalDateTime tiempoLimitePrimerPedido = safePlusHours(primerPedido.getDateTime(), (long) 24);
                    if (tiempoLimitePrimerPedido.isBefore(menorTiempoLimite)) {
                        menorTiempoLimite = tiempoLimitePrimerPedido;
                    }
                }

                // Verificar si llega antes del tiempo límite y tiene capacidad suficiente
                if (tiempoLlegadaEstimado.isBefore(menorTiempoLimite)
                        && envio.getCapacidadRestante() >= sale.getQuantity()) {
                    envio.setTiempoLlegada(tiempoLlegadaEstimado);
                    envio.setDemora(nuevaDemora); // Actualizar la demora
                    envio.getCamion().setFechaSalida(safePlusHours(tiempoLlegadaEstimado, (long) 2));
                    // Actualizar salida_minima
                    if (!tramoExistente) {
                        envio.getCamion().getDem_Pedidos().add(nuevaDemora);
                        envio.getCamion().getDist_Pedidos().add(distancia); // Agregar la nueva demora a la lista de
                                                                            // demoras por pedidos
                        envio.getCamion().getRutas().addAll(result.getValue()); // Agregar las rutas al camión
                    } else {
                        envio.getCamion().getDist_Pedidos().add(0.0);
                    }
                    Office lima = findOfficeByCode(offices, "040201");
                    Office trujillo = findOfficeByCode(offices, "130101");
                    Office arequipa = findOfficeByCode(offices, "040101");

                    Office destinoFinal = sale.getDestination();
                    Office mejorOficina = null;
                    double menorDistancia = Double.MAX_VALUE;

                    // Calcular la distancia a Lima
                    Map.Entry<Double, List<Route>> distanciaLima = calculateRouteDistance(destinoFinal, lima,
                            tiempoLlegadaEstimado);
                    if (distanciaLima.getKey() < menorDistancia) {
                        menorDistancia = distanciaLima.getKey();
                        mejorOficina = lima;
                    }

                    // Calcular la distancia a Trujillo
                    Map.Entry<Double, List<Route>> distanciaTrujillo = calculateRouteDistance(destinoFinal, trujillo,
                            tiempoLlegadaEstimado);
                    if (distanciaTrujillo.getKey() < menorDistancia) {
                        menorDistancia = distanciaTrujillo.getKey();
                        mejorOficina = trujillo;
                    }

                    // Calcular la distancia a Arequipa
                    Map.Entry<Double, List<Route>> distanciaArequipa = calculateRouteDistance(destinoFinal, arequipa,
                            tiempoLlegadaEstimado);
                    if (distanciaArequipa.getKey() < menorDistancia) {
                        menorDistancia = distanciaArequipa.getKey();
                        mejorOficina = arequipa;
                    }

                    // Actualizar el origen del camión con la mejor oficina encontrada
                    if (mejorOficina != null) {
                        envio.getCamion().setInicio(mejorOficina);
                    }
                    return envio;
                }
            }
        }

        // Si no se encuentra un envío adecuado, crear un nuevo envío
        for (Camion camion : camiones) {
            if (camion.getFechaSalida() == null || camion.getFechaSalida().isBefore(sale.getDateTime())) {
                if (camion.getCodigo().equals("A001")) {
                    pedidonum = 33;
                }
                Camion nuevoCamion = new Camion(camion.getCapacidad(), camion.getCodigo(), camion.getInicio(),
                        new ArrayList<>(), new ArrayList<>());
                Envio nuevoEnvio = new Envio(nuevoCamion, null);
                nuevoEnvio.setCapacidadRestante(nuevoCamion.getCapacidad());
                nuevoEnvio.setTiempoSalida(sale.getDateTime());

                // Calcular el tiempo de llegada estimado para el nuevo envío
                Map.Entry<Double, List<Route>> result = calculateRouteDistance(nuevoCamion.getInicio(),
                        sale.getDestination(), nuevoEnvio.getTiempoSalida());
                double distancia = result.getKey();
                double velocidad = obtenerVelocidad(nuevoCamion.getInicio().getRegion(),
                        sale.getDestination().getRegion());
                double tiempoLlegada = distancia / velocidad;
                double nuevaDemora = tiempoLlegada;
                LocalDateTime tiempoLlegadaEstimado = safePlusHours(sale.getDateTime(), (long) nuevaDemora);

                LocalDateTime tiempoLimitePrimerPedido = safePlusHours(sale.getDateTime(), (long) 24);

                // Verificar si llega antes del tiempo límite y tiene capacidad suficiente
                if (tiempoLlegadaEstimado.isBefore(tiempoLimitePrimerPedido)
                        && nuevoEnvio.getCapacidadRestante() >= sale.getQuantity()) {
                    // Asignar tiempos y demora al nuevo envío
                    nuevoEnvio.setTiempoLlegada(tiempoLlegadaEstimado);
                    nuevoEnvio.setDemora(nuevaDemora);
                    nuevoEnvio.getCamion().setFechaSalida(safePlusHours(tiempoLlegadaEstimado, (long) 2));
                    nuevoCamion.getDem_Pedidos().add(nuevaDemora); // Inicializar la lista de demoras con la nueva
                                                                   // demora
                    nuevoCamion.getDist_Pedidos().add(distancia); // Inicializar la lista de distancias con la nueva
                                                                  // demora
                    nuevoCamion.getRutas().addAll(result.getValue()); // Agregar las rutas al camión

                    envios.add(nuevoEnvio);
                    return nuevoEnvio;
                }
            }
        }

        // Si no se encuentra un camión adecuado, devolver null
        return null;
    }

    // Calcular el fitness de una solución (tiempo total de entrega de todas las
    // ventas)
    private LocalDateTime calcularTiempoLimite(LocalDateTime tiempoBase, String region) {
        switch (region) {
            case "COSTA":
                return safePlusHours(tiempoBase, (long) 24);
            case "SIERRA":
                return safePlusHours(tiempoBase, (long) 48);
            case "SELVA":
                return safePlusHours(tiempoBase, (long) 72);
            default:
                throw new IllegalArgumentException("Región desconocida: " + region);
        }
    }

    // Método para calcular la distancia mínima entre dos oficinas usando Dijkstra
    private Map.Entry<Double, List<Route>> calculateRouteDistance(Office origin, Office destination,
            LocalDateTime startDateTime) {
        Map<Office, Double> distances = new HashMap<>();
        Map<Office, Boolean> visited = new HashMap<>();
        Map<Office, Route> previousRoute = new HashMap<>();

        for (Office office : graph.keySet()) {
            distances.put(office, Double.MAX_VALUE);
            visited.put(office, false);
        }

        if (!graph.containsKey(origin) || !graph.containsKey(destination)) {
            System.out.println("Origen o destino no existen en el grafo.");
            return new AbstractMap.SimpleEntry<>(Double.MAX_VALUE, new ArrayList<>());
        }

        PriorityQueue<Office> queue = new PriorityQueue<>(Comparator.comparing(distances::get));
        distances.put(origin, 0.0);
        queue.add(origin);

        while (!queue.isEmpty()) {
            Office currentOffice = queue.poll();

            if (currentOffice.equals(destination)) {
                List<Route> path = new ArrayList<>();
                Office step = destination;
                while (previousRoute.containsKey(step)) {
                    Route route = previousRoute.get(step);
                    path.add(route);
                    step = route.getOrigin();
                }
                Collections.reverse(path);
                return new AbstractMap.SimpleEntry<>(distances.get(destination), path);
            }

            if (visited.get(currentOffice)) {
                continue;
            }

            visited.put(currentOffice, true);
            List<Route> neighbors = graph.get(currentOffice);

            if (neighbors == null) {
                continue;
            }

            for (Route route : neighbors) {
                Office neighbor = route.getDestination();
                if (visited.get(neighbor)) {
                    continue;
                }

                // Verificar si la ruta está bloqueada
                if (isRouteBlocked(route, startDateTime)) {
                    continue;
                }

                double newDist = distances.get(currentOffice) + route.getDistance();
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousRoute.put(neighbor, route);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println("Destino no alcanzable desde el origen.");
        return new AbstractMap.SimpleEntry<>(Double.MAX_VALUE, new ArrayList<>());
    }

    private boolean isRouteBlocked(Route route, LocalDateTime startDateTime) {
        for (Block block : bloqueos) {
            if (block.getRoute().getOrigin().getUbigeo().equals(route.getOrigin().getUbigeo())
                    && block.getRoute().getDestination().getUbigeo().equals(route.getDestination().getUbigeo())
                    && (startDateTime.isAfter(block.getStart()) || startDateTime.isEqual(block.getStart()))
                    && (startDateTime.isBefore(block.getEnd()) || startDateTime.isEqual(block.getEnd()))) {
                return true;
            }
        }
        return false;
    }

    private double obtenerVelocidad(String regionOrigen, String regionDestino) {
        // Normalizamos los nombres de las regiones para hacer las comparaciones más
        // robustas
        String regionOrigenNormalizada = regionOrigen.trim().substring(0, 1).toUpperCase()
                + regionOrigen.trim().substring(1).toLowerCase();
        String regionDestinoNormalizada = regionDestino.trim().substring(0, 1).toUpperCase()
                + regionDestino.trim().substring(1).toLowerCase();

        // Buscar la velocidad en la lista cargada
        for (Velocidad velocidad : velocidades) {
            // Comparamos las regiones de origen y destino en ambas direcciones (región 1 a
            // región 2 y viceversa)
            if ((velocidad.getRegion1().equals(regionOrigenNormalizada)
                    && velocidad.getRegion2().equals(regionDestinoNormalizada))
                    || (velocidad.getRegion1().equals(regionDestinoNormalizada)
                            && velocidad.getRegion2().equals(regionOrigenNormalizada))) {
                return velocidad.getVelocidad(); // Devolver la velocidad encontrada
            }
        }

        // Si no se encuentra una velocidad para las regiones dadas, devolver 60.0 por
        // defecto
        return 60.0;
    }

    // Manejar el caso en que no se encuentra un camión adecuado
    private void calcularCamiones(List<Envio> envios, List<Camion> camiones) {

        for (int i = camiones.size(); i < envios.size(); i++) {

            Envio envio = envios.get(i);
            Sale sale = envio.getCamion().getPedidos().isEmpty() ? null : envio.getCamion().getPedidos().get(0);

            if (sale != null) {
                Camion camionActual = camiones.stream()
                        .filter(camion -> camion.getCodigo().equals(envio.getCamion().getCodigo()))
                        .findFirst()
                        .orElse(null);
                Camion camion_final = camionActual;
                boolean camionAsignado = false;

                // Verificar si el camión actual cumple con las condiciones
                if ((camionActual.getFechaSalida() == null
                        || camionActual.getFechaSalida().isBefore(envio.getTiempoSalida()))
                        && camionActual.getCapacidad() >= envio.getCapacidadRestante()) {
                    camionActual.setFechaSalida(safePlusHours(envio.getTiempoLlegada(), (long) 2));
                    camion_final = camionActual;
                    camionAsignado = true;

                } else {

                    // Buscar otro camión adecuado
                    for (Camion camion : camiones) {
                        int sumaCantidadesPedidos = envio.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity)
                                .sum();
                        if ((camion.getFechaSalida() == null
                                || camion.getFechaSalida().isBefore(envio.getTiempoSalida()))
                                && camion.getCapacidad() >= sumaCantidadesPedidos) {

                            // Reemplazar los datos del camión
                            Camion nuevoCamion = new Camion(camion.getCapacidad(), camion.getCodigo(),
                                    camion.getInicio(), new ArrayList<>(), new ArrayList<>());
                            nuevoCamion.getDem_Pedidos().addAll(envio.getCamion().getDem_Pedidos());
                            nuevoCamion.getDist_Pedidos().addAll(envio.getCamion().getDist_Pedidos());
                            nuevoCamion.getPedidos().addAll(envio.getCamion().getPedidos());
                            nuevoCamion.getRutas().addAll(envio.getCamion().getRutas());

                            envio.setCamion(nuevoCamion);
                            envio.setCapacidadRestante(camion.getCapacidad() - sumaCantidadesPedidos);

                            // Iterar sobre los pedidos del nuevo camión
                            double tiempoTotal = 0.0;
                            Office origen = nuevoCamion.getInicio();
                            if (origen.equals(camion.getInicio())) {
                                // Sumar todos los valores de la lista dem_Pedidos del camión
                                tiempoTotal = camion.getDem_Pedidos().get(camion.getDem_Pedidos().size() - 1);
                                camion.setFechaSalida(safePlusHours(envio.getTiempoLlegada(), (long) 2));
                                camion_final = camion;
                            } else {
                                Sale primerPedido = envio.getCamion().getPedidos().get(0);
                                Map.Entry<Double, List<Route>> result = calculateRouteDistance(origen,
                                        primerPedido.getDestination(), envio.getTiempoSalida());
                                double distancia = result.getKey();
                                double velocidad = obtenerVelocidad(origen.getRegion(),
                                        primerPedido.getDestination().getRegion());
                                double tiempoLlegada = distancia / velocidad;

                                tiempoTotal += tiempoLlegada;
                                envio.getCamion().getDem_Pedidos().set(0, tiempoTotal);
                                envio.getCamion().getDist_Pedidos().set(0, distancia);
                                double distnacia_total = 0;
                                for (int j = 1; j < envio.getCamion().getPedidos().size(); j++) {
                                    Sale Prev = envio.getCamion().getPedidos().get(j);
                                    Sale pedido = envio.getCamion().getPedidos().get(j);
                                    distnacia_total = envio.getCamion().getDist_Pedidos().get(j);
                                    velocidad = obtenerVelocidad(Prev.getOrigin().getRegion(),
                                            pedido.getDestination().getRegion());
                                    tiempoLlegada = distancia / velocidad;
                                    tiempoTotal += tiempoLlegada;
                                    envio.getCamion().getDem_Pedidos().set(j, tiempoTotal);
                                    envio.getCamion().getDist_Pedidos().set(j, distancia);
                                }
                                // Calcular la distancia entre el origen del camión y el destino del primer
                                // pedido
                                LocalDateTime tiempoLlegadaEstimado = safePlusHours(envio.getTiempoSalida(),
                                        (long) tiempoTotal);
                                envio.setTiempoLlegada(tiempoLlegadaEstimado);
                                nuevoCamion.setFechaSalida(safePlusHours(tiempoLlegadaEstimado, (long) 2));
                                camion.setFechaSalida(safePlusHours(tiempoLlegadaEstimado, (long) 2));
                                envio.setDemora(tiempoTotal);
                                camion_final = camion;
                            }

                            // Actualizar tiempoLlegada del envío y FechaSalida del nuevo camión
                            camionAsignado = true;

                            break;
                        }
                    }
                }

                // Si no se pudo asignar un camión, manejar el caso aquí (opcional)
                if (!camionAsignado) {
                    // Manejar el caso en que no se encuentra un camión adecuado
                    System.out.println("Colapso logístico: No se encontró un camión adecuado para el envío.");
                } else {
                    // Calcular la menor distancia para el camión después de entregar la ruta
                    Office lima = findOfficeByCode(offices, "040201");
                    Office trujillo = findOfficeByCode(offices, "130101");
                    Office arequipa = findOfficeByCode(offices, "040101");

                    Office destinoFinal = sale.getDestination();
                    Office mejorOficina = null;
                    double menorDistancia = Double.MAX_VALUE;

                    // Calcular la distancia a Lima
                    Map.Entry<Double, List<Route>> distanciaLima = calculateRouteDistance(destinoFinal, lima,
                            envio.getTiempoLlegada());
                    if (distanciaLima.getKey() < menorDistancia) {
                        menorDistancia = distanciaLima.getKey();
                        mejorOficina = lima;
                    }

                    // Calcular la distancia a Trujillo
                    Map.Entry<Double, List<Route>> distanciaTrujillo = calculateRouteDistance(destinoFinal, trujillo,
                            envio.getTiempoLlegada());
                    if (distanciaTrujillo.getKey() < menorDistancia) {
                        menorDistancia = distanciaTrujillo.getKey();
                        mejorOficina = trujillo;
                    }

                    // Calcular la distancia a Arequipa
                    Map.Entry<Double, List<Route>> distanciaArequipa = calculateRouteDistance(destinoFinal, arequipa,
                            envio.getTiempoLlegada());
                    if (distanciaArequipa.getKey() < menorDistancia) {
                        menorDistancia = distanciaArequipa.getKey();
                        mejorOficina = arequipa;
                    }

                    // Actualizar el origen del camión con la mejor oficina encontrada
                    if (mejorOficina != null) {
                        envio.getCamion().setInicio(mejorOficina);
                        camion_final.setInicio(mejorOficina);
                    }
                }
            }
        }
    }

    private double calculateFitness(List<Envio> envios) {
        double totalFitness = 0.0;
        double maxPenalty = Double.MAX_VALUE; // Valor máximo para penalizar diferencias negativas
        double f = 0;
        for (Envio envio : envios) {
            f++;
            List<Sale> pedidos = envio.getCamion().getPedidos();
            LocalDateTime tiempoSalida = envio.getTiempoSalida();
            List<Double> demoras = envio.getCamion().getDem_Pedidos();
            if (f == 20) {
                f++;
            }
            if (!pedidos.isEmpty()) {
                for (int i = 0; i < pedidos.size(); i++) {
                    Sale sale = pedidos.get(i);
                    LocalDateTime tiempoLimite = calcularTiempoLimite(sale.getDateTime(),
                            sale.getDestination().getRegion());
                    double demora = demoras.get(i);

                    // Verificar si la ruta ya está en la lista de rutas del camión
                    Office origen = (i == 0) ? envio.getCamion().getInicio() : pedidos.get(i - 1).getDestination();
                    boolean tramoExistente = envio.getCamion().getRutas().stream()
                            .anyMatch(ruta -> ruta.getOrigin().equals(origen)
                                    && ruta.getDestination().equals(sale.getDestination()));

                    double diferencia = 0;
                    if (tiempoSalida == null || envio.getTiempoLlegada() == null) {
                        diferencia = 0; // Si no hay tiempo de salida o de llegada, la diferencia es 0
                    } else {
                        if (tramoExistente) {
                            // Si el tramo ya existe, no considerar la demora
                            diferencia = 0;
                        } else {
                            // Si el tramo no existe, considerar la demora
                            diferencia = calcularDiferenciaEnHoras(tiempoSalida, tiempoLimite, demora);
                        }
                    }

                    if (diferencia < -1) {
                        totalFitness += maxPenalty; // Penalizar diferencias negativas
                    } else {
                        totalFitness += Math.max(0, diferencia);
                    }
                }
            }
        }
        if (totalFitness < 100000000000000.00) {
            totalFitness = totalFitness + 0.1;
        }
        return totalFitness;
    }

    private double calcularDiferenciaEnHoras(LocalDateTime tiempoSalida, LocalDateTime tiempoLimite,
            double tiempoDeDemoraEnHoras) {
        long horasHastaLimite = ChronoUnit.HOURS.between(tiempoSalida, tiempoLimite);
        return horasHastaLimite - tiempoDeDemoraEnHoras;
    }

    // Generar un vecino modificando la solución actual (intercambiar dos ventas
    // aleatoriamente)
    private List<Envio> generateNeighbor(List<Envio> currentSolution, List<Camion> camiones) {
        List<Envio> neighbor = deepCopyEnvios(currentSolution);
        Random random = new Random();

        // Realizar múltiples intercambios para diversificar más el vecino
        // Realiza 3 intercambios aleatorios
        int envioIndex1 = random.nextInt(neighbor.size());
        int envioIndex2 = random.nextInt(neighbor.size());
        while (envioIndex1 == envioIndex2) {
            envioIndex2 = random.nextInt(neighbor.size());
        }
        Envio envio1 = neighbor.get(envioIndex1);
        Envio envio2 = neighbor.get(envioIndex2);
        List<Sale> pedidos1 = envio1.getCamion().getPedidos();
        List<Sale> pedidos2 = envio2.getCamion().getPedidos();

        if (!pedidos1.isEmpty() && !pedidos2.isEmpty()) {
            int saleIndex1 = random.nextInt(pedidos1.size());
            int saleIndex2 = random.nextInt(pedidos2.size());
            Sale sale1 = pedidos1.get(saleIndex1);
            Sale sale2 = pedidos2.get(saleIndex2);

            // Calcular la capacidad restante y la capacidad total de los pedidos
            double capacidadRestante1 = envio1.getCamion().getCapacidad();
            double capacidadRestante2 = envio2.getCamion().getCapacidad();
            double totalPedidos1 = pedidos1.stream().mapToDouble(Sale::getQuantity).sum();
            double totalPedidos2 = pedidos2.stream().mapToDouble(Sale::getQuantity).sum();
            if (totalPedidos1 - sale1.getQuantity() + sale2.getQuantity() <= capacidadRestante1 &&
                    totalPedidos2 - sale2.getQuantity() + sale1.getQuantity() <= capacidadRestante2 &&
                    (sale2.getDateTime().isAfter(envio1.getCamion().getSalida_minima())
                            || sale2.getDateTime().isEqual(envio1.getCamion().getSalida_minima()))
                    &&
                    (sale1.getDateTime().isAfter(envio2.getCamion().getSalida_minima())
                            || sale1.getDateTime().isEqual(envio2.getCamion().getSalida_minima()))) {

                // Intercambiar ventas entre dos envíos
                Sale temp = pedidos1.get(saleIndex1);
                pedidos1.set(saleIndex1, pedidos2.get(saleIndex2));
                pedidos2.set(saleIndex2, temp);
                envio1.setCapacidadRestante(-envio1.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                        + envio1.getCamion().getCapacidad());
                envio2.setCapacidadRestante(-envio2.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                        + envio2.getCamion().getCapacidad());
                // Actualizar tiempoSalida de los envíos
                envio1.setTiempoSalida(findLatestDate(pedidos1));
                envio2.setTiempoSalida(findLatestDate(pedidos2));

                // Recalcular la demora y otros atributos
                recalcularDemora(envio1, currentSolution, camiones, envioIndex1);
                recalcularDemora(envio2, currentSolution, camiones, envioIndex2);
            }
        } else if (pedidos1.isEmpty() && !pedidos2.isEmpty()) {
            int saleIndex2 = random.nextInt(pedidos2.size());
            Sale sale2 = pedidos2.get(saleIndex2);

            // Calcular la capacidad restante y la capacidad total de los pedidos
            double capacidadRestante1 = envio1.getCamion().getCapacidad();
            double totalPedidos2 = pedidos2.stream().mapToDouble(Sale::getQuantity).sum();
            if (totalPedidos2 - sale2.getQuantity() <= capacidadRestante1) {
                if (envio1.getCamion().getSalida_minima() == null) {
                    // Mover venta de pedidos2 a pedidos1
                    pedidos1.add(sale2);
                    pedidos2.remove(saleIndex2);
                    envio1.setCapacidadRestante(
                            -envio1.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                    + envio1.getCamion().getCapacidad());
                    envio2.setCapacidadRestante(
                            -envio2.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                    + envio2.getCamion().getCapacidad());
                    // Actualizar tiempoSalida de los envíos
                    envio1.setTiempoSalida(findLatestDate(pedidos1));
                    envio2.setTiempoSalida(findLatestDate(pedidos2));

                    // Recalcular la demora y otros atributos
                    recalcularDemora(envio1, currentSolution, camiones, envioIndex1);
                    recalcularDemora(envio2, currentSolution, camiones, envioIndex2);
                } else {
                    if ((sale2.getDateTime().isAfter(envio1.getCamion().getSalida_minima())
                            || sale2.getDateTime().isEqual(envio1.getCamion().getSalida_minima()))) {
                        // Mover venta de pedidos2 a pedidos1
                        pedidos1.add(sale2);
                        pedidos2.remove(saleIndex2);
                        envio1.setCapacidadRestante(
                                -envio1.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                        + envio1.getCamion().getCapacidad());
                        envio2.setCapacidadRestante(
                                -envio2.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                        + envio2.getCamion().getCapacidad());
                        // Actualizar tiempoSalida de los envíos
                        envio1.setTiempoSalida(findLatestDate(pedidos1));
                        envio2.setTiempoSalida(findLatestDate(pedidos2));

                        // Recalcular la demora y otros atributos
                        recalcularDemora(envio1, currentSolution, camiones, envioIndex1);
                        recalcularDemora(envio2, currentSolution, camiones, envioIndex2);
                    }
                }
            }
        } else if (!pedidos1.isEmpty() && pedidos2.isEmpty()) {

            int saleIndex1 = random.nextInt(pedidos1.size());
            Sale sale1 = pedidos1.get(saleIndex1);

            // Calcular la capacidad restante y la capacidad total de los pedidos
            double capacidadRestante2 = envio2.getCamion().getCapacidad();
            double totalPedidos1 = pedidos1.stream().mapToDouble(Sale::getQuantity).sum();
            if (totalPedidos1 - sale1.getQuantity() <= capacidadRestante2) {
                if (envio2.getCamion().getSalida_minima() == null) {
                    // Mover venta de pedidos1 a pedidos2
                    pedidos2.add(sale1);
                    pedidos1.remove(saleIndex1);
                    envio1.setCapacidadRestante(
                            -envio1.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                    + envio1.getCamion().getCapacidad());
                    envio2.setCapacidadRestante(
                            -envio2.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                    + envio2.getCamion().getCapacidad());
                    // Actualizar tiempoSalida de los envíos
                    envio1.setTiempoSalida(findLatestDate(pedidos1));
                    envio2.setTiempoSalida(findLatestDate(pedidos2));

                    // Recalcular la demora y otros atributos
                    recalcularDemora(envio1, currentSolution, camiones, envioIndex1);
                    recalcularDemora(envio2, currentSolution, camiones, envioIndex2);
                } else {
                    if ((sale1.getDateTime().isAfter(envio2.getCamion().getSalida_minima())
                            || sale1.getDateTime().isEqual(envio2.getCamion().getSalida_minima()))) {
                        // Mover venta de pedidos1 a pedidos2
                        pedidos2.add(sale1);
                        pedidos1.remove(saleIndex1);
                        envio1.setCapacidadRestante(
                                -envio1.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                        + envio1.getCamion().getCapacidad());
                        envio2.setCapacidadRestante(
                                -envio2.getCamion().getPedidos().stream().mapToInt(Sale::getQuantity).sum()
                                        + envio2.getCamion().getCapacidad());
                        // Actualizar tiempoSalida de los envíos
                        envio1.setTiempoSalida(findLatestDate(pedidos1));
                        envio2.setTiempoSalida(findLatestDate(pedidos2));

                        // Recalcular la demora y otros atributos
                        recalcularDemora(envio1, currentSolution, camiones, envioIndex1);
                        recalcularDemora(envio2, currentSolution, camiones, envioIndex2);
                    }
                }
            }
        }

        return neighbor;
    }

    private List<Envio> deepCopyEnvios(List<Envio> original) {
        List<Envio> copy = new ArrayList<>();
        for (Envio envio : original) {
            Camion originalCamion = envio.getCamion();
            List<Sale> pedidosCopy = new ArrayList<>();
            for (Sale sale : originalCamion.getPedidos()) {
                pedidosCopy.add(new Sale(sale.getDateTime(), sale.getOrigin(), sale.getDestination(),
                        sale.getQuantity(), sale.getClientId()));
            }
            Camion camionCopy = new Camion(originalCamion.getCapacidad(), originalCamion.getCodigo(),
                    originalCamion.getInicio(), new ArrayList<>(originalCamion.getRutas()), pedidosCopy);
            Envio envioCopy = new Envio(camionCopy, envio.getTiempoSalida());
            envioCopy.setTiempoLlegada(envio.getTiempoLlegada());
            envioCopy.setDemora(envio.getDemora());
            envioCopy.setCapacidadRestante(envio.getCapacidadRestante());
            envioCopy.getCamion().getDem_Pedidos().addAll(envio.getCamion().getDem_Pedidos());
            envioCopy.getCamion().getDist_Pedidos().addAll(envio.getCamion().getDist_Pedidos());
            envioCopy.getCamion().setFechaSalida(envio.getCamion().getSalida_minima());
            copy.add(envioCopy);
        }
        return copy;
    }

    // Función para encontrar la fecha más lejana en una lista de pedidos
    private LocalDateTime findLatestDate(List<Sale> pedidos) {
        LocalDateTime latestDate = null;
        for (Sale sale : pedidos) {
            if (latestDate == null || sale.getDateTime().isAfter(latestDate)) {
                latestDate = sale.getDateTime();
            }
        }
        return latestDate;
    }

    private void recalcularDemora(Envio envio, List<Envio> envios, List<Camion> camiones, int envioIndex) {
        Camion camion = envio.getCamion();
        List<Sale> pedidos = camion.getPedidos();
        List<Double> dem_Pedidos = camion.getDem_Pedidos();
        List<Double> dist_Pedidos = camion.getDist_Pedidos();

        dem_Pedidos.clear();
        dist_Pedidos.clear();
        if (pedidos.isEmpty()) {
            envio.setTiempoSalida(null);
            envio.setTiempoLlegada(null);
            envio.setDemora(0.0);
            camion.getRutas().clear();
            camion.setSalida_minima(null);
            return;
        }
        double demoraAcumulada = 0.0;
        Office origen = camion.getInicio();
        LocalDateTime tiempoSalida = findLatestDate(pedidos);
        camion.getRutas().clear();
        for (Sale pedido : pedidos) {
            Map.Entry<Double, List<Route>> result = calculateRouteDistance(origen, pedido.getDestination(),
                    tiempoSalida);
            double distancia = result.getKey();
            List<Route> rutas = result.getValue();
            double velocidad = obtenerVelocidad(origen.getRegion(), pedido.getDestination().getRegion());
            double tiempoLlegada = distancia / velocidad;

            demoraAcumulada += tiempoLlegada;
            dem_Pedidos.add(demoraAcumulada);
            dist_Pedidos.add(distancia);
            camion.getRutas().addAll(rutas);
            origen = pedido.getDestination();
        }

        LocalDateTime tiempoLlegadaEstimado = safePlusHours(tiempoSalida, (long) demoraAcumulada);
        envio.setTiempoSalida(tiempoSalida);
        envio.setTiempoLlegada(tiempoLlegadaEstimado);
        envio.setDemora(demoraAcumulada);
        camion.setSalida_minima(safePlusHours(tiempoLlegadaEstimado, 2));
    }

    // Determinar si se debe aceptar la nueva solución
    private boolean shouldAcceptSolution(double currentFitness, double neighborFitness) {
        if (neighborFitness > currentFitness) {
            return true; // Aceptar si es una mejor solución
        }

        // Calcular probabilidad de aceptación basada en la temperatura
        double acceptanceProbability = Math.exp((currentFitness - neighborFitness) / temperature);

        // Aumentar la probabilidad de aceptación al inicio del algoritmo
        if (temperature > 100) { // Ajusta el umbral según la necesidad
            acceptanceProbability *= 1.5; // Incrementa la probabilidad de aceptar
        }

        return acceptanceProbability < random.nextDouble();
    }

    // Imprimir la solución (ventas con origen y destino)
    private void printSolution(List<Envio> solution) {
        int i = 0;
        System.out.println("Detalles de la Solución:");
        for (Envio envio : solution) {
            i++;
            System.out.printf("\n\nEnvio n0:%d", i);
            Camion camion = envio.getCamion();
            System.out.printf("\nCamión: %s | Capacidad Inicial: %d | Capacidad Restante: %d\n",
                    camion.getCodigo(), camion.getCapacidad(), envio.getCapacidadRestante());
            System.out.printf("Tiempo de Salida: %s | Tiempo de Llegada: %s\n",
                    envio.getTiempoSalida(), envio.getTiempoLlegada());

            // Imprimir las rutas totales del camión
            System.out.println("  Rutas Totales:");
            for (Route ruta : camion.getRutas()) {
                System.out.printf("    Ruta: %s -> %s | Distancia: %.2f km\n ",
                        ruta.getOrigin().getUbigeo(),
                        ruta.getDestination().getUbigeo(),
                        ruta.getDistance());
            }

            // Imprimir los detalles de cada venta
            List<Double> demoras = envio.getCamion().getDem_Pedidos();
            for (int j = 0; j < camion.getPedidos().size(); j++) {
                Sale sale = camion.getPedidos().get(j);
                System.out.printf(
                        "  Venta: %s -> %s | Cantidad: %d | Registro de pedido: %s  | Tiempo estimado de llegada: %s\n",
                        sale.getOrigin().getUbigeo(),
                        sale.getDestination().getUbigeo(),
                        sale.getQuantity(),
                        sale.getDateTime(),
                        safePlusHours(envio.getTiempoSalida(), (long) demoras.get(j).longValue()));
            }
        }
    }

    // Función para encontrar el envío con el menor valor de tiempoLlegada y
    // devolver CamionInfo
    private Camion findEnvioWithEarliestArrival(List<Envio> envios, int currentIndex, LocalDateTime tiempoSalida) {
        Envio earliestEnvio = null;
        LocalDateTime earliestArrival = null;
        Envio solucionEnvio = null;
        LocalDateTime tiempoSolucion = null;

        for (int i = 0; i < currentIndex; i++) {
            Envio envio = envios.get(i);
            if (earliestArrival == null
                    || (envio.getTiempoLlegada() != null && envio.getTiempoLlegada().isBefore(earliestArrival))) {
                earliestEnvio = envio;
                earliestArrival = envio.getTiempoLlegada();
            }
            if (envio.getTiempoLlegada() != null && envio.getTiempoLlegada().isBefore(tiempoSalida)) {
                solucionEnvio = envio;
                tiempoSolucion = envio.getTiempoLlegada();
            }
        }

        if (solucionEnvio == null) {
            // Actualizar el atributo de envio en el índice currentIndex
            envios.get(currentIndex).setTiempoSalida(earliestArrival);
            return earliestEnvio != null ? earliestEnvio.getCamion() : null;
        } else {
            return solucionEnvio.getCamion();
        }
    }

    public void manejarAveria(Envio envioAveriado) {
        Camion camionAveriado = envioAveriado.getCamion();

        // Marcar el camión como inutilizable
        camionAveriado.setUtilizable(false);
        System.out.println("El camión " + camionAveriado.getCodigo() + " ha sufrido una avería.");

        // Buscar un camión disponible
        Camion camionDisponible = findCamionDisponible(envioAveriado);

        double bestFitness = bestFitness_;

        if (camionDisponible != null) {
            // Asignar el camión disponible al envío afectado
            envioAveriado.setCamion(camionDisponible);
            System.out.println("El envío ha sido reasignado al camión: " + camionDisponible.getCodigo());

            // Recalcular el fitness después de la reasignación
            double nuevoFitness = calculateFitness(envios);
            System.out.println("Nuevo fitness después de la avería: " + nuevoFitness);

            // Comparar el nuevo fitness con el fitness anterior
            if (nuevoFitness > bestFitness) {
                bestFitness = nuevoFitness;
                System.out.println("La solución ha mejorado después de la avería.");
            } else {
                System.out.println("La solución ha empeorado después de la avería.");
            }
        } else {
            System.out.println("No hay camiones disponibles para reasignar el envío.");
        }
    }

    // Método para encontrar un nuevo camión disponible
    private Camion findCamionDisponible(Envio envioAveriado) {
        for (Envio envio : envios) {
            Camion camion = envio.getCamion();
            if (camion.isUtilizable() && camion.getCapacidad() >= envioAveriado.getCapacidadRestante()) {
                return camion;
            }
        }
        return null;
    }

    private LocalDateTime safePlusHours(LocalDateTime dateTime, long hoursToAdd) {
        // Limita el valor máximo para evitar EpochDay fuera de rango
        long maxHours = ChronoUnit.HOURS.between(dateTime, LocalDateTime.MAX);
        long safeHoursToAdd = Math.min(hoursToAdd, maxHours);
        return dateTime.plusHours(safeHoursToAdd);
    }   
}
