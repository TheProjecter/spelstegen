USE spelstegen;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS matches;

CREATE TABLE players(
id VARCHAR(8) NOT NULL,
PRIMARY KEY(id),
name VARCHAR(50),
password VARCHAR(15),
email VARCHAR(50));

CREATE TABLE matches(
id VARCHAR(8) NOT NULL,
PRIMARY KEY(id),
date DATE,
player1 VARCHAR(8),
player2 VARCHAR(8),
sets varchar(100),
season VARCHAR(8));