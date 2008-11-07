SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `spelstegen` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `spelstegen`;

-- -----------------------------------------------------
-- Table `spelstegen`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`players` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`players` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`players` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`players` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`seasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`seasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`seasons` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `startDate` DATE NULL ,
  `endDate` DATE NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`matches`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`matches` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`matches` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `season_id` INT NOT NULL ,
  `date` DATE NULL ,
  `player1_id` INT NULL ,
  `player2_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_matches_seasons` (`season_id` ASC) ,
  INDEX `fk_matches_players` (`player1_id` ASC) ,
  INDEX `fk_matches_players1` (`player2_id` ASC) ,
  CONSTRAINT `fk_matches_seasons`
    FOREIGN KEY (`season_id` )
    REFERENCES `spelstegen`.`seasons` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_players`
    FOREIGN KEY (`player1_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_players1`
    FOREIGN KEY (`player2_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sports` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `iconUrl` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sets` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sets` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `sport_id` INT NOT NULL ,
  `player1Score` INT NULL ,
  `player2Score` INT NULL ,
  `match_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sets_sports` (`sport_id` ASC) ,
  INDEX `fk_sets_matches` (`match_id` ASC) ,
  CONSTRAINT `fk_sets_sports`
    FOREIGN KEY (`sport_id` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sets_matches`
    FOREIGN KEY (`match_id` )
    REFERENCES `spelstegen`.`matches` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagues` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagues` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leaguePlayers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leaguePlayers` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leaguePlayers` (
  `league_id` INT NULL ,
  `player_id` INT NULL ,
  INDEX `fk_leagueplayers_leagues` (`league_id` ASC) ,
  INDEX `fk_leagueplayers_players` (`player_id` ASC) ,
  CONSTRAINT `fk_leagueplayers_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueplayers_players`
    FOREIGN KEY (`player_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueSeasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueSeasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueSeasons` (
  `season_id` INT NULL ,
  `league_id` INT NULL ,
  INDEX `fk_leagueSeasons_seasons` (`season_id` ASC) ,
  INDEX `fk_leagueSeasons_leagues` (`league_id` ASC) ,
  CONSTRAINT `fk_leagueSeasons_seasons`
    FOREIGN KEY (`season_id` )
    REFERENCES `spelstegen`.`seasons` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueSeasons_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `spelstegen`;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`players`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Anders Andersson', 'a@a.se', 'Adde', 'test', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Bengt Bengtsson', 'b@b.se', 'Bengan', 'test', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Carl Carlsson', 'c@c.se', 'Calle', 'test', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Daniel Danielsson', 'd@d.se', 'Danne', 'test', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Erik Eriksson', 'e@e.se', 'Erkan', 'test', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Filip Filipsson', 'f@f.se', 'Fille', 'test', '');

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`matches`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-01-05', 1, 2);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-01-20', 3, 4);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-02-10', 1, 4);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-03-02', 2, 3);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-04-07', 1, 3);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-05-01', 4, 2);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 1, '2008-05-30', 3, 2);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 2, '2008-08-03', 4, 3);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 2, '2008-09-01', 2, 4);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 2, '2008-10-10', 1, 2);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 2, '2008-11-03', 2, 3);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 3, '2008-01-05', 3, 5);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 3, '2008-04-02', 4, 6);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 4, '2008-08-20', 4, 5);
INSERT INTO `matches` (`id`, `season_id`, `date`, `player1_id`, `player2_id`) VALUES (0, 4, '2008-10-12', 3, 6);

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`sets`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 3, 1);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 7, 15, 2);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 0, 3);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 5, 4);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 0, 15, 5);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 3, 6);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 6, 7);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 10, 7);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 3, 21, 8);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 2, 9);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 12, 15, 9);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 10, 9);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 9, 15, 10);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 15, 1, 11);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 1, 21, 3, 12);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 2, 4, 21, 12);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 18, 12);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 4, 3, 21, 12);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 1, 21, 4, 13);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 2, 1, 21, 13);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 10, 13);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 4, 2, 21, 13);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 1, 21, 2, 14);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 2, 21, 16, 14);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 4, 14);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 4, 18, 21, 14);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 1, 21, 10, 15);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 2, 18, 21, 15);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 3, 21, 1, 15);
INSERT INTO `sets` (`id`, `sport_id`, `player1Score`, `player2Score`, `match_id`) VALUES (0, 4, 15, 21, 15);

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`sports`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `sports` (`id`, `name`, `iconUrl`) VALUES (0, 'Bordtennis', '');
INSERT INTO `sports` (`id`, `name`, `iconUrl`) VALUES (0, 'Badminton', '');
INSERT INTO `sports` (`id`, `name`, `iconUrl`) VALUES (0, 'Squash', '');
INSERT INTO `sports` (`id`, `name`, `iconUrl`) VALUES (0, 'Tennis', '');

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`leagues`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `leagues` (`id`, `name`) VALUES (0, 'Epsilon IT Väst Squashstege');
INSERT INTO `leagues` (`id`, `name`) VALUES (0, 'Telia GBG Racketlon');

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`leaguePlayers`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (1, 1);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (1, 2);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (1, 3);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (1, 4);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (2, 3);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (2, 4);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (2, 5);
INSERT INTO `leaguePlayers` (`league_id`, `player_id`) VALUES (2, 6);

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`seasons`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `seasons` (`id`, `name`, `startDate`, `endDate`) VALUES (0, 'Epsilon IT Väst Squashstege VT08', '2008-01-01', '2008-05-31');
INSERT INTO `seasons` (`id`, `name`, `startDate`, `endDate`) VALUES (0, 'Epsilon IT Väst Squashstege HT08', '2008-08-01', '9999-12-31');
INSERT INTO `seasons` (`id`, `name`, `startDate`, `endDate`) VALUES (0, 'Telia GBG Racketlon VT08', '2008-01-01', '2008-05-31');
INSERT INTO `seasons` (`id`, `name`, `startDate`, `endDate`) VALUES (0, 'Telia GBG Racketlon HT08', '2008-08-01', '9999-12-31');

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`leagueSeasons`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `leagueSeasons` (`season_id`, `league_id`) VALUES (1, 1);
INSERT INTO `leagueSeasons` (`season_id`, `league_id`) VALUES (2, 1);
INSERT INTO `leagueSeasons` (`season_id`, `league_id`) VALUES (3, 2);
INSERT INTO `leagueSeasons` (`season_id`, `league_id`) VALUES (4, 2);

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
