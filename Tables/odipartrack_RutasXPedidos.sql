DROP TABLE IF EXISTS `RutasXPedidos`;

CREATE TABLE `RutasXPedidos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idPedido` int NOT NULL,
  `idRuta` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idPedido` (`idPedido`),
  KEY `idRuta` (`idRuta`),
  CONSTRAINT `RutasXPedidos_ibfk_1` FOREIGN KEY (`idPedido`) REFERENCES `Pedidos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `RutasXPedidos_ibfk_2` FOREIGN KEY (`idRuta`) REFERENCES `Ruta` (`id`) ON DELETE CASCADE
);