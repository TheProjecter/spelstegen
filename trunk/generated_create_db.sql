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
  `playerid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`playerid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`players` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`players` (
  `playerid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`playerid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`seasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`seasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`seasons` (
  `seasonid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `startDate` DATE NULL ,
  `endDate` DATE NULL ,
  PRIMARY KEY (`seasonid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`matches`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`matches` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`matches` (
  `matchid` INT NOT NULL AUTO_INCREMENT ,
  `seasonid` INT NULL ,
  `date` DATE NULL ,
  `player1id` INT NULL ,
  `player2id` INT NULL ,
  PRIMARY KEY (`matchid`) ,
  INDEX `fk_matches_players` (`player1id` ASC, `player2id` ASC) ,
  INDEX `fk_matches_seasons` (`seasonid` ASC) ,
  CONSTRAINT `fk_matches_players`
    FOREIGN KEY (`player1id` , `player2id` )
    REFERENCES `spelstegen`.`players` (`playerid` , `playerid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_seasons`
    FOREIGN KEY (`seasonid` )
    REFERENCES `spelstegen`.`seasons` (`seasonid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sports` (
  `sportid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `iconUrl` VARCHAR(50) NULL ,
  PRIMARY KEY (`sportid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sets` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sets` (
  `setid` INT NOT NULL AUTO_INCREMENT ,
  `sportid` INT NULL ,
  `player1Score` INT NULL ,
  `player2Score` INT NULL ,
  `matchid` INT NULL ,
  PRIMARY KEY (`setid`) ,
  INDEX `fk_sets_sports` (`sportid` ASC) ,
  INDEX `fk_sets_matches` (`matchid` ASC) ,
  CONSTRAINT `fk_sets_sports`
    FOREIGN KEY (`sportid` )
    REFERENCES `spelstegen`.`sports` (`sportid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sets_matches`
    FOREIGN KEY (`matchid` )
    REFERENCES `spelstegen`.`matches` (`matchid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagues` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagues` (
  `leagueid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  PRIMARY KEY (`leagueid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leaguePlayers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leaguePlayers` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leaguePlayers` (
  `leagueid` INT NULL ,
  `playerid` INT NULL ,
  INDEX `fk_leagueplayers_leagues` (`leagueid` ASC) ,
  INDEX `fk_leagueplayers_players` (`playerid` ASC) ,
  CONSTRAINT `fk_leagueplayers_leagues`
    FOREIGN KEY (`leagueid` )
    REFERENCES `spelstegen`.`leagues` (`leagueid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueplayers_players`
    FOREIGN KEY (`playerid` )
    REFERENCES `spelstegen`.`players` (`playerid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueSeasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueSeasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueSeasons` (
  `seasons_seasonid` INT NULL ,
  `leagues_leagueid` INT NULL ,
  INDEX `fk_leagueSeasons_seasons` (`seasons_seasonid` ASC) ,
  INDEX `fk_leagueSeasons_leagues` (`leagues_leagueid` ASC) ,
  CONSTRAINT `fk_leagueSeasons_seasons`
    FOREIGN KEY (`seasons_seasonid` )
    REFERENCES `spelstegen`.`seasons` (`seasonid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueSeasons_leagues`
    FOREIGN KEY (`leagues_leagueid` )
    REFERENCES `spelstegen`.`leagues` (`leagueid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
