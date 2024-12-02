DROP TABLE IF EXISTS `Velocidad`;

CREATE TABLE `Velocidad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `region_origen` varchar(50) DEFAULT NULL,
  `region_destino` varchar(50) DEFAULT NULL,
  `velocidad` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);