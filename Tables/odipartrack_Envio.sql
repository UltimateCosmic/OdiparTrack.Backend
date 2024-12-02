DROP TABLE IF EXISTS `Envio`;

CREATE TABLE `Envio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Tiempo_salida` datetime DEFAULT NULL,
  `Tiempo_llegada` datetime DEFAULT NULL,
  `Capacidad_restante` int DEFAULT NULL,
  `idCamion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idCamion` (`idCamion`),
  CONSTRAINT `Envio_ibfk_1` FOREIGN KEY (`idCamion`) REFERENCES `Camion` (`id`)
);