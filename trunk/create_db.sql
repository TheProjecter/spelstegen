USE spelstegen;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS sets;
DROP TABLE IF EXISTS sports;
DROP TABLE IF EXISTS leagues;
DROP TABLE IF EXISTS leagueplayers;

CREATE TABLE players (
	playerId INT NOT NULL AUTO_INCREMENT,
	email VARCHAR(50) NOT NULL,
	PRIMARY KEY (playerId, email),
	name VARCHAR(50),
	nickname VARCHAR(50),
	password VARCHAR(50),
	imageUrl VARCHAR(50)
);

CREATE TABLE matches(
	matchId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	leagueId INT,
	date DATE,
	player1Id INT,
	player2Id INT
);

CREATE TABLE sets(
	setId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	matchId INT,
	sportId INT,
	player1Score INT,
	player2Score INT
);

CREATE TABLE sports(
	sportId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50),
	iconUrl VARCHAR(50)
);

CREATE TABLE leagues(
	leagueId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50)
);

CREATE TABLE leagueplayers(
	leagueplayerId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	playerId INT,
	leagueId INT
);