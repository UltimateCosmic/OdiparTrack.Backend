DROP TABLE IF EXISTS `Oficina`;

CREATE TABLE `Oficina` (
  `id` int NOT NULL AUTO_INCREMENT,
  `UBIGEO` varchar(6) NOT NULL,
  `Capacidad` int DEFAULT NULL,
  `Latitud` decimal(10,7) DEFAULT NULL,
  `Longitud` decimal(10,7) DEFAULT NULL,
  `Region` varchar(100) DEFAULT NULL,
  `Departamento` varchar(100) DEFAULT NULL,
  `Provincia` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UBIGEO` (`UBIGEO`)
);