USE spelstegen;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS matches;

CREATE TABLE players(
email VARCHAR(50) NOT NULL,
PRIMARY KEY(email),
name VARCHAR(50),
password VARCHAR(50));

CREATE TABLE matches(
id MEDIUMINT NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
date DATE,
player1 VARCHAR(50),
player2 VARCHAR(50),
sets varchar(100),
season VARCHAR(8));