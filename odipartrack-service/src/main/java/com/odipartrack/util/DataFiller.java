package com.odipartrack.util;

import com.odipartrack.model.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataFiller {

	public static void fillRouteData(List<Route> routes, List<Office> offices, List<Velocidad> velocidades) {
		// Crear un mapa de oficinas (UBIGEO -> Office)
		Map<String, Office> officeMap = offices.stream()
				.collect(Collectors.toMap(Office::getUbigeo, office -> office));

		// Crear un mapa de velocidades (ID -> Velocidad)
		Map<Integer, Velocidad> velocityMap = velocidades.stream()
				.collect(Collectors.toMap(Velocidad::getId, velocidad -> velocidad));

		// Completar los datos en las rutas
		for (Route route : routes) {
			// Llenar el origen
			if (officeMap.containsKey(route.getIdOrigin())) {
				route.setOrigin(officeMap.get(route.getIdOrigin()));
			} else {
				System.out.println("No se encontró oficina para idOrigin: " + route.getIdOrigin());
			}

			// Llenar el destino
			if (officeMap.containsKey(route.getIdDestination())) {
				route.setDestination(officeMap.get(route.getIdDestination()));
			} else {
				System.out.println("No se encontró oficina para idDestination: " + route.getIdDestination());
			}

			// Llenar la velocidad
			if (velocityMap.containsKey(route.getIdVelocity())) {
				route.setVelocity(velocityMap.get(route.getIdVelocity()));
			} else {
				System.out.println("No se encontró velocidad para idVelocity: " + route.getIdVelocity());
				route.setVelocity(new Velocidad(0, "Desconocido", "Desconocido", 0.0));
			}
		}
	}

	public static void fillBlockData(List<Block> blocks, List<Route> routes) {
		// Crear un mapa de rutas (ID -> Route) para acceso rápido
		Map<Integer, Route> routeMap = routes.stream().collect(Collectors.toMap(Route::getId, route -> route));

		// Completar los datos en los bloques
		for (Block block : blocks) {
			int routeId = block.getIdRoute();
			if (routeMap.containsKey(routeId)) {
				block.setRoute(routeMap.get(routeId));
			} else {
				System.out.println("No se encontró ruta para idRoute: " + routeId);
			}
		}
	}

	public static void fillSaleData(List<Sale> sales, List<Office> offices, List<Envio> envios, List<Camion> camiones) {
		// Crear mapas para acceso rápido
		Map<Integer, Office> officeMap = offices.stream().collect(Collectors.toMap(Office::getId, office -> office)); // Ahora usa el ID de la oficina
		Map<Integer, Envio> envioMap = envios.stream().collect(Collectors.toMap(Envio::getId, envio -> envio));
		Map<Integer, Camion> camionMap = camiones.stream().collect(Collectors.toMap(Camion::getId, camion -> camion));
	
		for (Sale sale : sales) {
			// Asignar oficinas (origin y destination) utilizando el ID
			if (officeMap.containsKey(sale.getIdOrigin())) {
				sale.setOrigin(officeMap.get(sale.getIdOrigin()));
			}
			if (officeMap.containsKey(sale.getIdDestination())) {
				sale.setDestination(officeMap.get(sale.getIdDestination()));
			}
	
			// Asignar envíos
			if (envioMap.containsKey(sale.getIdEnvio())) {
				sale.setEnvio(envioMap.get(sale.getIdEnvio()));
			}
	
			// Asignar camiones parcialmente
			if (camionMap.containsKey(sale.getIdCamion())) {
				sale.setCamion(camionMap.get(sale.getIdCamion())); // El camión puede estar incompleto
			}
		}
	}	

	public static void fillCamionData(List<Camion> camiones, List<Office> offices, List<Sale> sales) {
		// Crear un mapa de oficinas para acceso rápido
		Map<String, Office> officeMap = offices.stream().collect(Collectors.toMap(Office::getUbigeo, office -> office));

		// Agrupar pedidos por camión
		Map<Integer, List<Sale>> salesByCamion = sales.stream().collect(Collectors.groupingBy(Sale::getIdCamion));

		for (Camion camion : camiones) {
			// Asignar oficina de inicio
			if (officeMap.containsKey(camion.getIdInicio())) {
				camion.setInicio(officeMap.get(camion.getIdInicio()));
			}

			// Asignar pedidos
			if (salesByCamion.containsKey(camion.getId())) {
				camion.setPedidos(salesByCamion.get(camion.getId())); // Los pedidos pueden estar incompletos
			}
		}
	}

	public static void fillCamionRoutes(List<Camion> camiones, List<Route> routes) {
		// Crear un mapa de rutas basado en origen y destino para acceso rápido
		Map<String, Route> routeMap = routes.stream()
				.collect(Collectors.toMap(
						route -> route.getIdOrigin() + "-" + route.getIdDestination(),
						route -> route));

		for (Camion camion : camiones) {
			if (camion.getPedidos() != null) {
				// Buscar rutas asociadas a los pedidos
				List<Route> rutasCamion = camion.getPedidos().stream()
						.map(pedido -> routeMap.get(pedido.getIdOrigin() + "-" + pedido.getIdDestination()))
						.filter(route -> route != null) // Filtrar rutas existentes
						.collect(Collectors.toList());

				// Asignar rutas al camión
				camion.setRutas(rutasCamion);
			}
		}
	}
	
	public static void fillEnvioData(List<Envio> envios, List<Camion> camiones) {
		// Crear un mapa de camiones para acceso rápido
		Map<Integer, Camion> camionMap = camiones.stream().collect(Collectors.toMap(Camion::getId, camion -> camion));
	
		for (Envio envio : envios) {
			// Asignar camión parcialmente
			if (camionMap.containsKey(envio.getIdCamion())) {
				envio.setCamion(camionMap.get(envio.getIdCamion()));
			}
		}
	}	

	public static void completeSaleData(List<Sale> sales, List<Camion> camiones) {
		// Crear un mapa de camiones actualizados para acceso rápido
		Map<Integer, Camion> camionMap = camiones.stream()
												 .collect(Collectors.toMap(Camion::getId, camion -> camion));
	
		for (Sale sale : sales) {
			// Obtener el ID del camión asociado a la venta
			Integer camionId = sale.getIdCamion();
			if (camionId != null && camionMap.containsKey(camionId)) {
				// Asignar el camión completamente actualizado a la venta
				sale.setCamion(camionMap.get(camionId));
			} else {
				System.out.println("No se encontró un camión actualizado para el pedido con ID: " + sale.getId());
			}
		}
	}
	
}
