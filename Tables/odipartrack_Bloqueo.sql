DROP TABLE IF EXISTS `Bloqueo`;

CREATE TABLE `Bloqueo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Fecha_inicio` datetime DEFAULT NULL,
  `Fecha_fin` datetime DEFAULT NULL,
  `idRuta` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idRuta` (`idRuta`),
  CONSTRAINT `Bloqueo_ibfk_1` FOREIGN KEY (`idRuta`) REFERENCES `Ruta` (`id`)
);
