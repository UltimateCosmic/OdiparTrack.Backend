DROP TABLE IF EXISTS `Camion`;

CREATE TABLE `Camion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Codigo` varchar(50) DEFAULT NULL,
  `idOrigen` int DEFAULT NULL,
  `Capacidad` int DEFAULT NULL,
  `Tiempo_nuevo_envio` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idOrigen` (`idOrigen`),
  CONSTRAINT `Camion_ibfk_1` FOREIGN KEY (`idOrigen`) REFERENCES `Oficina` (`id`)
);