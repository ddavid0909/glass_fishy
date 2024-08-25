-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (x86_64)
--
-- Host: 127.0.0.1    Database: baza3
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pretplata`
--

DROP TABLE IF EXISTS `pretplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pretplata` (
  `idPre` int NOT NULL AUTO_INCREMENT,
  `IdKor` int NOT NULL,
  `IdPak` int NOT NULL,
  `Datum` date NOT NULL,
  `Vrijeme` time NOT NULL,
  `Cijena` float NOT NULL,
  PRIMARY KEY (`idPre`),
  KEY `FK_IdKor_pretplata_idx` (`IdKor`),
  KEY `FK_IdPak_pretplata_idx` (`IdPak`),
  CONSTRAINT `FK_IdKor_pretplata` FOREIGN KEY (`IdKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_IdPak_pretplata` FOREIGN KEY (`IdPak`) REFERENCES `paket` (`idpak`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pretplata`
--

LOCK TABLES `pretplata` WRITE;
/*!40000 ALTER TABLE `pretplata` DISABLE KEYS */;
INSERT INTO `pretplata` VALUES (1,1,2,'2021-01-01','12:53:07',2000),(2,2,2,'2020-05-01','02:23:27',1600),(3,2,2,'2020-06-01','22:17:36',1750),(4,3,5,'2019-11-07','17:23:37',1000),(5,3,7,'2023-12-15','12:57:47',4223),(6,4,8,'2023-12-01','14:25:46',5000),(7,4,8,'2024-01-01','17:28:59',5000),(8,5,3,'2024-01-01','14:00:07',3000),(9,6,4,'2024-01-01','12:59:56',2500),(10,7,6,'2024-01-01','22:22:22',3400),(11,10,4,'2024-01-23','23:34:49',40000);
/*!40000 ALTER TABLE `pretplata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-23 23:48:59
