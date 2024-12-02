DROP TABLE IF EXISTS `Ruta`;

CREATE TABLE `Ruta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idOrigen` varchar(6) DEFAULT NULL,
  `idDestino` varchar(6) DEFAULT NULL,
  `Distancia` decimal(10,2) DEFAULT NULL,
  `idVelocidad` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idOrigen` (`idOrigen`),
  KEY `idDestino` (`idDestino`),
  KEY `idVelocidad` (`idVelocidad`),
  CONSTRAINT `Ruta_ibfk_1` FOREIGN KEY (`idOrigen`) REFERENCES `Oficina` (`UBIGEO`),
  CONSTRAINT `Ruta_ibfk_2` FOREIGN KEY (`idDestino`) REFERENCES `Oficina` (`UBIGEO`),
  CONSTRAINT `Ruta_ibfk_3` FOREIGN KEY (`idVelocidad`) REFERENCES `Velocidad` (`id`) ON DELETE SET NULL
);
