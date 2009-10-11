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
ENGINE = InnoDB
COMMENT = 'Info about all player that are part of one or more league.';


-- -----------------------------------------------------
-- Table `spelstegen`.`sports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sports` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `iconUrl` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'Contains all available sports to compete in.';


-- -----------------------------------------------------
-- Table `spelstegen`.`leagues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagues` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagues` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'This table contains all available leagues.';


-- -----------------------------------------------------
-- Table `spelstegen`.`matches`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`matches` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`matches` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `date` DATE NULL ,
  `sport_id` INT NOT NULL ,
  `league_id` INT NOT NULL ,
  `player1_id` INT NOT NULL ,
  `player2_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_matches_sport` (`sport_id` ASC) ,
  INDEX `fk_matches_leagues` (`league_id` ASC) ,
  INDEX `fk_matches_player1` (`player1_id` ASC) ,
  INDEX `fk_matches_player2` (`player2_id` ASC) ,
  CONSTRAINT `fk_matches_sport`
    FOREIGN KEY (`sport_id` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_player1`
    FOREIGN KEY (`player1_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_player2`
    FOREIGN KEY (`player2_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Represent a math between to players';


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
ENGINE = InnoDB
COMMENT = 'Represents a set in a match between to players.';


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
ENGINE = InnoDB
COMMENT = 'This table contains all leagues that a player is part of.';


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
ENGINE = InnoDB
COMMENT = 'A season represents a time period with a start and end date.';


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


-- -----------------------------------------------------
-- Table `spelstegen`.`childSports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`childSports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`childSports` (
  `parentSportId` INT NOT NULL ,
  `childSportId` INT NOT NULL ,
  PRIMARY KEY (`parentSportId`, `childSportId`) ,
  INDEX `fk_parent_sport` (`parentSportId` ASC) ,
  INDEX `fk_child_sport` (`childSportId` ASC) ,
  CONSTRAINT `fk_parent_sport`
    FOREIGN KEY (`parentSportId` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_child_sport`
    FOREIGN KEY (`childSportId` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This table defines sports that are part of another sport.';


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueSports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueSports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueSports` (
  `leagueId` INT NOT NULL ,
  `sportId` INT NOT NULL ,
  PRIMARY KEY (`leagueId`, `sportId`) ,
  INDEX `fk_league` (`leagueId` ASC) ,
  INDEX `fk_sport` (`sportId` ASC) ,
  CONSTRAINT `fk_league`
    FOREIGN KEY (`leagueId` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sport`
    FOREIGN KEY (`sportId` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'The sports that are part of a league.';


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueMatchAdmins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueMatchAdmins` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueMatchAdmins` (
  `league_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  INDEX `fk_leagueMatchAdmins_leagues` (`league_id` ASC) ,
  INDEX `fk_leagueMatchAdmins_players` (`player_id` ASC) ,
  CONSTRAINT `fk_leagueMatchAdmins_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueMatchAdmins_players`
    FOREIGN KEY (`player_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueAdmins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueAdmins` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueAdmins` (
  `league_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  INDEX `fk_leagueAdmins_leagues` (`league_id` ASC) ,
  INDEX `fk_leagueAdmins_players` (`player_id` ASC) ,
  CONSTRAINT `fk_leagueAdmins_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueAdmins_players`
    FOREIGN KEY (`player_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `spelstegen`;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`players`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Anders Andersson', 'a@a.se', 'Adde', '098f6bcd4621d373cade4e832627b4f6', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Bengt Bengtsson', 'b@b.se', 'Bengan', '098f6bcd4621d373cade4e832627b4f6', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Carl Carlsson', 'c@c.se', 'Calle', '098f6bcd4621d373cade4e832627b4f6', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Daniel Danielsson', 'd@d.se', 'Danne', '098f6bcd4621d373cade4e832627b4f6', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Erik Eriksson', 'e@e.se', 'Erkan', '098f6bcd4621d373cade4e832627b4f6', '');
INSERT INTO `players` (`id`, `name`, `email`, `nickname`, `password`, `image_url`) VALUES (0, 'Filip Filipsson', 'f@f.se', 'Fille', '098f6bcd4621d373cade4e832627b4f6', '');

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`matches`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-01-05', 3, 1, 1, 2);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-01-20', 3, 1, 3, 4);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-02-10', 3, 1, 1, 4);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-03-02', 3, 1, 2, 3);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-04-07', 3, 1, 1, 3);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-05-01', 3, 1, 4, 2);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-05-30', 3, 1, 3, 2);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-08-03', 3, 1, 4, 3);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-09-01', 3, 1, 2, 4);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-10-10', 3, 1, 1, 2);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-11-03', 3, 1, 2, 3);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-01-05', 5, 2, 3, 5);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-04-02', 5, 2, 4, 6);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-08-20', 5, 2, 4, 5);
INSERT INTO `matches` (`id`, `date`, `sport_id`, `league_id`, `player1_id`, `player2_id`) VALUES (0, '2008-10-12', 5, 2, 3, 6);

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
INSERT INTO `sports` (`id`, `name`, `iconUrl`) VALUES (0, 'Racketlon', '');

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

-- -----------------------------------------------------
-- Data for table `spelstegen`.`childSports`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `childSports` (`parentSportId`, `childSportId`) VALUES (5, 1);
INSERT INTO `childSports` (`parentSportId`, `childSportId`) VALUES (5, 2);
INSERT INTO `childSports` (`parentSportId`, `childSportId`) VALUES (5, 3);
INSERT INTO `childSports` (`parentSportId`, `childSportId`) VALUES (5, 4);

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`leagueSports`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `leagueSports` (`leagueId`, `sportId`) VALUES (1, 3);
INSERT INTO `leagueSports` (`leagueId`, `sportId`) VALUES (2, 5);

COMMIT;

-- -----------------------------------------------------
-- Data for table `spelstegen`.`leagueMatchAdmins`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `leagueMatchAdmins` (`league_id`, `player_id`) VALUES (1, 1);
INSERT INTO `leagueMatchAdmins` (`league_id`, `player_id`) VALUES (2, 6);

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
