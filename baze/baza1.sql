UNLOCK TABLES;
DROP DATABASE IF EXISTS `baza1`;
CREATE DATABASE `baza1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE baza1;

DROP TABLE IF EXISTS Mesto;
CREATE TABLE `Mesto` (
  `idMes` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`idMes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS korisnik;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL AUTO_INCREMENT,
  `ime` varchar(45) NOT NULL,
  `mejl` varchar(45) UNIQUE NOT NULL,
  `godiste` INT NOT NULL,
  `pol` varchar(1) NOT NULL,
  `idMes` int NOT NULL,
  PRIMARY KEY (`idKor`),
  KEY `FK_idMes_korisnik_idx` (`idMes`),
  CONSTRAINT `FK_idMes_korisnik` FOREIGN KEY (`idMes`) REFERENCES `Mesto` (`idMes`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES Mesto WRITE;
INSERT INTO Mesto(naziv) VALUES('Zvornik');
INSERT INTO Mesto(naziv) VALUES('Beograd');
INSERT INTO Mesto(naziv) VALUES('Sarajevo');
INSERT INTO Mesto(naziv) VALUES('Foca');
INSERT INTO Mesto(naziv) VALUES('London');
INSERT INTO Mesto(naziv) VALUES('Budimpesta');
INSERT INTO Mesto(naziv) VALUES('Zrenjanin');
UNLOCK TABLES;

LOCK TABLES Korisnik WRITE;
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('David', 'dd21@gmail.com', 2001, 'M', 1);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Mila', 'mp21@gmail.com', 2002, 'Z', 2);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Dragisa', 'ddd21@gmail.com', 1967, 'M', 1);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Sofija', 'sm21@gmail.com', 2002, 'Z', 5);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Kristina', 'kk21@gmail.com', 2002, 'Z', 6);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Stefan', 'ss22@gmail.com', 1999, 'M', 4);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Milan', 'mm21@gmail.com', 1998, 'M', 3);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Dragana', 'dd26@gmail.com', 2004, 'Z', 7);
INSERT INTO Korisnik(ime, mejl, godiste, pol, idMes) VALUES('Milenko', 'ff21@gmail.com', 2006, 'M', 4);
UNLOCK TABLES;