UNLOCK TABLES;
DROP DATABASE IF EXISTS `baza3`;
CREATE DATABASE `baza3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE baza3;

DROP TABLE IF EXISTS korisnik;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL AUTO_INCREMENT,
  `ime` varchar(45) NOT NULL,
  `mejl` varchar(45) UNIQUE NOT NULL,
  `godiste` INT NOT NULL,
  `pol` varchar(1) NOT NULL,
  PRIMARY KEY (`idKor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS video;
CREATE TABLE `video` (
  `idVid` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `trajanje` double NOT NULL,
  `datum` date NOT NULL,
  `vrijeme` time NOT NULL,
  `idKor` int NOT NULL,
  PRIMARY KEY (`idVid`),
  KEY `FK_idKor_video_idx` (`idKor`),
  CONSTRAINT `FK_idKor_video` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS paket;
CREATE TABLE `paket` (
  `idpak` int NOT NULL AUTO_INCREMENT,
  `cijena` float NOT NULL,
  PRIMARY KEY (`idpak`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS pretplata;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS gledanje;
CREATE TABLE `gledanje` (
  `idGle` int NOT NULL AUTO_INCREMENT,
  `idVid` int NOT NULL,
  `idKor` int NOT NULL,
  `datum` date NOT NULL,
  `vrijeme` time NOT NULL,
  `pocetak` int NOT NULL DEFAULT '0',
  `trajanje` int NOT NULL,
  PRIMARY KEY (`idGle`),
  KEY `FK_idVid_gledanje_idx` (`idVid`),
  KEY `FK_idKor_gledanje_idx` (`idKor`),
  CONSTRAINT `FK_idKor_gledanje` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_idVid_gledanje` FOREIGN KEY (`idVid`) REFERENCES `video` (`idVid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS ocena;
CREATE TABLE `ocena` (
  `idKor` int NOT NULL,
  `idVid` int NOT NULL,
  `ocena` int NOT NULL,
  `datum` date NOT NULL,
  `vrijeme` time NOT NULL,
  PRIMARY KEY (`idKor`,`idVid`),
  KEY `FK_idVid_ocena_idx` (`idVid`),
  CONSTRAINT `FK_idKor_ocena` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_idVid_ocena` FOREIGN KEY (`idVid`) REFERENCES `video` (`idVid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES Korisnik WRITE;
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('David', 'dd21@gmail.com', 2001, 'M');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Mila', 'mp21@gmail.com', 2002, 'Z');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Dragisa', 'ddd21@gmail.com', 1967, 'M');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Sofija', 'sm21@gmail.com', 2002, 'Z');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Kristina', 'kk21@gmail.com', 2002, 'Z');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Stefan', 'ss22@gmail.com', 1999, 'M');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Milan', 'mm21@gmail.com', 1998, 'M');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Dragana', 'dd26@gmail.com', 2004, 'Z');
INSERT INTO Korisnik(ime, mejl, godiste, pol) VALUES('Milenko', 'ff21@gmail.com', 2006, 'M');
UNLOCK TABLES;

LOCK TABLES Video WRITE;
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Foke', 543, '2003-04-04', '12:54:30', 1);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Kepibare', 227, '2017-03-04', '02:24:00', 2);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Pandice', 777, '2021-11-30', '09:09:09', 2);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Zmije', 337, '2002-12-31', '06:25:38', 5);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Lame', 874, '2012-11-23', '13:43:13', 3);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Slonovi', 223, '2021-12-04', '12:44:17', 3);
INSERT INTO Video(naziv, trajanje, datum, vrijeme, idKor) VALUES ('Konji', 123, '2022-11-16', '16:24:58', 4);
UNLOCK TABLES;

LOCK TABLE Paket WRITE;
INSERT INTO Paket(Cijena) VALUES(2000);
INSERT INTO Paket(Cijena) VALUES(2450);
INSERT INTO Paket(Cijena) VALUES(3678);
INSERT INTO Paket(Cijena) VALUES(2999);
INSERT INTO Paket(Cijena) VALUES(1780);
INSERT INTO Paket(Cijena) VALUES(4000);
INSERT INTO Paket(Cijena) VALUES(4223);
INSERT INTO Paket(Cijena) VALUES(5000);
UNLOCK TABLES;

LOCK TABLE Pretplata WRITE;
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(1, 2, '2021-01-01', '12:53:07', 2000);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(2, 2, '2020-05-01', '02:23:27', 1600);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(2, 2, '2020-06-01', '22:17:36', 1750);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(3, 5, '2019-11-07', '17:23:37', 1000);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(3, 7, '2023-12-15', '12:57:47', 4223);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(4, 8, '2023-12-01', '14:25:46', 5000);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(4, 8, '2024-01-01', '17:28:59', 5000);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(5, 3, '2024-01-01', '14:00:07', 3000);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(6, 4, '2024-01-01', '12:59:56', 2500);
INSERT INTO Pretplata(idKor, idPak, Datum, Vrijeme, Cijena) VALUES(7, 6, '2024-01-01', '22:22:22', 3400);
UNLOCK TABLES;

LOCK TABLE Gledanje WRITE;
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(1, 2, '2020-09-09', '23:34:54', 10, 54);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(3, 7, '2019-09-19', '13:35:24', 19, 89);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(4, 3, '2018-08-31', '12:12:57', 2, 27);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(2, 6, '2016-05-09', '12:56:54', 3, 78);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(5, 2, '2020-04-21', '23:34:54', 12, 74);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(6, 4, '2020-03-16', '23:34:54', 13, 94);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(7, 7, '2020-11-15', '18:27:54', 17, 24);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(5, 1, '2020-11-15', '23:34:54', 10, 54);
INSERT INTO Gledanje(idVid, idKor, Datum, Vrijeme, Pocetak, Trajanje) VALUES(4, 5, '2020-02-28', '13:32:54', 9, 12);
UNLOCK TABLES;

LOCK TABLE ocena WRITE;
INSERT INTO ocena VALUES(1,5, 2, '2020-09-09', '12:12:57');
INSERT INTO ocena VALUES(3,2, 5, '2018-08-31', '12:12:57');
UNLOCK TABLES;