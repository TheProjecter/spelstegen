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

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
