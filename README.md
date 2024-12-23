# OdiparTrack.Backend

Este proyecto consta de dos backends: 

1. **OdiparTrack.Backend**: Una API RESTful construida con .NET para la gestión y rastreo de datos. Esta API incluye autenticación, operaciones CRUD y está diseñada para ser escalable.
2. **odipartrack-service**: Un servicio adicional que usa Spring Boot para manejar principalmente el algoritmo Simulated Annealing, construido con Java y Maven.

El backend OdiparTrack.Backend (ASP.NET) fue desarrollado con un enfoque más informal, lo que implica que su arquitectura no sigue completamente las mejores prácticas para un proyecto de esta magnitud. Si bien es adecuado para un entorno académico, no refleja las convenciones y estándares que se utilizan en un entorno productivo. Además, existen otros frameworks que ofrecen ventajas significativas en términos de escalabilidad, mantenimiento y eficiencia, pero debido a limitaciones de tiempo y presión, el equipo no tuvo la oportunidad de investigar otras alternativas. El uso de ASP.NET, tal como lo implementamos, fue elegido principalmente por su simplicidad y la facilidad de adaptación, dada su menor curva de aprendizaje.

A lo largo de nuestras discusiones, hemos considerado que lo más adecuado sería trabajar con un solo backend. Sin embargo, el desafío principal radica en la integración con la base de datos, que no es tan directa y debe gestionarse a través de Spring Boot. Para optimizar el desarrollo y aprovechar mejor las capacidades del sistema, sería necesario investigar y considerar el uso de un framework más adecuado para la gestión de bases de datos, lo cual podría mejorar la eficiencia y flexibilidad del proyecto en su conjunto.

## Recomendación de Frameworks

- Backend: Spring Boot (con Spring WebSocket para comunicación en tiempo real)
- Frontend: React.js (con React Leaflet o Mapbox para mapas y Socket.io para WebSockets)

Esta combinación de tecnologías proporciona una arquitectura flexible y eficiente, capaz de manejar la simulación y visualización de las entregas de manera interactiva y en tiempo real.

## Contexto del Proyecto

El proyecto busca resolver las necesidades de **OdiparPack**, una empresa peruana que se dedica a la venta corporativa por teléfono y la entrega de insumos (paquetes A) a nivel nacional. La empresa ha crecido considerablemente en términos de clientes y entregas, pero enfrenta desafíos en la gestión eficiente de sus entregas, planificación de rutas, y monitoreo en tiempo real de sus operaciones logísticas.

### Problemas que Resuelve:

1. **Registro de Ventas**: La empresa necesita registrar las ventas de los paquetes A vendidos a sus clientes.
2. **Planificación y Replanificación de Rutas**: La empresa debe planificar (y replanificar) las rutas de transporte de sus flotas, respetando los plazos de entrega establecidos para cada región:
   - Costa: 1 día
   - Sierra: 2 días
   - Selva: 3 días
3. **Monitoreo Visual de las Operaciones**: La solución también incluye un componente visualizador para monitorear las operaciones de la empresa, mostrando en un mapa el estado de cada paquete y su ubicación actual.
4. **Escenarios de Evaluación**: El proyecto se evalúa en 3 escenarios:
   - **Operaciones en Tiempo Real**: Visualización y monitoreo de las entregas en tiempo real.
   - **Simulación Semanal**: Simulación de la operación semanal, en la que se evalúa la entrega de los paquetes a lo largo de la semana. Este escenario debe completarse entre 20 y 50 minutos.
   - **Simulación hasta el Colapso Logístico**: Este escenario evalúa el colapso logístico de las operaciones, cuando no se puede cumplir con el plazo de entrega de al menos un paquete.

### Objetivo Principal:
El objetivo del proyecto es desarrollar una solución informática para cubrir las necesidades de planificación, monitoreo y simulación de las operaciones logísticas de OdiparPack. Para esto, el sistema debe:
1. **Registrar Ventas** de los paquetes.
2. **Planificar y Replanificar** los viajes de la flota utilizando algoritmos metaheurísticos, que permitan encontrar las mejores soluciones para cumplir con los plazos de entrega, tomando en cuenta las restricciones logísticas de cada región.
3. **Visualizar** gráficamente las operaciones en tiempo real, mostrando la ubicación de los paquetes en un mapa.

## Requisitos No Funcionales:
Para este proyecto, se establecen los siguientes requisitos no funcionales:
- Presentar dos soluciones **algorítmicas en Lenguaje Java**, evaluadas mediante diseño de experimentos (experimentación numérica).
- Las soluciones del **componente planificador** deben basarse en **algoritmos metaheurísticos**.
- La solución debe funcionar en el equipamiento disponible en el laboratorio de Ingeniería Informática.
- Se evaluará el proceso seguido utilizando **NTP-ISO/IEC 29110-5-1-2 (VSE)**.
- Se entregarán **videos** de la presentación final del equipo, así como avances sobre los tres escenarios requeridos.

## Requisitos Previos

Asegúrate de tener instalados los siguientes requisitos según el backend que vayas a ejecutar:

### Para OdiparTrack.Backend (.NET):

- [.NET SDK](https://dotnet.microsoft.com/download) (8.0)
- [Visual Studio](https://visualstudio.microsoft.com/) o [Visual Studio Code](https://code.visualstudio.com/)

### Para odipartrack-service (Spring Boot):

- [Java](https://adoptopenjdk.net/) (Versión 17 o superior)
- [Maven](https://maven.apache.org/) (Versión 3.8.1 o superior)

## Clonar el Repositorio

Primero, clona el repositorio en tu máquina local:

```bash
git clone https://github.com/UltimateCosmic/OdiparTrack.Backend.git
cd OdiparTrack.Backend
```

## Restaurar Dependencias

Antes de ejecutar el proyecto, necesitas restaurar las dependencias. En la terminal, ejecuta:

```bash
dotnet restore
```

## Para odipartrack-service (Spring Boot):

Dirígete a la carpeta del servicio odipartrack-service:

```bash
cd odipartrack-service
```

Luego, asegúrate de tener Maven instalado. Si no lo tienes, puedes instalarlo siguiendo las instrucciones de la documentación oficial de Maven. Después, ejecuta el siguiente comando para restaurar las dependencias del proyecto:

```bash
mvn clean install
```

## Ejecutar el Proyecto

### Ejecutar OdiparTrack.Backend (.NET):

Para iniciar el proyecto .NET, ejecuta el siguiente comando en la raíz del repositorio:

```bash
dotnet run
```

Esto iniciará el backend principal de .NET y podrás acceder a la API.

### Ejecutar odipartrack-service (Spring Boot):

Una vez que hayas restaurado las dependencias, puedes ejecutar el servicio de Spring Boot con el siguiente comando:

```bash
mvn spring-boot:run
```

Este comando iniciará el servicio odipartrack-service, el cual estará disponible y listo para manejar operaciones específicas en conjunto con el backend .NET.

## Otros Archivos Importantes

- **Tables**: Contiene los scripts .sql para crear las tablas necesarias en la base de datos.
- **API_Endpoints_EjemplosSolicitud.txt**: Ejemplos de solicitudes a la API.
- **LICENSE**: Información sobre la licencia del proyecto.
- **OdiparTrack.Backend.sln**: Solución del proyecto en Visual Studio.



