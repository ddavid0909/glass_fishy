UNLOCK TABLES;
DROP DATABASE IF EXISTS `baza2`;
CREATE DATABASE `baza2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE baza2;


DROP TABLE IF EXISTS korisnik;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL AUTO_INCREMENT,
  `ime` varchar(45) NOT NULL,
  `mejl` varchar(45) UNIQUE NOT NULL,
  `godiste` INT NOT NULL,
  `pol` varchar(1) NOT NULL,
  PRIMARY KEY (`idKor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS Kategorija;
CREATE TABLE `Kategorija` (
  `idKat` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`idKat`)
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

DROP TABLE IF EXISTS pripada;
CREATE TABLE `pripada` (
  `idVid` int NOT NULL,
  `idKat` int NOT NULL,
  PRIMARY KEY (`idVid`,`idKat`),
  KEY `FK_IdKat_pripada_idx` (`idKat`),
  CONSTRAINT `FK_IdKat_pripada` FOREIGN KEY (`idKat`) REFERENCES `Kategorija` (`idKat`) ON UPDATE CASCADE,
  CONSTRAINT `FK_IdVid_pripada` FOREIGN KEY (`idVid`) REFERENCES `video` (`idVid`) ON UPDATE CASCADE
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

LOCK TABLES Kategorija WRITE;
INSERT INTO Kategorija(naziv) VALUES('fantazija');
INSERT INTO Kategorija(naziv) VALUES('akcija');
INSERT INTO Kategorija(naziv) VALUES('krimi');
INSERT INTO Kategorija(naziv) VALUES('vestern');
INSERT INTO Kategorija(naziv) VALUES('drama');
INSERT INTO Kategorija(naziv) VALUES('komedija');
INSERT INTO Kategorija(naziv) VALUES('romansa');
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

LOCK TABLE Pripada WRITE;
INSERT INTO Pripada VALUES(1, 3);
INSERT INTO Pripada VALUES(1, 4);
INSERT INTO Pripada VALUES(2, 3);
INSERT INTO Pripada VALUES(3, 7);
INSERT INTO Pripada VALUES(3, 5);
INSERT INTO Pripada VALUES(4, 6);
INSERT INTO Pripada VALUES(5, 7);
INSERT INTO Pripada VALUES(6, 6);
INSERT INTO Pripada VALUES(6, 2);
INSERT INTO Pripada VALUES(5, 1);
INSERT INTO Pripada VALUES(6, 1);
INSERT INTO Pripada VALUES(6, 4);
INSERT INTO Pripada VALUES(7, 2);
INSERT INTO Pripada VALUES(7, 3);
INSERT INTO Pripada VALUES(7, 5);
INSERT INTO Pripada VALUES(1, 6);
UNLOCK TABLES;