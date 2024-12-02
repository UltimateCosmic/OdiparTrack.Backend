DROP TABLE IF EXISTS `Pedidos`;

CREATE TABLE `Pedidos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idOrigen` int NOT NULL,
  `idDestino` int NOT NULL,
  `Cantidad` int NOT NULL,
  `Cliente` varchar(100) NOT NULL,
  `idEnvio` int DEFAULT NULL,
  `idCamion` int DEFAULT NULL,
  `Fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idEnvio` (`idEnvio`),
  KEY `idOrigen` (`idOrigen`),
  KEY `idCamion` (`idCamion`),
  KEY `idDestino` (`idDestino`),
  CONSTRAINT `Pedidos_ibfk_1` FOREIGN KEY (`idEnvio`) REFERENCES `Envio` (`id`),
  CONSTRAINT `Pedidos_ibfk_2` FOREIGN KEY (`idOrigen`) REFERENCES `Oficina` (`id`),
  CONSTRAINT `Pedidos_ibfk_3` FOREIGN KEY (`idCamion`) REFERENCES `Camion` (`id`),
  CONSTRAINT `Pedidos_ibfk_4` FOREIGN KEY (`idDestino`) REFERENCES `Oficina` (`id`)
);